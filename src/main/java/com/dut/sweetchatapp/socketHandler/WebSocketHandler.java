package com.dut.sweetchatapp.socketHandler;

import com.dut.sweetchatapp.enums.MessageStatus;
import com.dut.sweetchatapp.model.Conversation;
import com.dut.sweetchatapp.model.ConversationReply;
import com.dut.sweetchatapp.service.ConversationReplyService;
import com.dut.sweetchatapp.service.ConversationService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {

    private final ConversationReplyService conversationReplyService;

    private final ConversationService conversationService;

    private Set<WebSocketSession> webSocketSessions = new HashSet<>();

    public WebSocketHandler(ConversationReplyService conversationReplyService, ConversationService conversationService) {
        this.conversationReplyService = conversationReplyService;
        this.conversationService = conversationService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) {
        log.info("New WebSocket connection with id: ", webSocketSession.getId());
        webSocketSessions = Stream.concat(webSocketSessions.stream(), Stream.of(webSocketSession))
                .collect(Collectors.toSet());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) {
        webSocketSessions = webSocketSessions.stream()
                .filter(s -> !webSocketSession.equals(s))
                .collect(Collectors.toSet());
    }

    @Override
    public void handleTextMessage(WebSocketSession webSocketSession, TextMessage textMessage) {
        webSocketSessions.forEach((ses) -> {
            try {
                ses.sendMessage(textMessage);
            } catch (Exception exception) {
                log.error("Couldn't send WebSocket message to session with id: ", webSocketSession.getId());
            }
        });

        Gson gson = new Gson();
        JsonObject message = gson.fromJson(textMessage.getPayload(), JsonObject.class);
        int senderId = message.get("senderId").getAsInt();
        int receiverId = message.get("receiverId").getAsInt();
        String content = message.get("text").getAsString();
        Conversation newConversation;

        if (!conversationService.existsByUserOneIdAndUserTwoId(senderId, receiverId) &&
                !conversationService.existsByUserOneIdAndUserTwoId(receiverId, senderId)) {
            Conversation conversation = new Conversation();
            conversation.setUserOneId(senderId);
            conversation.setUserTwoId(receiverId);
            newConversation = conversationService.addConversation(conversation);
        } else {
            if (Objects.isNull(conversationService.findByUserOneIdAndUserTwoId(senderId, receiverId))) {
                newConversation = conversationService.findByUserOneIdAndUserTwoId(receiverId, senderId);
            } else {
                newConversation = conversationService.findByUserOneIdAndUserTwoId(senderId, receiverId);
            }
        }

        ConversationReply conversationReply = new ConversationReply();
        conversationReply.setContent(content);
        conversationReply.setSenderId(senderId);
        conversationReply.setStatus(MessageStatus.SENDING);
        conversationReply.setConversationId(newConversation.getId());
        conversationReplyService.addConversationReply(conversationReply);
    }

}
