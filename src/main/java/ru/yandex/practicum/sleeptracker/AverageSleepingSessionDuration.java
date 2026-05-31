package ru.yandex.practicum.sleeptracker;

import java.util.List;
import java.util.function.Function;

public class AverageSleepingSessionDuration implements Function<List<SleepingSession>, SleepAnalysisResult<?>> {
    @Override
    public SleepAnalysisResult<Integer> apply(List<SleepingSession> sleepingSessions) {
        if (sleepingSessions.isEmpty()) {
            return new SleepAnalysisResult<>("Средняя продолжительность сессии", -1);
        }

        int averageDurationMinutes = (int) sleepingSessions.stream()
                .map(SleepingSession::getSleepDuration)
                .mapToInt(d -> (int) d.toMinutes())
                .average()
                .orElse(-1);

        return new SleepAnalysisResult<>("Средняя продолжительность сессии в минутах: 3", averageDurationMinutes);
    }
}
