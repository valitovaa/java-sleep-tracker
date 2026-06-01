package ru.yandex.practicum.sleeptracker;

import java.util.List;
import java.util.function.Function;

public class MaxSleepingSessionDuration implements Function<List<SleepingSession>, SleepAnalysisResult<?>> {
    @Override
    public SleepAnalysisResult<Integer> apply(List<SleepingSession> sleepingSessions) {

        return sleepingSessions.stream()
                .map(SleepingSession::getSleepDuration)
                .map(d -> (int) d.toMinutes())
                .max(Integer::compareTo)
                .map(max -> new SleepAnalysisResult<>("Максимальная продолжительность сессии (в минутах)", max))
                .orElse(new SleepAnalysisResult<>("Максимальная продолжительность сессии", -1));

    }
}
