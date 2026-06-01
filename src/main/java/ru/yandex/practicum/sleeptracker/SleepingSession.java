package ru.yandex.practicum.sleeptracker;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class SleepingSession {
    private final LocalDateTime sleepStartTime;
    private final LocalDateTime wakeUpTime;
    private  final QualityOfSleep qualityOfSleep;

    public SleepingSession(LocalDateTime fallingAsleepTime, LocalDateTime wakingUpTime, QualityOfSleep qualityOfSleep) {
        this.sleepStartTime = fallingAsleepTime;
        this.wakeUpTime = wakingUpTime;
        this.qualityOfSleep = qualityOfSleep;
    }

    //Date + Time
    public LocalDateTime getSleepStartDateTime() {
        return sleepStartTime;
    }

    public LocalDateTime getWakeUpDateTime() {
        return wakeUpTime;
    }

    //Quality
    public QualityOfSleep getSleepQuality() {
        return qualityOfSleep;
    }


    //Date
    public LocalDate getSleepStartDate(){
        return sleepStartTime.toLocalDate();
    }
    public LocalDate getWakeUpDate(){
        return wakeUpTime.toLocalDate();
    }


    //Time
    public LocalTime getSleepStartTime(){
        return sleepStartTime.toLocalTime();
    }
    public LocalTime getWakeUpTime(){
        return wakeUpTime.toLocalTime();
    }


    public Duration getSleepDuration() {

        return Duration.between(sleepStartTime, wakeUpTime);
    }


    @Override
    public String toString() {
        return String.format("Сон с %s до %s (качество сна: %s)",
                sleepStartTime, wakeUpTime, qualityOfSleep);
    }
}
