package pl.edu.pollub.virtualcasino

import java.time.Clock
import java.time.Instant
import java.time.ZoneId

import static pl.edu.pollub.virtualcasino.SamplePointInTime.samplePointInTime

class FakedClock extends Clock {

    private Instant currentTime
    private ZoneId zoneId
    private FakedTaskExecutor taskExecutor

    FakedClock(Instant currentTime) {
        this.currentTime = currentTime
        this.zoneId = ZoneId.systemDefault()
        taskExecutor = new FakedTaskExecutor()
    }

    FakedClock(Instant currentTime, FakedTaskExecutor taskExecutor) {
        this.currentTime = currentTime
        this.zoneId = ZoneId.systemDefault()
        this.taskExecutor = taskExecutor
    }

    @Override
    ZoneId getZone() {
        return zoneId
    }

    @Override
    Clock withZone(ZoneId zoneId) {
        return new FakedClock(samplePointInTime())
    }

    @Override
    Instant instant() {
        return currentTime
    }

    void moveTo(Instant pointInTime) {
        currentTime = pointInTime
        taskExecutor.executeTasksForTimePoint(pointInTime)
    }
}
