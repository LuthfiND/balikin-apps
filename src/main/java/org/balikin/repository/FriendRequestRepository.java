package org.balikin.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.balikin.dto.FriendRequestDto;
import org.balikin.entity.Auth;
import org.balikin.entity.FriendRequest;

import java.util.Optional;
@ApplicationScoped

public class FriendRequestRepository implements PanacheRepositoryBase<FriendRequest, Long> {
    @Transactional
    public Optional<FriendRequestDto> addFriendRequest(FriendRequest friendRequest) {
    persist(friendRequest);
    FriendRequestDto friendRequestDto = new FriendRequestDto();
    friendRequestDto.setStatus(friendRequest.getStatus());
    friendRequestDto.setTo(friendRequestDto.getTo());
    return Optional.of(friendRequestDto);
    }
    public Boolean existByFromAndTo (Auth from ,Auth to) {
        return find("sender = ?1 and to = ?2", from, to).firstResultOptional().isPresent();
    }
    public Boolean isFriendShip (Auth from, Auth to) {
        return find("(sender = ?1 and to = ?2 OR sender = ?2 and to =?1) AND status = ?3" , from, to, FriendRequest.FriendRequestStatus.ACCEPTED).firstResultOptional().isPresent();
    }

}
