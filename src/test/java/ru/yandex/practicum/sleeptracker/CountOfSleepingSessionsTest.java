package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CountOfSleepingSessionsTest {

    private CountOfSleepingSessions counter;
    private final LocalDateTime baseTime = LocalDateTime.of(2026, 6, 1, 22, 0);

    @BeforeEach
    void setUp() {
        counter = new CountOfSleepingSessions();
    }

    @Test
    void shouldReturnCorrectCountOfSessions() {
        // Создаем список из 3 сессий сна
        SleepingSession session1 = new SleepingSession(baseTime, baseTime.plusHours(8), QualityOfSleep.GOOD);
        SleepingSession session2 = new SleepingSession(baseTime.plusDays(1), baseTime.plusDays(1).plusHours(7), QualityOfSleep.NORMAL);
        SleepingSession session3 = new SleepingSession(baseTime.plusDays(2), baseTime.plusDays(2).plusHours(6), QualityOfSleep.BAD);

        List<SleepingSession> sessions = List.of(session1, session2, session3);

        SleepAnalysisResult<?> result = counter.apply(sessions);

        // Проверяем, что метод вернул ровно 3
        assertEquals(3, result.getResult());
    }

    @Test
    void shouldReturnZeroWhenSessionListIsEmpty() {
        // Передаем пустой список сессий
        List<SleepingSession> emptySessions = Collections.emptyList();

        SleepAnalysisResult<?> result = counter.apply(emptySessions);

        // Согласно вашей логике size() для пустой коллекции вернет 0
        assertEquals(0, result.getResult());
    }
}
