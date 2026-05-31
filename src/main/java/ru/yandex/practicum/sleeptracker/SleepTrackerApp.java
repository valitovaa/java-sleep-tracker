package ru.yandex.practicum.sleeptracker;

import java.util.List;
import java.util.function.Function;

public class SleepTrackerApp {

    public static void main(String[] args) {
        System.out.println("Welcome to the sleep tracker \n");

        // Поддерживаем любые типы благодаря wildcard <?>
        List<Function<List<SleepingSession>, SleepAnalysisResult<?>>> analysisFunctions = List.of(
                new AverageSleepingSessionDuration(),
                new CountOfBadQualitySleepingSessions(),
                new CountOfSleepingSessions(),
                new MaxSleepingSessionDuration(),
                new MinSleepingSessionDuration(),
                new SleeplessNightsCount(),
                new SleepingPersonalityClassificator()
        );

        SleepLogReader sleepLogReader = new SleepLogReader();

        try {
            List<SleepingSession> sleepingSessionList = sleepLogReader.readSleepLog("src/main/resources/sleep_log.txt");

            // Вывод строго по вашему шаблону, но с заменой %d на %s для универсальности
            analysisFunctions.stream()
                    .map(function -> function.apply(sleepingSessionList))
                    .forEach(result -> System.out.printf("%s: %s%n", result.getDescription(), result.getResult()));

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при анализе лога сна", e);
        }
    }
}
