package org.balikin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.balikin.entity.FriendRequest;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter


public class FriendRequestDto {
    private String to;
    private FriendRequest.FriendRequestStatus status;

}

