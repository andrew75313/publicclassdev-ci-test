package com.sparta.publicclassdev.domain.chatrooms.entity;

import com.sparta.publicclassdev.domain.users.entity.Users;
import com.sparta.publicclassdev.global.entity.Timestamped;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;

@Entity
@Table(name = "messages")
public class Messages extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contents;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users users;

    @ManyToOne
    @JoinColumn(name = "chatrooms_id")
    private ChatRooms chatRooms;

    @Builder
    public Messages(String contents, Users users, ChatRooms chatRooms) {
        this.contents = contents;
        this.users = users;
        this.chatRooms = chatRooms;
    }
}
