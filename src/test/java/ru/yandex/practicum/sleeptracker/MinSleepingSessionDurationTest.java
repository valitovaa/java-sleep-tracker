package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MinSleepingSessionDurationTest {

    private MinSleepingSessionDuration calculator;
    private final LocalDateTime baseTime = LocalDateTime.of(2026, 6, 1, 22, 0);

    @BeforeEach
    void setUp() {
        calculator = new MinSleepingSessionDuration();
    }

    @Test
    void shouldFindMinimumSessionDurationInMinutes() {
        // Сессия 1: 9 часов = 540 минут
        SleepingSession longSession = new SleepingSession(baseTime, baseTime.plusHours(9), QualityOfSleep.GOOD);
        // Сессия 2: 5 часов = 300 минут (Минимальная)
        SleepingSession shortSession = new SleepingSession(baseTime.plusDays(1), baseTime.plusDays(1).plusHours(5), QualityOfSleep.BAD);
        // Сессия 3: 7 часов = 420 минут
        SleepingSession mediumSession = new SleepingSession(baseTime.plusDays(2), baseTime.plusDays(2).plusHours(7), QualityOfSleep.NORMAL);

        List<SleepingSession> sessions = List.of(longSession, shortSession, mediumSession);

        SleepAnalysisResult<?> result = calculator.apply(sessions);

        // Проверяем, что стрим успешно нашел минимум (300 минут)
        assertEquals(300, result.getResult());
    }

    @Test
    void shouldReturnMinusOneWhenSessionListIsEmpty() {
        // Передаем пустой список
        List<SleepingSession> emptySessions = Collections.emptyList();

        SleepAnalysisResult<?> result = calculator.apply(emptySessions);

        // Проверяем выполнение защитного условия (ожидаем -1)
        assertEquals(-1, result.getResult());
    }
}
