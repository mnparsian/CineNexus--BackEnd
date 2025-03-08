package com.cinenexus.backend.repository;

import com.cinenexus.backend.model.chat.ChatRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRequestRepository extends JpaRepository<ChatRequest,Long> {}
