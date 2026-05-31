package ru.yandex.practicum.sleeptracker;

import java.util.List;
import java.util.function.Function;

public class MaxSleepingSessionDuration implements Function<List<SleepingSession>, SleepAnalysisResult<?>> {
    @Override
    public SleepAnalysisResult<Integer> apply(List<SleepingSession> sleepingSessions) {
        if (sleepingSessions.isEmpty()) {
            return new SleepAnalysisResult<>("Максимальная продолжительность сессии", -1);
        }

        int maxDurationMinutes = sleepingSessions.stream()
                .map(SleepingSession::getSleepDuration)
                .mapToInt(d -> (int) d.toMinutes())
                .max()
                .orElse(-1);

        return new SleepAnalysisResult<>(
                "Максимальная продолжительность сессии (в минутах)",
                maxDurationMinutes
        );
    }
}
