package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SleepingPersonalityClassificatorTest {

    private SleepingPersonalityClassificator classificator;

    // Базовая дата для тестов (15 июня 2026 года)
    private final LocalDateTime midMonthDate = LocalDateTime.of(2026, 6, 15, 0, 0);

    @BeforeEach
    void setUp() {
        classificator = new SleepingPersonalityClassificator();
    }

    @Test
    void shouldClassifyAsOwlWhenOwlsAreMajority() {
        // Настройка: 2 сессии совы и 1 сессия жаворонка. Побеждают совы.
        // Сова: отбой после 23:00, подъем после 09:00
        SleepingSession owl1 = new SleepingSession(
                midMonthDate.withHour(23).withMinute(30),
                midMonthDate.withHour(9).withMinute(30),
                QualityOfSleep.GOOD
        );
        SleepingSession owl2 = new SleepingSession(
                midMonthDate.withHour(23).withMinute(15),
                midMonthDate.withHour(10).withMinute(0),
                QualityOfSleep.NORMAL
        );
        // Жаворонок: отбой до 22:00, подъем до 07:00
        SleepingSession lark = new SleepingSession(
                midMonthDate.withHour(21).withMinute(0),
                midMonthDate.withHour(6).withMinute(30),
                QualityOfSleep.GOOD
        );

        List<SleepingSession> sessions = List.of(owl1, owl2, lark);
        SleepAnalysisResult<?> result = classificator.apply(sessions);

        assertEquals(SleepingPersonality.OWL, result.getResult());
    }

    @Test
    void shouldClassifyAsLarkWhenLarksAreMajority() {
        // Настройка: 2 сессии жаворонка и 1 сессия голубя. Побеждают жаворонки.
        SleepingSession lark1 = new SleepingSession(
                midMonthDate.withHour(21).withMinute(30),
                midMonthDate.withHour(6).withMinute(0),
                QualityOfSleep.GOOD
        );
        SleepingSession lark2 = new SleepingSession(
                midMonthDate.withHour(20).withMinute(45),
                midMonthDate.withHour(5).withMinute(45),
                QualityOfSleep.NORMAL
        );
        // Голубь (не сова и не жаворонок)
        SleepingSession pigeon = new SleepingSession(
                midMonthDate.withHour(22).withMinute(30),
                midMonthDate.withHour(8).withMinute(0),
                QualityOfSleep.GOOD
        );

        List<SleepingSession> sessions = List.of(lark1, lark2, pigeon);
        SleepAnalysisResult<?> result = classificator.apply(sessions);

        assertEquals(SleepingPersonality.LARK, result.getResult());
    }

    @Test
    void shouldClassifyAsPigeonWhenOwlAndLarkCountsAreEqual() {
        // Настройка: 1 сова и 1 жаворонок. При равенстве голосов побеждает голубь.
        SleepingSession owl = new SleepingSession(
                midMonthDate.withHour(23).withMinute(45),
                midMonthDate.withHour(9).withMinute(15),
                QualityOfSleep.GOOD
        );
        SleepingSession lark = new SleepingSession(
                midMonthDate.withHour(21).withMinute(15),
                midMonthDate.withHour(6).withMinute(15),
                QualityOfSleep.GOOD
        );

        List<SleepingSession> sessions = List.of(owl, lark);
        SleepAnalysisResult<?> result = classificator.apply(sessions);

        assertEquals(SleepingPersonality.PIGEON, result.getResult());
    }

    @Test
    void shouldFilterOutInvalidSessionsBeforeClassification() {
        // Настройка: добавляем одну сессию совы, которая должна быть отфильтрована по дате,
        // и одну валидную сессию жаворонка. В итоге должен победить жаворонок.

        // Эта сова отфильтруется, так как дата старта (14-е) меньше даты конца (15-е) и день конца НЕ 1-е число.
        SleepingSession invalidDateOwl = new SleepingSession(
                LocalDateTime.of(2026, 6, 14, 23, 30),
                LocalDateTime.of(2026, 6, 15, 9, 30),
                QualityOfSleep.NORMAL
        );

        // Валидный жаворонок (все события внутри одних суток)
        SleepingSession validLark = new SleepingSession(
                midMonthDate.withHour(21).withMinute(0),
                midMonthDate.withHour(6).withMinute(0),
                QualityOfSleep.GOOD
        );

        List<SleepingSession> sessions = List.of(invalidDateOwl, validLark);
        SleepAnalysisResult<?> result = classificator.apply(sessions);

        // Без фильтрации была бы ничья (голубь), но с фильтрацией сова исчезает, оставляя жаворонка
        assertEquals(SleepingPersonality.LARK, result.getResult());
    }
}
