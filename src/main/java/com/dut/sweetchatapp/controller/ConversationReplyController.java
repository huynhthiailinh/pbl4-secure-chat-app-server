package com.dut.sweetchatapp.controller;

import com.dut.sweetchatapp.model.ConversationReply;
import com.dut.sweetchatapp.service.ConversationReplyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.dut.sweetchatapp.constant.DefaultPath.CONVERSATION_REPLY_PATH;

@AllArgsConstructor
@RestController
@CrossOrigin
@RequestMapping(CONVERSATION_REPLY_PATH)
public class ConversationReplyController {

    private final ConversationReplyService conversationReplyService;

    @PostMapping
    public ResponseEntity addConversationReply(@RequestBody ConversationReply conversationReply) {
        return new ResponseEntity<>(conversationReplyService.addConversationReply(conversationReply), HttpStatus.OK);
    }

}
