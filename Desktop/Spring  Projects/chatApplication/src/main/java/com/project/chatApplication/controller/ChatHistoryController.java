package com.project.chatApplication.controller;

import com.project.chatApplication.dao.StatusRepository;
import com.project.chatApplication.dao.UserRepository;
import com.project.chatApplication.model.ChatHistory;
import com.project.chatApplication.model.Users;
import com.project.chatApplication.service.ChatHistoryService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@RestController
@RequestMapping(value = "/api/v1/chat")
public class ChatHistoryController {

    @Autowired
    ChatHistoryService chatHistoryService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    StatusRepository statusRepository;

    @PostMapping(value = "/send-message")
    public ResponseEntity<String> sendMessage(@RequestBody String requestData){
        JSONObject requestObj = new JSONObject(requestData);
        JSONObject errorList = validateRequest(requestObj);

        if(errorList.isEmpty()){
            ChatHistory chat = saveChatHistory(requestObj);
            int chatID = chatHistoryService.saveMessage(chat);
            return new ResponseEntity<>("Message sent with chatId: " + chatID , HttpStatus.CREATED);
        }else {
            return new ResponseEntity<String>(errorList.toString() , HttpStatus.BAD_REQUEST);
        }

    }

    private ChatHistory saveChatHistory(JSONObject requestObj) {
        ChatHistory chat = new ChatHistory();

        String message = requestObj.getString("message");
        int senderId = requestObj.getInt("sender");
        int receiverId = requestObj.getInt("receiver");
        Users senderUserObj = userRepository.findById(senderId).get();
        Users receiverUserObj = userRepository.findById(receiverId).get();

        chat.setReceiver(receiverUserObj);
        chat.setSender(senderUserObj);
        chat.setStatusId(statusRepository.findById(1).get());
        chat.setMessage(message);

        Timestamp createdDate = new Timestamp(System.currentTimeMillis());
        chat.setCreatedDate(createdDate);
        return chat;
    }

    private JSONObject validateRequest(JSONObject requestObj){
        JSONObject errorObj = new JSONObject();

        if(!requestObj.has("sender")){
            errorObj.put("sender" , "Missing Parameter");
        }

        if(!requestObj.has("receiver")){
            errorObj.put("receiver" , "Missing Parameter");
        }

        if(requestObj.has("message")){
            String message = requestObj.getString("message");
            if(message.isBlank() || message.isEmpty()){
                errorObj.put("message" , "Missing Parameter");
            }
        }else {
            errorObj.put("message" , "Missing Parameter");
        }

        return errorObj;
    }
}
