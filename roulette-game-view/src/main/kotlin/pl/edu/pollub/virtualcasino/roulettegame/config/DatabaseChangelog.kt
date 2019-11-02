package pl.edu.pollub.virtualcasino.roulettegame.config

import com.github.mongobee.changeset.ChangeLog
import com.github.mongobee.changeset.ChangeSet
import com.mongodb.BasicDBObject
import com.mongodb.DB

@ChangeLog
class RouletteGameViewDatabaseChangelog {

    @ChangeSet(order = "01-11-2019.16:54", id = "createViewDocuments", author = "Jarek0")
    fun createViewDocuments(db: DB) {
        db.createCollection("roulette_game_view", BasicDBObject())
    }

}