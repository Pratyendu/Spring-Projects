package com.project.chatApplication.dao;

import com.project.chatApplication.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status , Integer> {
}
