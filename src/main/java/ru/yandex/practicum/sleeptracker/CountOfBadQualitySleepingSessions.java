package ru.yandex.practicum.sleeptracker;

import java.util.List;
import java.util.function.Function;

public class CountOfBadQualitySleepingSessions implements Function<List<SleepingSession>, SleepAnalysisResult<?>> {
    @Override
    public SleepAnalysisResult<Integer> apply(List<SleepingSession> sleepingSessionlist) {

        if (sleepingSessionlist.isEmpty()) {
            return new SleepAnalysisResult<>("Количество сессий с плохим качеством сна: ", -1);
        }

        int badQualitySessionsCount = (int) sleepingSessionlist.stream()
                .filter(session -> session.getSleepQuality() == QualityOfSleep.BAD)
                .count();

        return new SleepAnalysisResult<>("Количество сессий с плохим качеством сна: ", badQualitySessionsCount);

    }
}
