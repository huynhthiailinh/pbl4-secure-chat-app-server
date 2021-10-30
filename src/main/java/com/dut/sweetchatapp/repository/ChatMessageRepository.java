package com.dut.sweetchatapp.repository;

import com.dut.sweetchatapp.enums.MessageStatus;
import com.dut.sweetchatapp.model.ChatMessage;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface ChatMessageRepository extends CrudRepository<ChatMessage, Integer> {

    int countBySenderIdAndReceiverIdAndStatus (int senderId, int receiverId, MessageStatus status);

    List<ChatMessage> findByRoomId(String roomId);

    @Modifying
    @Query("UPDATE ChatMessage c SET c.status = :status WHERE c.senderId = :senderId AND c.receiverId = :receiverId")
    void updateStatuses(@Param("senderId") int senderId, @Param("receiverId") int receiverId, @Param("status") MessageStatus status);

}
