package ru.yandex.practicum.sleeptracker;

import java.time.LocalTime;
import java.util.List;
import java.util.function.Function;

public class SleeplessNightsCount implements Function<List<SleepingSession>, SleepAnalysisResult<?>> {


    @Override
    public SleepAnalysisResult<Integer> apply(List<SleepingSession> sleepingSessions) {

        int countOfSleeplessNights = (int) sleepingSessions.stream()
                .filter(sleepingSession -> sleepingSession.getSleepStartDate().isBefore(sleepingSession.getWakeUpDate()) && sleepingSession.getWakeUpDate().getDayOfMonth() != 1)
                .filter(sleepingSession -> sleepingSession.getSleepStartTime().isAfter(LocalTime.of(0, 0)) && sleepingSession.getWakeUpTime().isBefore(LocalTime.of(6, 0)))
                .count();

        return new SleepAnalysisResult<>("Количество бессонных ночей: ", countOfSleeplessNights);
    }
}
