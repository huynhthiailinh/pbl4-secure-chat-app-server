package com.dut.sweetchatapp.service.impl;

import com.dut.sweetchatapp.model.ConversationReply;
import com.dut.sweetchatapp.repository.ConversationReplyRepository;
import com.dut.sweetchatapp.service.ConversationReplyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ConversationReplyServiceImpl implements ConversationReplyService {

    private final ConversationReplyRepository conversationReplyRepository;

    @Override
    public ConversationReply addConversationReply(ConversationReply conversationReply) {
        return conversationReplyRepository.save(conversationReply);
    }
}
