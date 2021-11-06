package com.dut.sweetchatapp.service.impl;

import com.dut.sweetchatapp.enums.MessageStatus;
import com.dut.sweetchatapp.handleException.exception.ObjectNotFoundException;
import com.dut.sweetchatapp.model.ChatMessage;
import com.dut.sweetchatapp.repository.ChatMessageRepository;
import com.dut.sweetchatapp.rsa.Decryption;
import com.dut.sweetchatapp.service.AccountService;
import com.dut.sweetchatapp.service.ChatMessageService;
import com.dut.sweetchatapp.service.ChatRoomService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.dut.sweetchatapp.enums.MessageStatus.RECEIVED;

@AllArgsConstructor
@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    private final ChatRoomService chatRoomService;

    private final Decryption decryption;

    private final AccountService accountService;

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
    public List<ChatMessage> findChatMessages(int senderId, int receiverId) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        Optional<String> roomId = chatRoomService.getRoomId(senderId, receiverId, false);

        List<ChatMessage> messages = roomId.map(cId -> chatMessageRepository.findByRoomId(cId)).orElse(new ArrayList<>());

        List<ChatMessage> temp = new ArrayList<>();

        for (ChatMessage i : messages) {
            String content = decryption.decrypt(
                    i.getContent(),
                    accountService.getPrivateKeyByAccountId(i.getReceiverId()));

            ChatMessage messageTmp = ChatMessage
                    .builder()
                    .id(i.getId())
                    .roomId(i.getRoomId())
                    .senderId(i.getSenderId())
                    .senderName(i.getSenderName())
                    .receiverId(i.getReceiverId())
                    .receiverName(i.getReceiverName())
                    .content(content)
                    .createdDate(i.getCreatedDate())
                    .updatedDate(i.getUpdatedDate())
                    .isDeleted(i.isDeleted())
                    .status(i.getStatus())
                    .build();

            temp.add(messageTmp);
        }

        if (messages.size() > 0) {
            updateStatuses(senderId, receiverId, MessageStatus.DELIVERED);
        }

        return temp;
    }

    @Override
    public void updateStatuses(int senderId, int receiverId, MessageStatus status) {
        chatMessageRepository.updateStatuses(senderId, receiverId, status);
    }

    @Override
    public ChatMessage findById(int id) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        ChatMessage chatMessage = chatMessageRepository.findById(id).get();

        String content = decryption.decrypt(
                chatMessage.getContent(),
                accountService.getPrivateKeyByAccountId(chatMessage.getReceiverId()));

        chatMessage.setStatus(MessageStatus.DELIVERED);
        chatMessageRepository.save(chatMessage);
        chatMessage.setContent(content);

        return chatMessage;
    }
}
