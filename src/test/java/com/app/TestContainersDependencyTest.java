package com.app;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

public class TestContainersDependencyTest {

    @Test
    void testPostgresContainerStarts() {
        try (PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")) {
            postgres.start();
            assertThat(postgres.isRunning()).isTrue();
            postgres.stop();
        }
    }
}
