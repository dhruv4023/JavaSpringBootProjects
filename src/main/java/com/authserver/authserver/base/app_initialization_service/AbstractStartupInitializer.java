package com.authserver.authserver.base.app_initialization_service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractStartupInitializer
        implements CommandLineRunner, Ordered {

    @Override
    @Transactional
    public void run(String... args) {
        initialize();
    }

    protected abstract void initialize();

    @Override
    public int getOrder() {
        return 0;
    }
}