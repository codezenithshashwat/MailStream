package com.example.mailstream.repository;

import com.example.mailstream.model.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubscriberRepository extends JpaRepository<Subscriber, UUID> {
}
