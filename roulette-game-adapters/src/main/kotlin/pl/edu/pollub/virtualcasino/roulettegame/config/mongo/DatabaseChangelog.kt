package pl.edu.pollub.virtualcasino.roulettegame.config.mongo

import com.github.mongobee.changeset.ChangeLog
import com.github.mongobee.changeset.ChangeSet
import com.mongodb.BasicDBObject
import com.mongodb.DB

@ChangeLog
class RouletteGameBoundedContextDatabaseChangelog {

    @ChangeSet(order = "18-08-2019.21:23", id = "createEventSourcingDocuments", author = "Jarek0")
    fun createEventSourcingDocumentsAndTableParticipantsProjection(db: DB) {
        db.createCollection("event_stream", BasicDBObject())
        db.createCollection("event_descriptor", BasicDBObject())
    }

}