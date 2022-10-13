package com.drlionardo.authserver.conroller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @GetMapping("/ping")
    public ResponseEntity<Void> ping() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
