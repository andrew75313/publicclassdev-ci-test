package com.sparta.publicclassdev.domain.chatrooms.controller;

import com.sparta.publicclassdev.domain.chatrooms.dto.ChatRoomsDto;
import com.sparta.publicclassdev.domain.chatrooms.service.ChatRoomsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatRoomsController {

    private final ChatRoomsService chatRoomsService;

    @MessageMapping("/chat.sentMessage")
    public void sendMessage(@Payload ChatRoomsDto chatroomsDto) {
        log.info("Receive message : {}", chatroomsDto);
        chatRoomsService.sendMessage(chatroomsDto);
    }

    @MessageMapping("/chat.addUser")
    public void addUser(@Payload ChatRoomsDto chatroomsDto, SimpMessageHeaderAccessor headerAccessor) {
        log.info("User join : {}", chatroomsDto);
        chatRoomsService.addUser(chatroomsDto);
        headerAccessor.getSessionAttributes().put("username", chatroomsDto.getSender());
    }
}
