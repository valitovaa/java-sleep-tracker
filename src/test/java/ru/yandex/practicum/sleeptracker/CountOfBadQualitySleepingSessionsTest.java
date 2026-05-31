package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CountOfBadQualitySleepingSessionsTest {

    private CountOfBadQualitySleepingSessions counter;
    private final LocalDateTime baseTime = LocalDateTime.of(2026, 6, 1, 23, 0);

    @BeforeEach
    void setUp() {
        counter = new CountOfBadQualitySleepingSessions();
    }

    @Test
    void shouldCountOnlySessionsWithBadQuality() {
        // Создаем тестовые сессии с разным качеством сна
        // Предполагается, что в вашем QualityOfSleep есть значения BAD и GOOD (или аналогичные)
        SleepingSession badSession1 = new SleepingSession(baseTime, baseTime.plusHours(8), QualityOfSleep.BAD);
        SleepingSession badSession2 = new SleepingSession(baseTime.plusDays(1), baseTime.plusDays(1).plusHours(7), QualityOfSleep.BAD);
        SleepingSession goodSession = new SleepingSession(baseTime.plusDays(2), baseTime.plusDays(2).plusHours(9), QualityOfSleep.GOOD);

        List<SleepingSession> sessions = List.of(badSession1, badSession2, goodSession);

        SleepAnalysisResult<?> result = counter.apply(sessions);

        // Из 3 сессий только 2 имеют статус BAD, ожидаем результат 2
        assertEquals(2, result.getResult());
    }

    @Test
    void shouldReturnMinusOneWhenSessionListIsEmpty() {
        // Передаем пустой список сессий
        List<SleepingSession> emptySessions = Collections.emptyList();

        SleepAnalysisResult<?> result = counter.apply(emptySessions);

        // Проверяем выполнение защитного условия из начала вашего метода (ожидаем -1)
        assertEquals(-1, result.getResult());
    }
}
