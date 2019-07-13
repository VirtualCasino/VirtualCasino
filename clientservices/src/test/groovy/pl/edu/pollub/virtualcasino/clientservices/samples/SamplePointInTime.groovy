package pl.edu.pollub.virtualcasino.clientservices.samples

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class SamplePointInTime {

    static Instant samplePointInTime(customProperties = [:]) {
        def properties = [
                year: 2019,
                month: 7,
                dayOfMonth: 7,
                hour: 18,
                minute: 0,
                second: 0,
                nanoOfSecond: 0
        ] + customProperties
        return LocalDateTime.of(
                properties.year as Integer,
                properties.month as Integer,
                properties.hour as Integer,
                properties.minute as Integer,
                properties.second as Integer,
                properties.nanoOfSecond as Integer
        ).toInstant(ZoneOffset.UTC)
    }

}
