package com.example.greenproject.controller;

import com.example.greenproject.entity.EnvironmentalEvent;
import com.example.greenproject.repository.EnvironmentalEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/admin/events")
public class AdminEventController {
    
    @Autowired
    private EnvironmentalEventRepository eventRepository;
    
    @GetMapping
    public List<EnvironmentalEvent> getAllEvents() {
        return eventRepository.findAll();
    }
    
    @PostMapping
    public EnvironmentalEvent addEvent(@RequestBody EnvironmentalEvent event) {
        return eventRepository.save(event);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<EnvironmentalEvent> updateEvent(@PathVariable Integer id, @RequestBody EnvironmentalEvent eventData) {
        return eventRepository.findById(id)
            .map(event -> {
                event.setEvent_name(eventData.getEvent_name());
                event.setLocation(eventData.getLocation()); // 新增
                event.setDescription(eventData.getDescription()); // 新增
                event.setStart_date(eventData.getStart_date());
                event.setEnd_date(eventData.getEnd_date());
                EnvironmentalEvent updatedEvent = eventRepository.save(event);
                return ResponseEntity.ok(updatedEvent);
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Integer id) {
        return eventRepository.findById(id)
            .map(event -> {
                eventRepository.delete(event);
                return ResponseEntity.ok().build();
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}