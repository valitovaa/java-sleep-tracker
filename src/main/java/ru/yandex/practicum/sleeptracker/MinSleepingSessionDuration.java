package ru.yandex.practicum.sleeptracker;

import java.util.List;
import java.util.function.Function;


public class MinSleepingSessionDuration implements Function<List<SleepingSession>, SleepAnalysisResult<?>> {
    @Override
    public SleepAnalysisResult<Integer> apply(List<SleepingSession> sleepingSessions) {
        if (sleepingSessions.isEmpty()) {
            return new SleepAnalysisResult<>("Минимальная продолжительность сессии", -1);
        }

        int minDurationMinutes = sleepingSessions.stream()
                .map(SleepingSession::getSleepDuration)
                .mapToInt(d -> (int) d.toMinutes())
                .min()
                .orElse(-1);

        return new SleepAnalysisResult<>(
                "Минимальная продолжительность сессии (в минутах)",
                minDurationMinutes
        );
    }

}
