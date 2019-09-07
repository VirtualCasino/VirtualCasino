package pl.edu.pollub.virtualcasino.clientservices.samples.table.samples

import pl.edu.pollub.virtualcasino.clientservices.table.TableId

import static java.util.UUID.randomUUID

class SampleTableId {


    static TableId sampleTableId(customProperties = [:]) {
        def properties = [
                value: randomUUID()
        ] + customProperties
        return new TableId(properties.value as UUID)
    }

}
