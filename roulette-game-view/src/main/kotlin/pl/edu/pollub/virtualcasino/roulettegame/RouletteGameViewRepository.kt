package pl.edu.pollub.virtualcasino.roulettegame

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria.*
import org.springframework.stereotype.Component
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import java.util.*

@Component
class RouletteGameViewRepository(private val rouletteGameViewMongoTemplate: MongoTemplate) {

    fun find(viewId: UUID): RouletteGameView? {
        val query = Query()
        query.addCriteria(where("rouletteGameViewId").isEqualTo(viewId))
        return rouletteGameViewMongoTemplate.findOne(query, RouletteGameView::class.java)
    }

    fun save(rouletteGameView: RouletteGameView) {
        rouletteGameViewMongoTemplate.save(rouletteGameView)
    }

}