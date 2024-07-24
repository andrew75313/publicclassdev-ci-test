package com.sparta.publicclassdev.domain.chatrooms.entity;

import com.sparta.publicclassdev.domain.users.entity.Users;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "chatroomusers")
public class ChatRoomUsers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users users;

    @ManyToOne
    @JoinColumn(name = "chatrooms_id")
    private ChatRooms chatRooms;

    public void setChatRooms(ChatRooms chatRooms) {
    }

    public void setUsers(Users users) {
    }
}
