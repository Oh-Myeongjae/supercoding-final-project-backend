package com.github.supercodingfinalprojectbackend.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private Long chatRoomId;
    private Long senderId;
    private String sendAt;
    private String chatContent;
    @Getter
    @Setter
    public static class CreateChatRoomRequest {
        private Long mentorId;
    }

    @Getter
    @Setter
    public static class ChatRoomListRequest {
        private Long userId;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ResponseChatRoom {
        private List<ResponseChatRoomInfo> chatRoom;
    }

    @Getter
    @Setter
    public static class ResponseChatRoomInfo {
        private Long chatroomId;
        private String lastChat;
        private Integer unreadCount;
        private String profileImg;
        private String partnerName;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseMessage {
        private Long senderId;
        private String sendAt;
        private String chatContent;
        private String DbSendAt;
    }
}
