package com.example.article.mock;

import com.example.common.service.port.ClockHolder;

import java.time.LocalDateTime;

public class FakeClockHolder implements ClockHolder {

    private LocalDateTime now;

    public FakeClockHolder(LocalDateTime now) {
        this.now = now;
    }

    @Override
    public LocalDateTime current() {
        return now;
    }
}
