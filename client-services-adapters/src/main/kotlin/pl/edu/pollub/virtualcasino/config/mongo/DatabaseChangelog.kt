package pl.edu.pollub.virtualcasino.config.mongo

import com.github.mongobee.changeset.ChangeLog
import com.github.mongobee.changeset.ChangeSet
import com.mongodb.BasicDBObject
import com.mongodb.DB

@ChangeLog
class DatabaseChangelog {

    @ChangeSet(order = "27-07-2019.23:00", id = "createEventSourcingDocumentsAndTableParticipantsProjection", author = "Jarek0")
    fun createEventSourcingDocumentsAndTableParticipantsProjection(db: DB) {
        db.createCollection("event_stream", BasicDBObject())
        db.createCollection("event_descriptor", BasicDBObject())
        db.createCollection("participants_of_table", BasicDBObject())
    }

}