package ru.yandex.practicum.sleeptracker;

import java.util.List;
import java.util.function.Function;

public class CountOfSleepingSessions implements Function<List<SleepingSession>, SleepAnalysisResult<?>> {
    @Override
    public SleepAnalysisResult<Integer> apply(List<SleepingSession> sleepingSessions) {
        int count = sleepingSessions.size();
        return new SleepAnalysisResult<>("Общее количество сессий сна: ", count);
    }
}
