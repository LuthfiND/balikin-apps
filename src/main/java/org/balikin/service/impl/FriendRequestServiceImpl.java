package org.balikin.service.impl;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import org.balikin.dto.FriendRequestDto;
import org.balikin.dto.RequestFriendResponseDto;
import org.balikin.entity.Auth;
import org.balikin.entity.FriendRequest;
import org.balikin.repository.AuthRepository;
import org.balikin.repository.FriendRequestRepository;
import org.balikin.service.FriendRequestService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class FriendRequestServiceImpl implements FriendRequestService {
    @Inject
    AuthRepository authRepository;
    @Inject
    FriendRequestRepository friendRequestRepository;
    @Inject
    SecurityIdentity securityIdentity;
    @Override
    public Optional<FriendRequestDto> FriendRequest(String to) throws Exception {
        String id = securityIdentity.getPrincipal().getName();
        Optional<Auth>  currentUser = Optional.ofNullable(authRepository.findById(id));
    Optional<Auth> toUser =  authRepository.FindByEmail(to);
     if(toUser.isEmpty()) {
         throw new Exception("User dengan " + to + " tidak ditemukan" );
     }
     if (currentUser.isEmpty()) {
         throw new Exception("User dengan " + currentUser.get().getEmail() + " tidak ditemukan" );
     }
    if (friendRequestRepository.existByFromAndTo(currentUser.get(), toUser.get())) {
        throw new Exception("Anda sudah Menambahkan ke email " + to );
    }
    FriendRequest friendRequest = new FriendRequest();
    friendRequest.setTo(toUser.get());
    friendRequest.setSender(currentUser.get());
    friendRequest.setCreatedDate(new Date());
    friendRequest.setStatus(FriendRequest.FriendRequestStatus.PENDING);
    return friendRequestRepository.addFriendRequest(friendRequest);
    }

    @Override
    public List<RequestFriendResponseDto> getAllRequest(String status) throws Exception {
        String id = securityIdentity.getPrincipal().getName();
        Long senderId = Long.parseLong(id);
        List<FriendRequest> allFriendRequest;
        if (status == null || status.isEmpty()) {
            allFriendRequest = friendRequestRepository
                    .find("sender.id = ?1 or to.id = ?1", senderId)
                    .list();        } else {
            FriendRequest.FriendRequestStatus statusEnum;
            try {
                statusEnum = FriendRequest.FriendRequestStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Status tidak valid");
            }
            allFriendRequest = friendRequestRepository
                    .find("(sender.id = ?1 or to.id = ?1) and status = ?2", senderId, statusEnum)
                    .list();
        }

        return allFriendRequest.stream().map(friendRequest -> {
            RequestFriendResponseDto dto = new RequestFriendResponseDto();
            dto.setId(friendRequest.getId());
            dto.setSent_at(friendRequest.getCreatedDate());
            dto.setStatus(friendRequest.getStatus());

            if (Long.valueOf(friendRequest.getSender().getId()).equals(senderId)) {
                dto.setSender_email(friendRequest.getSender().getEmail());
                dto.setSender_name(friendRequest.getSender().getUsername());
                dto.setReceived_email(friendRequest.getTo().getEmail());
                dto.setReceived_name(friendRequest.getTo().getUsername());
                dto.setImage_url(friendRequest.getTo().getImageUrl());
            } else {
                dto.setSender_email(friendRequest.getTo().getEmail());
                dto.setSender_name(friendRequest.getTo().getUsername());
                dto.setReceived_email(friendRequest.getSender().getEmail());
                dto.setReceived_name(friendRequest.getSender().getUsername());
                dto.setImage_url(friendRequest.getSender().getImageUrl());
            }

            return dto;
        }).toList();

    }

    @Override
    public List<RequestFriendResponseDto> getStatusPending() throws Exception {
        String id = securityIdentity.getPrincipal().getName();
        Long senderId = Long.parseLong(id);

        List<FriendRequest> allFriendRequest = friendRequestRepository
                .find("sender.id = ?1 or to.id = ?1", senderId)
                .list();

        return allFriendRequest.stream()
                .filter(friendRequest -> !Long.valueOf(friendRequest.getSender().getId()).equals(senderId) && friendRequest.getStatus().equals(FriendRequest.FriendRequestStatus.PENDING))
                .map(friendRequest -> {
                    RequestFriendResponseDto dto = new RequestFriendResponseDto();
                    dto.setId(friendRequest.getId());
                    dto.setSent_at(friendRequest.getCreatedDate());
                    dto.setStatus(friendRequest.getStatus());
                    dto.setSender_email(friendRequest.getTo().getEmail());
                    dto.setSender_name(friendRequest.getTo().getUsername());
                    dto.setReceived_email(friendRequest.getSender().getEmail());
                    dto.setReceived_name(friendRequest.getSender().getUsername());
                    dto.setImage_url(friendRequest.getSender().getImageUrl());

                    return dto;
                })
                .toList();
    }

    @Override
    public RequestFriendResponseDto getRequestById(Long id) throws Exception {
        FriendRequest friendRequestById = friendRequestRepository.findById(id);
        RequestFriendResponseDto requestFriendResponseDto = new RequestFriendResponseDto();
        requestFriendResponseDto.setId(friendRequestById.getId());
        requestFriendResponseDto.setReceived_email(friendRequestById.getTo().getEmail());
        requestFriendResponseDto.setReceived_name(friendRequestById.getTo().getUsername());
        requestFriendResponseDto.setSender_email(friendRequestById.getSender().getEmail());
        requestFriendResponseDto.setSender_name(friendRequestById.getSender().getUsername());
        requestFriendResponseDto.setSent_at(friendRequestById.getCreatedDate());
        requestFriendResponseDto.setStatus(friendRequestById.getStatus());
        return requestFriendResponseDto;
    }

    @Override
    public void ApproveOrRejectRequest(Long id, String status) throws Exception {
        FriendRequest friendRequestById = friendRequestRepository.findById(id);
        String currentUserEmail = securityIdentity.getPrincipal().getName();

        if (!friendRequestById.getTo().getId().equals(Integer.parseInt(currentUserEmail))) {
            throw new BadRequestException("Hanya Penerima yang Bisa Mengubah Status Permintaan Pertemanan");
        }
        if (friendRequestById.getStatus() == FriendRequest.FriendRequestStatus.PENDING) {
            friendRequestById.setStatus(FriendRequest.FriendRequestStatus.valueOf(status));
            friendRequestRepository.persist(friendRequestById);
        } else {
            throw new BadRequestException("Status Anda Sudah Menjadi REJECTED atau ACCEPTED");
        }
    }
}
