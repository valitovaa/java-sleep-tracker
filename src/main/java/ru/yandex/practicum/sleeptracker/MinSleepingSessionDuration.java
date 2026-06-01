package ru.yandex.practicum.sleeptracker;

import java.util.List;
import java.util.function.Function;

public class MinSleepingSessionDuration implements Function<List<SleepingSession>, SleepAnalysisResult<?>> {

    @Override
    public SleepAnalysisResult<Integer> apply(List<SleepingSession> sleepingSessions) {

        return sleepingSessions.stream()
                .map(SleepingSession::getSleepDuration)
                .map(d -> (int) d.toMinutes())
                .min(Integer::compareTo)
                .map(min -> new SleepAnalysisResult<>("Минимальная продолжительность сессии (в минутах)", min))
                .orElse(new SleepAnalysisResult<>("Минимальная продолжительность сессии", -1));

    }
}
