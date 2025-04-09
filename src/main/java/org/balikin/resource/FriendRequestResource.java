package org.balikin.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.balikin.dto.ApproveRequestDto;
import org.balikin.dto.FriendRequestDto;
import org.balikin.dto.RequestFriendResponseDto;
import org.balikin.model.ApiResponse;
import org.balikin.service.impl.FriendRequestServiceImpl;

import java.util.List;
import java.util.Optional;

@Slf4j
@Path("friend-request")
@RolesAllowed({"USER"})
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FriendRequestResource {
    @Inject
    private FriendRequestServiceImpl friendRequestService;

    @POST
    @Transactional
    @Path("/send-friend")
    public ApiResponse<FriendRequestDto> sendFriendRequest(FriendRequestDto friendRequestDto) throws Exception {
        Optional<FriendRequestDto> result = friendRequestService.FriendRequest(friendRequestDto.getTo());
        if (result.isEmpty()) {
            return new ApiResponse<FriendRequestDto>("Data Tidak ditemukan", null, 400);
        }
        FriendRequestDto friendRequest = new FriendRequestDto();
        friendRequest.setTo(result.get().getTo());
        friendRequest.setStatus(result.get().getStatus());
        if (result.isPresent()) {
            return new ApiResponse<FriendRequestDto>("success", friendRequest, 200);
        } else {
            return new ApiResponse<>("failed", null, 400);
        }
    }

    @GET
    @Path("all-request")
    public ApiResponse<List<RequestFriendResponseDto>> getAllRequest(@QueryParam("status") String status) throws Exception {
        List<RequestFriendResponseDto> allFriendRequest = friendRequestService.getAllRequest(status);
        if (allFriendRequest.isEmpty()) {
            return new ApiResponse<>("Data Tidak ditemukan", null, 400);
        }
        return new ApiResponse<List<RequestFriendResponseDto>>("success", allFriendRequest, 200);
    }
    @GET
    @Path("request-pending")
    public ApiResponse<List<RequestFriendResponseDto>> getPendingRequest() throws Exception {
        List<RequestFriendResponseDto> allFriendRequest = friendRequestService.getStatusPending();
        if (allFriendRequest.isEmpty()) {
            return new ApiResponse<>("Data Tidak ditemukan", null, 400);
        }
        return new ApiResponse<List<RequestFriendResponseDto>>("success", allFriendRequest, 200);
    }

    @GET
    @Path("request/{id}")
    public ApiResponse<RequestFriendResponseDto> getRequest(@PathParam("id") Long id) throws Exception {
        RequestFriendResponseDto friendRequestById = friendRequestService.getRequestById(id);
        return new ApiResponse<RequestFriendResponseDto>("success", friendRequestById, 200);
    }

    @POST
    @Transactional
    @Path("approve-or-rejected")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse<?> approveOrReject (ApproveRequestDto approveRequestDto) throws Exception {
        friendRequestService.ApproveOrRejectRequest(approveRequestDto.id,approveRequestDto.status);
        return new ApiResponse<>("Success", null, 200);
    }


}
