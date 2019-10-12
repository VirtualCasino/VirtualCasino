package pl.edu.pollub.virtualcasino.clientservices.config

import com.github.mongobee.changeset.ChangeLog
import com.github.mongobee.changeset.ChangeSet
import com.mongodb.BasicDBObject
import com.mongodb.DB

@ChangeLog
class ClientServicesViewDatabaseChangelog {

    @ChangeSet(order = "06-10-2019.22:26", id = "createViewDocuments", author = "Jarek0")
    fun createViewDocuments(db: DB) {
        db.createCollection("table_view", BasicDBObject())
    }

    @ChangeSet(order = "12-11-2019.20:49", id = "createClientViewDocument", author = "Jarek0")
    fun createClientViewDocument(db: DB) {
        db.createCollection("client_view", BasicDBObject())
    }

}