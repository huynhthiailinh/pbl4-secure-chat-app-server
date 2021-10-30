package com.dut.sweetchatapp.service;

import java.util.Optional;

public interface ChatRoomService {

    Optional<String> getRoomId (int senderId, int receiverId, boolean createIfNotExist);

}
