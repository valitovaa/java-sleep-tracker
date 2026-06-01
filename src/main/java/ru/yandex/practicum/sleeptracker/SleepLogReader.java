package ru.yandex.practicum.sleeptracker;


import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.List;
import java.util.stream.Collectors;

public class SleepLogReader {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");


    public List<SleepingSession> readSleepLog(String filePath) throws Exception {

        return Files.readAllLines(Path.of(filePath))
                .stream()
                .filter(line -> !line.trim().isEmpty())
                .map(this::parseSleepSession)
                .collect(Collectors.toList());
    }


    private SleepingSession parseSleepSession(String line) {
        String[] parts = line.trim().split(";");


        if (parts.length != 3) {
            throw new IllegalArgumentException(
                    "Некорректный формат строки: " + line +
                            ". Ожидаются 3 значения: дата и время начала сна, дата и время пробуждения, качество сна.");
        }

        try {

            String startDateTimeStr = parts[0];
            LocalDateTime sleepStart = LocalDateTime.parse(startDateTimeStr, DATE_TIME_FORMATTER);

            String endDateTimeStr = parts[1];
            LocalDateTime wakeUp = LocalDateTime.parse(endDateTimeStr, DATE_TIME_FORMATTER);

            QualityOfSleep quality = QualityOfSleep.valueOf(parts[2].toUpperCase());

            if (!wakeUp.isAfter(sleepStart)) {
                throw new IllegalArgumentException(
                        "Время пробуждения не может быть раньше начала сна: " + line);
            }

            return new SleepingSession(sleepStart, wakeUp, quality);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Ошибка парсинга данных в строке: " + line, e);
        }
    }
}
