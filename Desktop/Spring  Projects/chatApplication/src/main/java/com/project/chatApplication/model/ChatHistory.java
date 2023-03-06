package com.project.chatApplication.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_chat_history")
public class ChatHistory {
    @Id
    @Column(name = "chat_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int chatId;
    @JoinColumn(name = "receiver_user_id")
    @ManyToOne
    private Users receiver;
    @JoinColumn(name = "sender_user_id")
    @ManyToOne
    private Users sender;
    @Column(name = "message")
    private String message;
    @Column(name = "created_date")
    private Timestamp createdDate;
    @Column(name = "updated_date")
    private Timestamp updatedDate;
    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status statusId;
}
