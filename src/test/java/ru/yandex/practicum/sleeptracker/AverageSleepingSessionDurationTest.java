package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;

class AverageSleepingSessionDurationTest {

    private AverageSleepingSessionDuration calculator;

    @BeforeEach
    void setUp() {
        calculator = new AverageSleepingSessionDuration();
    }

    @Test
    void shouldCalculateCorrectAverageDurationForValidSessions() {
        // 1-я сессия: с 22:00 до 06:00 следующего дня (8 часов = 480 минут)
        SleepingSession session1 = new SleepingSession(
                LocalDateTime.of(2026, 5, 1, 22, 0),
                LocalDateTime.of(2026, 5, 2, 6, 0),
                QualityOfSleep.GOOD
        );

        // 2-я сессия: с 23:00 до 09:00 следующего дня (10 часов = 600 минут)
        SleepingSession session2 = new SleepingSession(
                LocalDateTime.of(2026, 5, 2, 23, 0),
                LocalDateTime.of(2026, 5, 3, 9, 0),
                QualityOfSleep.GOOD
        );

        // Среднее значение: (480 + 600) / 2 = 540 минут (9 часов)
        List<SleepingSession> sessions = List.of(session1, session2);

        SleepAnalysisResult<?> result = calculator.apply(sessions);

        // Проверяем, что расчет равен ровно 540 минутам
        assertEquals(540, result.getResult());
    }

    @Test
    void shouldReturnMinusOneWhenSessionListIsEmpty() {
        // Передаем пустой список сессий
        List<SleepingSession> emptySessions = Collections.emptyList();

        SleepAnalysisResult<?> result = calculator.apply(emptySessions);

        // Проверяем выполнение защитного условия (ожидаем -1)
        assertEquals(-1, result.getResult());
    }
}
