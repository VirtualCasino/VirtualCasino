package pl.edu.pollub.virtualcasino.clientservices.table

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.BasicQuery
import org.springframework.data.mongodb.core.query.Criteria.*
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.util.*
import kotlin.math.floor
import kotlin.text.Typography.dollar

@Component
class TableViewRepository(private val clientServicesViewMongoTemplate: MongoTemplate) {

    fun find(aggregateId: UUID): TableView? {
        val query = Query()
        query.addCriteria(where("tableViewId").isEqualTo(aggregateId))
        return clientServicesViewMongoTemplate.findOne(query, TableView::class.java)
    }

    fun save(tableView: TableView) {
        clientServicesViewMongoTemplate.save(tableView)
    }

    fun findAllOpenRouletteTables(searchedPlayerNick: String, pageNumber: Int): TablePageView {
        val countQuery = BasicQuery("""{ ${dollar}where: "this.playersIds.length < this.maxPlayersCount && this.playersIds.length > 0" }""")
                .addCriteria(where("gameType").isEqualTo("Roulette"))
                .addCriteria(where("firstPlayerNick").regex(".*$searchedPlayerNick.*"))
        val allTablesCount = clientServicesViewMongoTemplate.count(countQuery, TableView::class.java)
        val allTablePagesCount = floor((BigDecimal(allTablesCount) / BigDecimal(PAGE_SIZE)).toDouble()).toInt()
        val currentPage = when(pageNumber > allTablePagesCount) {
            true -> allTablePagesCount
            else -> pageNumber
        }
        val query = countQuery.limit(PAGE_SIZE).skip((PAGE_SIZE * currentPage).toLong())
        val tables = clientServicesViewMongoTemplate.find(query, TableView::class.java)
        return TablePageView(
                allTablePagesCount,
                currentPage,
                tables
        )
    }

    companion object {
        const val PAGE_SIZE = 10
    }
}