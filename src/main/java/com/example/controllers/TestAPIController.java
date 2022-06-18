package com.example.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Security Test Ground",
        description = "Resource to manage Security Testing")
@RestController
@RequestMapping("/api/test")
class TestAPIController {
    /**
     * all.
     * @return content
     */
    @GetMapping("/all")
    public ResponseEntity<String> allAccess() {
        return ResponseEntity.ok("Public Content.");
    }
    /**
     * user.
     * @return content
     */
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<String> userAccess() {
        return ResponseEntity.ok("User Content.");
    }
    /**
     * mod.
     * @return board
     */
    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<String> moderatorAccess() {
        return ResponseEntity.ok("Moderator Board.");
    }
    /**
     * admin.
     * @return admin board
     */
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminAccess() {
        return ResponseEntity.ok("Admin Board.");
    }
}
