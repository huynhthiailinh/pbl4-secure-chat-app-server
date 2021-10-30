package com.dut.sweetchatapp.service.impl;

import com.dut.sweetchatapp.enums.MessageStatus;
import com.dut.sweetchatapp.handleException.exception.ObjectNotFoundException;
import com.dut.sweetchatapp.model.ChatMessage;
import com.dut.sweetchatapp.repository.ChatMessageRepository;
import com.dut.sweetchatapp.service.ChatMessageService;
import com.dut.sweetchatapp.service.ChatRoomService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.dut.sweetchatapp.enums.MessageStatus.RECEIVED;

@AllArgsConstructor
@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    private final ChatRoomService chatRoomService;

    @Override
    public ChatMessage save(ChatMessage chatMessage) {
        chatMessage.setStatus(RECEIVED);
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    @Override
    public int countNewMessages(int senderId, int receiverId) {
        return chatMessageRepository.countBySenderIdAndReceiverIdAndStatus(
                senderId, receiverId, MessageStatus.RECEIVED);
    }

    @Override
    public List<ChatMessage> findChatMessages(int senderId, int receiverId) {
        var roomId = chatRoomService.getRoomId(senderId, receiverId, false);

        var messages = roomId.map(cId -> chatMessageRepository.findByRoomId(cId)).orElse(new ArrayList<>());

        if (messages.size() > 0) {
            updateStatuses(senderId, receiverId, MessageStatus.DELIVERED);
        }

        return messages;
    }

    @Override
    public void updateStatuses(int senderId, int receiverId, MessageStatus status) {
        chatMessageRepository.updateStatuses(senderId, receiverId, status);
    }

    @Override
    public ChatMessage findById(int id) {
        return chatMessageRepository
                .findById(id)
                .map(chatMessage -> {
                    chatMessage.setStatus(MessageStatus.DELIVERED);
                    return chatMessageRepository.save(chatMessage);
                })
                .orElseThrow(() ->
                        new ObjectNotFoundException("can't find message (" + id + ")"));
    }

}
