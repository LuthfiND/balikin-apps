package org.balikin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.balikin.entity.FriendRequest;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AcceptOrRejectReqFriendDto {
    @NotBlank
    private String FriendRequestId;
    private FriendRequest.FriendRequestStatus status;
}
