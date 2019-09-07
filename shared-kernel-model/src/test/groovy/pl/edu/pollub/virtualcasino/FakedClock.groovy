package pl.edu.pollub.virtualcasino

import java.time.Clock
import java.time.Instant
import java.time.ZoneId

import static pl.edu.pollub.virtualcasino.SamplePointInTime.samplePointInTime

class FakedClock extends Clock {

    private Instant currentTime
    private ZoneId zoneId

    FakedClock(Instant currentTime) {
        this.currentTime = currentTime
        this.zoneId = ZoneId.systemDefault()
    }

    @Override
    ZoneId getZone() {
        return zoneId
    }

    @Override
    Clock withZone(ZoneId zoneId) {
        return new FakedClock(samplePointInTime(), zoneId)
    }

    @Override
    Instant instant() {
        return currentTime
    }

    void moveTo(Instant pointInTime) {
        currentTime = pointInTime
    }
}
