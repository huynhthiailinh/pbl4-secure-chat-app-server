package com.dut.sweetchatapp.repository;

import com.dut.sweetchatapp.model.ChatRoom;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends CrudRepository<ChatRoom, Integer> {

    Optional<ChatRoom> findBySenderIdAndReceiverId(int senderId, int receiverId);

    Boolean existsBySenderIdAndReceiverId(int senderId, int receiverId);

}
