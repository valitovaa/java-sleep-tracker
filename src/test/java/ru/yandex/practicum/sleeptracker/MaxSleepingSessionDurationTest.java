package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MaxSleepingSessionDurationTest {

    private MaxSleepingSessionDuration calculator;
    private final LocalDateTime baseTime = LocalDateTime.of(2026, 6, 1, 22, 0);

    @BeforeEach
    void setUp() {
        calculator = new MaxSleepingSessionDuration();
    }

    @Test
    void shouldFindMaximumSessionDurationInMinutes() {
        // Сессия 1: 6 часов = 360 минут
        SleepingSession shortSession = new SleepingSession(baseTime, baseTime.plusHours(6), QualityOfSleep.NORMAL);
        // Сессия 2: 10 часов = 600 минут (Максимальная)
        SleepingSession longSession = new SleepingSession(baseTime.plusDays(1), baseTime.plusDays(1).plusHours(10), QualityOfSleep.GOOD);
        // Сессия 3: 8 часов = 480 минут
        SleepingSession mediumSession = new SleepingSession(baseTime.plusDays(2), baseTime.plusDays(2).plusHours(8), QualityOfSleep.BAD);

        List<SleepingSession> sessions = List.of(shortSession, longSession, mediumSession);

        SleepAnalysisResult<?> result = calculator.apply(sessions);

        // Проверяем, что стрим успешно нашел максимум (600 минут)
        assertEquals(600, result.getResult());
    }

    @Test
    void shouldReturnMinusOneWhenSessionListIsEmpty() {
        // Передаем пустой список
        List<SleepingSession> emptySessions = Collections.emptyList();

        SleepAnalysisResult<?> result = calculator.apply(emptySessions);

        // Проверяем выполнение защитного условия из начала вашего метода (ожидаем -1)
        assertEquals(-1, result.getResult());
    }
}
