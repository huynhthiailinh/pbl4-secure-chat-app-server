package com.dut.sweetchatapp.service.impl;

import com.dut.sweetchatapp.model.ChatRoom;
import com.dut.sweetchatapp.repository.ChatRoomRepository;
import com.dut.sweetchatapp.service.ChatRoomService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    @Override
    public Optional<String> getRoomId(int senderId, int receiverId, boolean createIfNotExist) {
        return chatRoomRepository
                .findBySenderIdAndReceiverId(senderId, receiverId)
                .map(ChatRoom::getRoomId)
                .or(() -> {
                    if(!createIfNotExist) {
                        return  Optional.empty();
                    }
                    var roomId =
                            String.format("%s_%s", senderId, receiverId);

                    ChatRoom senderRecipient = ChatRoom
                            .builder()
                            .roomId(roomId)
                            .senderId(senderId)
                            .receiverId(receiverId)
                            .build();

                    ChatRoom recipientSender = ChatRoom
                            .builder()
                            .roomId(roomId)
                            .senderId(receiverId)
                            .receiverId(senderId)
                            .build();
                    chatRoomRepository.save(senderRecipient);
                    chatRoomRepository.save(recipientSender);

                    return Optional.of(roomId);
                });
    }
}
