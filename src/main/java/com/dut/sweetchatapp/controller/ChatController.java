package com.dut.sweetchatapp.controller;

import com.dut.sweetchatapp.model.ChatMessage;
import com.dut.sweetchatapp.model.ChatNotification;
import com.dut.sweetchatapp.rsa.Encryption;
import com.dut.sweetchatapp.service.AccountService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@AllArgsConstructor
@RestController
@CrossOrigin
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    private final ChatMessageService chatMessageService;

    private final ChatRoomService chatRoomService;

    private final Encryption encryption;

    private final AccountService accountService;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        var roomId = chatRoomService
                .getRoomId(chatMessage.getSenderId(), chatMessage.getReceiverId(), true);
        chatMessage.setRoomId(roomId.get());

        byte[] publicKey = accountService.getPublicKeyByAccountId(chatMessage.getReceiverId());
        chatMessage.setContent(encryption.encrypt(chatMessage.getContent(), publicKey));

        ChatMessage saved = chatMessageService.save(chatMessage);
        messagingTemplate.convertAndSendToUser(
                Integer.toString(chatMessage.getReceiverId()),"/queue/messages",
                new ChatNotification(
                        saved.getId(),
                        saved.getSenderId(),
                        saved.getSenderName()));
    }

    @MessageMapping("/file")
    public void processMessageFile(@Payload String file) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException, IOException {
        System.out.println(file);
//        ByteArrayInputStream bis = new ByteArrayInputStream(file);
//        BufferedImage bImage2 = ImageIO.read(bis);
//        ImageIO.write(bImage2, "jpg", new File("output.jpg") );
//        System.out.println("image created");
//        var roomId = chatRoomService
//                .getRoomId(chatMessage.getSenderId(), chatMessage.getReceiverId(), true);
//        chatMessage.setRoomId(roomId.get());
//
//        ChatMessage saved = chatMessageService.save(chatMessage);
//        messagingTemplate.convertAndSendToUser(
//                Integer.toString(chatMessage.getReceiverId()),"/queue/messages",
//                new ChatNotification(
//                        saved.getId(),
//                        saved.getSenderId(),
//                        saved.getSenderName()));
    }

    @GetMapping("/messages/{senderId}/{receiverId}/count")
    public ResponseEntity<Integer> countNewMessages(@PathVariable int senderId, @PathVariable int receiverId) {
        return ResponseEntity.ok(chatMessageService.countNewMessages(senderId, receiverId));
    }

    @GetMapping("/messages/{senderId}/{receiverId}")
    public ResponseEntity<?> findChatMessages(@PathVariable int senderId, @PathVariable int receiverId) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        return ResponseEntity
                .ok(chatMessageService.findChatMessages(senderId, receiverId));
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<?> findMessage(@PathVariable int id) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        return ResponseEntity
                .ok(chatMessageService.findById(id));
    }

}
