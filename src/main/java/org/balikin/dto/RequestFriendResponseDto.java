package org.balikin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.balikin.entity.FriendRequest;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequestFriendResponseDto {
    private Long id;
    private String received_email;
    private String sender_email;
    private String sender_name;
    private String received_name;
    private Date sent_at;
    private FriendRequest.FriendRequestStatus status;
    private String image_url;

}
