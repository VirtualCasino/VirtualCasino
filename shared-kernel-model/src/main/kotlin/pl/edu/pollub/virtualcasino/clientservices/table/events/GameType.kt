package pl.edu.pollub.virtualcasino.clientservices.table.events

enum class GameType(val gameName: String, val maxPlayers: Int) {

    ROULETTE("Roulette", 10),
    POKER("Poker", 10)

}
