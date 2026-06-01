package ru.yandex.practicum.sleeptracker;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SleepingPersonalityClassificator implements Function<List<SleepingSession>, SleepAnalysisResult<?>> {

    @Override
    public SleepAnalysisResult<SleepingPersonality> apply(List<SleepingSession> sleepingSessions) {

        List<SleepingSession> validSessions = sleepingSessions.stream()
                .filter(s -> !(s.getSleepStartDate().isBefore(s.getWakeUpDate()) && s.getWakeUpDate().getDayOfMonth() != 1))
                .filter(s -> !(s.getSleepStartTime().isAfter(LocalTime.MIDNIGHT) && s.getWakeUpTime().isBefore(LocalTime.of(6, 0))))
                .toList();


        Map<SleepingPersonality, Long> counts = validSessions.stream()
                .collect(Collectors.groupingBy(s -> {
                    if (s.getSleepStartTime().isAfter(LocalTime.of(23, 0)) && s.getWakeUpTime().isAfter(LocalTime.of(9, 0))) {
                        return SleepingPersonality.OWL;
                    } else if (s.getSleepStartTime().isBefore(LocalTime.of(22, 0)) && s.getWakeUpTime().isBefore(LocalTime.of(7, 0))) {
                        return SleepingPersonality.LARK;
                    } else {
                        return SleepingPersonality.PIGEON; // Все остальные автоматически стали голубями
                    }
                }, Collectors.counting()));


        long owlCount = counts.getOrDefault(SleepingPersonality.OWL, 0L);
        long larkCount = counts.getOrDefault(SleepingPersonality.LARK, 0L);


        SleepingPersonality finalType = SleepingPersonality.PIGEON;
        if (owlCount > larkCount) finalType = SleepingPersonality.OWL;
        if (larkCount > owlCount) finalType = SleepingPersonality.LARK;

        return new SleepAnalysisResult<>("Вы относитесь к хронотипу: ", finalType);
    }
}
