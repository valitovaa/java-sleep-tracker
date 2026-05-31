package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SleepTrackerAppTest {

    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    private final Path projectResourcesDir = Path.of("src", "main", "resources");
    private final Path logFile = projectResourcesDir.resolve("sleep_log.txt");
    private boolean backupExists = false;
    private final Path backupFile = projectResourcesDir.resolve("sleep_log.txt.bak");

    @BeforeEach
    void setUp() throws IOException {
        // Перехватываем консольный вывод
        System.setOut(new PrintStream(outputStreamCaptor));

        // Создаем директорию ресурсов, если её нет
        Files.createDirectories(projectResourcesDir);

        // Если оригинальный файл существует, делаем бэкап, чтобы не стереть ваши данные
        if (Files.exists(logFile)) {
            Files.move(logFile, backupFile);
            backupExists = true;
        }

        // Записываем предсказуемые тестовые данные
        List<String> mockLogs = List.of(
                "01.06.26 23:30;02.06.26 09:30;GOOD", // Сова, 10 часов (600 мин)
                "02.06.26 21:00;03.06.26 06:00;BAD"   // Жаворонок, 9 часов (540 мин)
        );
        Files.write(logFile, mockLogs);
    }

    @AfterEach
    void tearDown() throws IOException {
        // Возвращаем стандартный вывод
        System.setOut(originalOut);

        // Удаляем тестовый файл
        Files.deleteIfExists(logFile);

        // Восстанавливаем бэкап оригинального файла, если он был
        if (backupExists) {
            Files.move(backupFile, logFile);
        }
    }

    @Test
    void shouldRunFullPipelineAndPrintResults() {
        // Проверяем, что метод main выполняется от начала до конца без исключений
        assertDoesNotThrow(() -> SleepTrackerApp.main(new String[]{}));

        String output = outputStreamCaptor.toString();

        // Проверяем наличие приветствия и корректность шаблона вывода
        assertTrue(output.contains("Welcome to the sleep tracker"));
        assertTrue(output.contains("Средняя продолжительность сессии в минутах"));
        assertTrue(output.contains("Общее количество сессий сна:"));
        assertTrue(output.contains("Вы относитесь к хронотипу:"));
    }
}
