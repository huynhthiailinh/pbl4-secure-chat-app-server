package com.dut.sweetchatapp.model;

import com.dut.sweetchatapp.enums.MessageStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    @Column(columnDefinition = "TEXT")
    private String content;

    @CreatedDate
    @Column(updatable = false)
    private Date createdDate = new Date(System.currentTimeMillis());

    @LastModifiedDate
    private Date updatedDate = new Date(System.currentTimeMillis());

    private boolean isDeleted;

    private MessageStatus status;

}
