package com.dut.sweetchatapp.service;

import com.dut.sweetchatapp.enums.MessageStatus;
import com.dut.sweetchatapp.model.ChatMessage;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

public interface ChatMessageService {

    ChatMessage save(ChatMessage chatMessage);

    int countNewMessages(int senderId, int receiverId);

    List<ChatMessage> findChatMessages(int senderId, int receiverId) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException;

    void updateStatuses(int senderId, int receiverId, MessageStatus status);

    ChatMessage findById(int id) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException;

}
