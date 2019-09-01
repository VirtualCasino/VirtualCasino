package pl.edu.pollub.virtualcasino.clientservices.client

data class Tokens(val count: Int = 0) {

    operator fun plus(other: Tokens): Tokens = Tokens(this.count + other.count)

    operator fun compareTo(other: Tokens): Int = count - other.count

}