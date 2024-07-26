package com.sparta.publicclassdev.domain.chatrooms.service;

import com.sparta.publicclassdev.domain.chatrooms.dto.ChatRoomsDto;
import com.sparta.publicclassdev.domain.chatrooms.dto.ChatRoomsDto.MessageType;
import com.sparta.publicclassdev.domain.chatrooms.entity.ChatRooms;
import com.sparta.publicclassdev.domain.chatrooms.entity.Messages;
import com.sparta.publicclassdev.domain.chatrooms.repository.ChatRoomsRepository;
import com.sparta.publicclassdev.domain.chatrooms.repository.MessagesRepository;
import com.sparta.publicclassdev.domain.users.entity.Users;
import com.sparta.publicclassdev.domain.users.repository.UsersRepository;
import com.sparta.publicclassdev.global.exception.CustomException;
import com.sparta.publicclassdev.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomsService {

    private final SimpMessageSendingOperations operations;
    private final UsersRepository usersRepository;
    private final ChatRoomsRepository chatRoomsRepository;
    private final MessagesRepository messagesRepository;

    public void sendMessage(ChatRoomsDto chatroomsDto) {
        Users users = usersRepository.findByName(chatroomsDto.getSender())
            .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));
        ChatRooms chatRooms = chatRoomsRepository.findById(Long.parseLong(chatroomsDto.getRoomId()))
            .orElseThrow(()-> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        Messages messages = Messages.builder()
            .contents(chatroomsDto.getContent())
            .users(users)
            .chatRooms(chatRooms)
            .build();
        messagesRepository.save(messages);

        operations.convertAndSend("/topic/chatroom/" + chatroomsDto.getRoomId(), chatroomsDto);
    }

    public void addUser(ChatRoomsDto chatRoomsDto) {
        ChatRoomsDto chatRooms = ChatRoomsDto.builder()
            .type(MessageType.JOIN)
            .sender(chatRoomsDto.getSender())
            .roomId(chatRoomsDto.getRoomId())
            .content(chatRoomsDto.getSender())
            .build();
        operations.convertAndSend("/topic/chatroom/" + chatRoomsDto.getRoomId(), chatRooms);
    }
}
