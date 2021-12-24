package com.dut.sweetchatapp.model;

import com.dut.sweetchatapp.enums.MessageStatus;
import com.dut.sweetchatapp.enums.MessageType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String roomId;

    private int senderId;

    private String senderName;

    private int receiverId;

    private String receiverName;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    private byte[] file;

    private MessageType messageType;

    @CreatedDate
    @Column(updatable = false)
    private Date createdDate = new Date(System.currentTimeMillis());

    @LastModifiedDate
    private Date updatedDate = new Date(System.currentTimeMillis());

    private boolean isDeleted;

    private MessageStatus status;

}
