package pl.edu.pollub.virtualcasino.clientservices.client.samples.comands

import pl.edu.pollub.virtualcasino.clientservices.client.Nick
import pl.edu.pollub.virtualcasino.clientservices.client.commands.RegisterClient

import static pl.edu.pollub.virtualcasino.clientservices.samples.client.samples.SampleNick.sampleNick

class SampleRegisterClient {

    static RegisterClient sampleRegisterClient(customProperties = [:]) {
        def properties = [
                nick: sampleNick()
        ] + customProperties
        return new RegisterClient(
                properties.nick as Nick
        )
    }

}
