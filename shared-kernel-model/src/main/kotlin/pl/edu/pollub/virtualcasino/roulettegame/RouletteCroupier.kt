package pl.edu.pollub.virtualcasino.roulettegame

interface RouletteCroupier {

    fun scheduleTheStartOfFirstSpinForGame(gameId: RouletteGameId)

    fun scheduleTheStartOfSpinForGame(gameId: RouletteGameId)

    fun scheduleTheFinishOfSpinForGame(gameId: RouletteGameId)

}