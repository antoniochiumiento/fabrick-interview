package com.fabrick.interview.controller;

import com.fabrick.interview.service.AsteroidService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AsteroidControllerTest {

    @Mock
    private AsteroidService asteroidService;

    @InjectMocks
    private AsteroidController asteroidController;

    @Test
    @DisplayName("Should use default dates when parameters are null")
    void shouldUseDefaultDates() {
        when(asteroidService.getAsteroidPath(any(), any(), any())).thenReturn(Flux.empty());

        asteroidController.getAsteroidPaths("123", null, null);

        ArgumentCaptor<LocalDate> fromDateCaptor = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<LocalDate> toDateCaptor = ArgumentCaptor.forClass(LocalDate.class);

        verify(asteroidService).getAsteroidPath(eq("123"), fromDateCaptor.capture(), toDateCaptor.capture());

        assertEquals(LocalDate.now().minusYears(100), fromDateCaptor.getValue());

        assertEquals(LocalDate.now(), toDateCaptor.getValue());
    }

    @Test
    @DisplayName("Should pass provided dates correctly")
    void shouldPassProvidedDates() {
        when(asteroidService.getAsteroidPath(any(), any(), any())).thenReturn(Flux.empty());

        LocalDate myFrom = LocalDate.of(1990, 1, 1);
        LocalDate myTo = LocalDate.of(1995, 1, 1);
        asteroidController.getAsteroidPaths("123", myFrom, myTo);

        verify(asteroidService).getAsteroidPath("123", myFrom, myTo);
    }
}