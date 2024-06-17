package com.example.common.adaptor;

import com.example.common.service.port.ClockHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SystemClockHolder implements ClockHolder {

    @Override
    public LocalDateTime current() {
        return LocalDateTime.now();
    }
}
