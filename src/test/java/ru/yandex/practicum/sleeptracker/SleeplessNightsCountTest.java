package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SleeplessNightsCountTest {

    private SleeplessNightsCount counter;

    @BeforeEach
    void setUp() {
        counter = new SleeplessNightsCount();
    }

    @Test
    void shouldCountSleeplessNightWhenAllConditionsAreMet() {
        // Условие 1: Дата начала ДО даты конца (10-е и 11-е число, день конца НЕ 1-е)
        // Условие 2: Время старта ПОСЛЕ 00:00, время конца ДО 06:00
        SleepingSession sleeplessNight = new SleepingSession(
                LocalDateTime.of(2026, 6, 10, 1, 0),  // 01:00 ночи
                LocalDateTime.of(2026, 6, 11, 5, 0),  // 05:00 утра
                QualityOfSleep.BAD
        );

        List<SleepingSession> sessions = List.of(sleeplessNight);
        SleepAnalysisResult<?> result = counter.apply(sessions);

        // Сессия полностью удовлетворяет обоим фильтрам, ожидаем 1
        assertEquals(1, result.getResult());
    }

    @Test
    void shouldIgnoreSleeplessNightWhenWakeUpDayIsFirstOfMonth() {
        // Первое условие нарушено: дата конца — 1-е число месяца (getWakeUpDate().getDayOfMonth() != 1 вернет false)
        SleepingSession firstDayOfMonthSession = new SleepingSession(
                LocalDateTime.of(2026, 5, 31, 1, 0),
                LocalDateTime.of(2026, 6, 1, 5, 0), // Проснулся 1-го числа
                QualityOfSleep.BAD
        );

        List<SleepingSession> sessions = List.of(firstDayOfMonthSession);
        SleepAnalysisResult<?> result = counter.apply(sessions);

        // Должно отфильтроваться из-за 1-го числа, ожидаем 0
        assertEquals(0, result.getResult());
    }

    @Test
    void shouldIgnoreNightWhenTimeIsOutsideInterval() {
        // Первое условие выполнено (10 и 11 число), но второе нарушено:
        // время старта НЕ после 00:00 (ровно в полночь) или время конца НЕ до 06:00 (в 6 утра)
        SleepingSession earlyStartSession = new SleepingSession(
                LocalDateTime.of(2026, 6, 10, 0, 0), // Ровно 00:00
                LocalDateTime.of(2026, 6, 11, 6, 0), // Ровно 06:00
                QualityOfSleep.NORMAL
        );

        List<SleepingSession> sessions = List.of(earlyStartSession);
        SleepAnalysisResult<?> result = counter.apply(sessions);

        // Должно отфильтроваться по времени, ожидаем 0
        assertEquals(0, result.getResult());
    }

    @Test
    void shouldReturnZeroForEmptyList() {
        // Проверка пограничного случая с пустой базой данных
        List<SleepingSession> emptySessions = Collections.emptyList();

        SleepAnalysisResult<?> result = counter.apply(emptySessions);

        // Для пустого стрима count() возвращает 0, ожидаем 0
        assertEquals(0, result.getResult());
    }
}
