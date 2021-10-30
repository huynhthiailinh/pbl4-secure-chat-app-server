package com.dut.sweetchatapp.controller;

import com.dut.sweetchatapp.model.ChatMessage;
import com.dut.sweetchatapp.model.ChatNotification;
import com.dut.sweetchatapp.service.ChatMessageService;
import com.dut.sweetchatapp.service.ChatRoomService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@CrossOrigin
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    private final ChatMessageService chatMessageService;

    private final ChatRoomService chatRoomService;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        var roomId = chatRoomService
                .getRoomId(chatMessage.getSenderId(), chatMessage.getReceiverId(), true);
        chatMessage.setRoomId(roomId.get());

        ChatMessage saved = chatMessageService.save(chatMessage);
        messagingTemplate.convertAndSendToUser(
                Integer.toString(chatMessage.getReceiverId()),"/queue/messages",
                new ChatNotification(
                        saved.getId(),
                        saved.getSenderId(),
                        saved.getSenderName()));
    }

    @GetMapping("/messages/{senderId}/{receiverId}/count")
    public ResponseEntity<Integer> countNewMessages(@PathVariable int senderId, @PathVariable int receiverId) {
        return ResponseEntity.ok(chatMessageService.countNewMessages(senderId, receiverId));
    }

    @GetMapping("/messages/{senderId}/{receiverId}")
    public ResponseEntity<?> findChatMessages(@PathVariable int senderId, @PathVariable int receiverId) {
        return ResponseEntity
                .ok(chatMessageService.findChatMessages(senderId, receiverId));
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<?> findMessage(@PathVariable int id) {
        return ResponseEntity
                .ok(chatMessageService.findById(id));
    }

}
