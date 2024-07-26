package com.sparta.publicclassdev.domain.chatrooms.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomsDto {

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }

    private MessageType type;
    private String content;
    private String sender;
    private String roomId;

    @Builder
    public ChatRoomsDto(MessageType type, String content, String sender, String roomId) {
        this.type = type;
        this.content = content;
        this.sender = sender;
        this.roomId = roomId;
    }
}
