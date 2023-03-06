package com.project.chatApplication.service;

import com.project.chatApplication.dao.StatusRepository;
import com.project.chatApplication.model.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatusService {
    @Autowired
    StatusRepository statusRepository;
    public int saveStatus(Status status) {
        Status statusObj = statusRepository.save(status);
        return statusObj.getStatusId();
    }
}
