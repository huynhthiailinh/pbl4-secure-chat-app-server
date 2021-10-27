package com.dut.sweetchatapp.repository;

import com.dut.sweetchatapp.model.ConversationReply;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationReplyRepository extends CrudRepository<ConversationReply, Integer> {
}
