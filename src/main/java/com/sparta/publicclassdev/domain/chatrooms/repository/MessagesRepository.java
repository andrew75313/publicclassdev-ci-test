package com.sparta.publicclassdev.domain.chatrooms.repository;

import com.sparta.publicclassdev.domain.chatrooms.entity.Messages;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessagesRepository extends JpaRepository<Messages, Long> {

}
