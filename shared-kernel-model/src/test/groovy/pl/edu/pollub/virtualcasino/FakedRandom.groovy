package pl.edu.pollub.virtualcasino

class FakedRandom extends Random {

    private int randomValue = 0

    void setRandomIntValue(int value) {
        this.randomValue = value
    }

    @Override
    int nextInt(int bound) {
        return randomValue
    }

}
