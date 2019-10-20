package pl.edu.pollub.virtualcasino.clientservices.table

data class TablePageView(
        val totalPages: Int,
        val currentPageNumber: Int,
        val content: List<TableView>
)