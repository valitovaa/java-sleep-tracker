package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SleepLogReaderTest {

    private SleepLogReader reader;

    // JUnit 5 автоматически создаст и удалит эту временную папку для тестов
    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        reader = new SleepLogReader();
    }

    @Test
    void shouldSuccessfullyParseValidSleepLog() throws Exception {
        // Настройка: создаем файл с корректными строками
        Path filePath = tempDir.resolve("valid_sleep_log.txt");
        List<String> lines = List.of(
                "01.06.26 22:00;02.06.26 06:00;GOOD",
                "  ", // Пустая строка для проверки filter()
                "02.06.26 23:30;03.06.26 07:45;NORMAL"
        );
        Files.write(filePath, lines);

        // Действие
        List<SleepingSession> result = reader.readSleepLog(filePath.toString());

        // Проверка: пустая строка проигнорирована, распарсились ровно 2 сессии
        assertEquals(2, result.size());

        // Проверяем точность парсинга первой сессии
        SleepingSession firstSession = result.get(0);
        assertEquals(LocalDateTime.of(2026, 6, 1, 22, 0), firstSession.getSleepStartDateTime());
        assertEquals(LocalDateTime.of(2026, 6, 2, 6, 0), firstSession.getWakeUpDateTime());
        assertEquals(QualityOfSleep.GOOD, firstSession.getSleepQuality());
    }

    @Test
    void shouldThrowExceptionWhenLineHasInvalidFormat() throws IOException {
        // Настройка: строка имеет 2 части вместо 3 (забыли качество сна)
        Path filePath = tempDir.resolve("invalid_format.txt");
        Files.writeString(filePath, "01.06.26 22:00;02.06.26 06:00");

        // Проверка: ожидаем IllegalArgumentException на неверный формат
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reader.readSleepLog(filePath.toString());
        });

        assertTrue(exception.getMessage().contains("Некорректный формат строки"));
    }

    @Test
    void shouldThrowExceptionWhenWakeUpIsBeforeSleepStart() throws IOException {
        // Настройка: время пробуждения (01:00) идет раньше начала сна (22:00) в рамках одних суток
        Path filePath = tempDir.resolve("invalid_chronology.txt");
        Files.writeString(filePath, "01.06.26 22:00;01.06.26 01:00;BAD");

        // Проверка: ожидаем IllegalArgumentException на нарушение хронологии
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reader.readSleepLog(filePath.toString());
        });

        assertTrue(exception.getMessage().contains("Время пробуждения не может быть раньше начала сна"));
    }

    @Test
    void shouldThrowExceptionWhenEnumConstantIsInvalid() throws IOException {
        // Настройка: передано несуществующее качество сна "SUPER_GOOD"
        Path filePath = tempDir.resolve("invalid_enum.txt");
        Files.writeString(filePath, "01.06.26 22:00;02.06.26 06:00;SUPER_GOOD");

        // Проверка: ловим внутреннюю ошибку парсинга данных
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reader.readSleepLog(filePath.toString());
        });

        assertTrue(exception.getMessage().contains("Ошибка парсинга данных в строке"));
    }
}
