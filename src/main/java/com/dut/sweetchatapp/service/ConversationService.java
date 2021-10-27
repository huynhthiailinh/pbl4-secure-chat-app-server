package com.dut.sweetchatapp.service;

import com.dut.sweetchatapp.model.Conversation;

public interface ConversationService {

    Conversation addConversation(Conversation conversation);

    Boolean existsByUserOneIdAndUserTwoId(int userOneId, int userTwoId);

    Conversation findByUserOneIdAndUserTwoId(int userOneId, int userTwoId);

}
