package com.project.chatApplication.service;

import com.project.chatApplication.dao.ChatHistoryRepository;
import com.project.chatApplication.model.ChatHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatHistoryService {
    @Autowired
    ChatHistoryRepository chatHistoryRepository;
    public int saveMessage(ChatHistory chat) {
        chatHistoryRepository.save(chat);
        return chat.getChatId();
    }
}
