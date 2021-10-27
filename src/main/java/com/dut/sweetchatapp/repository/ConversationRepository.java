package com.dut.sweetchatapp.repository;

import com.dut.sweetchatapp.model.Conversation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationRepository extends CrudRepository<Conversation, Integer> {

    Boolean existsByUserOneIdAndUserTwoId(int userOneId, int userTwoId);

    Conversation findByUserOneIdAndUserTwoId(int userOneId, int userTwoId);

}
