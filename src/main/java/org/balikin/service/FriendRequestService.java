package org.balikin.service;
import org.balikin.dto.FriendRequestDto;
import org.balikin.dto.RequestFriendResponseDto;
import org.balikin.entity.FriendRequest;

import java.util.List;
import java.util.Optional;


public interface FriendRequestService {
     Optional<FriendRequestDto> FriendRequest(String to) throws Exception;
     List<RequestFriendResponseDto> getAllRequest(String status) throws Exception;
     RequestFriendResponseDto getRequestById (Long id) throws Exception;
     void ApproveOrRejectRequest (Long id,String status) throws Exception;


}
