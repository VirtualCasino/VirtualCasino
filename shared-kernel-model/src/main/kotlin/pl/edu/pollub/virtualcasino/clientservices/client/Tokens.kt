package pl.edu.pollub.virtualcasino.clientservices.client

data class Tokens(val count: Int = 0) {

    fun changeCount(tokens: Tokens = Tokens()): Tokens = Tokens(this.count + tokens.count)

    operator fun compareTo(other: Tokens): Int = count - other.count

}