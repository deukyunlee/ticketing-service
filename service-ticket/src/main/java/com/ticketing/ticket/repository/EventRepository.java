package com.ticketing.ticket.repository;

import com.ticketing.ticket.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {

}
