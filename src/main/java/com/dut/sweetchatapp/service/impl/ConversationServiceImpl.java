package com.dut.sweetchatapp.service.impl;

import com.dut.sweetchatapp.model.Conversation;
import com.dut.sweetchatapp.repository.ConversationRepository;
import com.dut.sweetchatapp.service.ConversationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;

    @Override
    public Conversation addConversation(Conversation conversation) {
        return conversationRepository.save(conversation);
    }

    @Override
    public Boolean existsByUserOneIdAndUserTwoId(int userOneId, int userTwoId) {
        return conversationRepository.existsByUserOneIdAndUserTwoId(userOneId, userTwoId);
    }

    @Override
    public Conversation findByUserOneIdAndUserTwoId(int userOneId, int userTwoId) {
        return conversationRepository.findByUserOneIdAndUserTwoId(userOneId, userTwoId);
    }
}
