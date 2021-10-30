package com.dut.sweetchatapp.service;

import com.dut.sweetchatapp.enums.MessageStatus;
import com.dut.sweetchatapp.model.ChatMessage;

import java.util.List;

public interface ChatMessageService {

    ChatMessage save(ChatMessage chatMessage);

    int countNewMessages(int senderId, int receiverId);

    List<ChatMessage> findChatMessages(int senderId, int receiverId);

    void updateStatuses(int senderId, int receiverId, MessageStatus status);

    ChatMessage findById(int id);

}
