package pl.edu.pollub.virtualcasino.roulettegame.fakes

import org.jetbrains.annotations.NotNull
import pl.edu.pollub.virtualcasino.roulettegame.RouletteCroupier
import pl.edu.pollub.virtualcasino.roulettegame.RouletteGameId

class FakedRouletteCroupier implements RouletteCroupier {

    @Override
    void scheduleTheStartOfFirstSpinForGame(@NotNull RouletteGameId gameId) {

    }

    @Override
    void scheduleTheStartOfSpinForGame(@NotNull RouletteGameId gameId) {

    }

    @Override
    void scheduleTheFinishOfSpinForGame(@NotNull RouletteGameId gameId) {

    }
}
