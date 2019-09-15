package pl.edu.pollub.virtualcasino.roulettegame

import pl.edu.pollub.virtualcasino.roulettegame.ColorField.*
import pl.edu.pollub.virtualcasino.roulettegame.HalfDozenFiled.*
import pl.edu.pollub.virtualcasino.roulettegame.NumberField.*
import pl.edu.pollub.virtualcasino.roulettegame.PairField.*

interface RouletteField {

    fun valueMultiplier(): Int

    fun isDrawn(drawnField: NumberField): Boolean

}

enum class NumberField(private val number: Int, val color: ColorField?): RouletteField {
    NUMBER_0(0, null),
    NUMBER_1(1, RED), NUMBER_2(2, BLACK), NUMBER_3(3, RED),
    NUMBER_4(4, BLACK), NUMBER_5(5, RED), NUMBER_6(6, BLACK),
    NUMBER_7(7, RED), NUMBER_8(8, BLACK), NUMBER_9(9, RED),
    NUMBER_10(10, BLACK), NUMBER_11(11, BLACK), NUMBER_12(12, RED),
    NUMBER_13(13, BLACK), NUMBER_14(14, RED), NUMBER_15(15, BLACK),
    NUMBER_16(16, RED), NUMBER_17(17, BLACK), NUMBER_18(18, RED),
    NUMBER_19(19, RED), NUMBER_20(20, BLACK), NUMBER_21(21, RED),
    NUMBER_22(22, BLACK), NUMBER_23(23, RED), NUMBER_24(24, BLACK),
    NUMBER_25(25, RED), NUMBER_26(26, BLACK), NUMBER_27(27, RED),
    NUMBER_28(28, BLACK), NUMBER_29(29, BLACK), NUMBER_30(30, RED),
    NUMBER_31(31, BLACK), NUMBER_32(32, RED), NUMBER_33(33, BLACK),
    NUMBER_34(34, RED), NUMBER_35(25, BLACK), NUMBER_36(36, RED);

    override fun valueMultiplier(): Int = 35

    override fun isDrawn(drawnField: NumberField): Boolean = drawnField == this

    fun isOdd(): Boolean = number % 2 != 0

    fun isEven(): Boolean = number % 2 == 0
}

enum class PairField(private vararg val numbersInPair: NumberField): RouletteField {
    PAIR_0_1(NUMBER_0, NUMBER_1), PAIR_0_2(NUMBER_0, NUMBER_2), PAIR_0_3(NUMBER_0, NUMBER_3),
    PAIR_1_2(NUMBER_1, NUMBER_2), PAIR_2_3(NUMBER_2, NUMBER_3),
    PAIR_1_4(NUMBER_1, NUMBER_4), PAIR_2_5(NUMBER_2, NUMBER_5), PAIR_3_6(NUMBER_3, NUMBER_6),
    PAIR_4_5(NUMBER_4, NUMBER_5), PAIR_5_6(NUMBER_5, NUMBER_6),
    PAIR_4_7(NUMBER_4, NUMBER_7), PAIR_5_8(NUMBER_5, NUMBER_8), PAIR_6_9(NUMBER_6, NUMBER_9),
    PAIR_7_8(NUMBER_7, NUMBER_8), PAIR_8_9(NUMBER_8, NUMBER_9),
    PAIR_7_10(NUMBER_7, NUMBER_10), PAIR_8_11(NUMBER_8, NUMBER_11), PAIR_9_12(NUMBER_9, NUMBER_12),
    PAIR_10_11(NUMBER_10, NUMBER_11), PAIR_11_12(NUMBER_11, NUMBER_12),
    PAIR_10_13(NUMBER_10, NUMBER_13), PAIR_11_14(NUMBER_11, NUMBER_14), PAIR_12_15(NUMBER_12, NUMBER_15),
    PAIR_13_14(NUMBER_13, NUMBER_14), PAIR_14_15(NUMBER_14, NUMBER_15),
    PAIR_13_16(NUMBER_13, NUMBER_16), PAIR_14_17(NUMBER_14, NUMBER_17), PAIR_15_18(NUMBER_15, NUMBER_18),
    PAIR_16_17(NUMBER_16, NUMBER_17), PAIR_17_18(NUMBER_17, NUMBER_18),
    PAIR_16_19(NUMBER_16, NUMBER_19), PAIR_17_20(NUMBER_17, NUMBER_20), PAIR_18_21(NUMBER_18, NUMBER_21),
    PAIR_19_20(NUMBER_19, NUMBER_20), PAIR_20_21(NUMBER_20, NUMBER_21),
    PAIR_19_22(NUMBER_19, NUMBER_22), PAIR_20_23(NUMBER_20, NUMBER_23), PAIR_21_24(NUMBER_21, NUMBER_24),
    PAIR_22_23(NUMBER_22, NUMBER_23), PAIR_23_24(NUMBER_23, NUMBER_24),
    PAIR_22_25(NUMBER_22, NUMBER_25), PAIR_23_26(NUMBER_23, NUMBER_26), PAIR_24_27(NUMBER_24, NUMBER_27),
    PAIR_25_26(NUMBER_25, NUMBER_26), PAIR_26_27(NUMBER_26, NUMBER_27),
    PAIR_25_28(NUMBER_25, NUMBER_28), PAIR_26_29(NUMBER_26, NUMBER_29), PAIR_27_30(NUMBER_27, NUMBER_30),
    PAIR_28_29(NUMBER_28, NUMBER_29), PAIR_29_30(NUMBER_29, NUMBER_30),
    PAIR_28_31(NUMBER_28, NUMBER_31), PAIR_29_32(NUMBER_29, NUMBER_32), PAIR_30_33(NUMBER_30, NUMBER_33),
    PAIR_31_32(NUMBER_31, NUMBER_32), PAIR_32_33(NUMBER_32, NUMBER_33),
    PAIR_31_34(NUMBER_31, NUMBER_34), PAIR_32_35(NUMBER_32, NUMBER_35), PAIR_33_36(NUMBER_33, NUMBER_36),
    PAIR_34_35(NUMBER_34, NUMBER_35), PAIR_35_36(NUMBER_32, NUMBER_33);

    override fun valueMultiplier(): Int = 17

    override fun isDrawn(drawnField: NumberField): Boolean = numbersInPair.any { it.isDrawn(drawnField) }
}

enum class TripleFiled(private vararg val numbersInPair: NumberField): RouletteField {
    TRIPLE_0_1_2(NUMBER_0, NUMBER_1, NUMBER_2),
    TRIPLE_0_2_3(NUMBER_0, NUMBER_2, NUMBER_3),
    TRIPLE_1_2_3(NUMBER_1, NUMBER_2, NUMBER_3),
    TRIPLE_4_5_6(NUMBER_4, NUMBER_5, NUMBER_6),
    TRIPLE_7_8_9(NUMBER_7, NUMBER_8, NUMBER_9),
    TRIPLE_10_11_12(NUMBER_10, NUMBER_11, NUMBER_12),
    TRIPLE_13_14_15(NUMBER_13, NUMBER_14, NUMBER_15),
    TRIPLE_16_17_18(NUMBER_16, NUMBER_17, NUMBER_18),
    TRIPLE_19_20_21(NUMBER_19, NUMBER_20, NUMBER_21),
    TRIPLE_22_23_24(NUMBER_22, NUMBER_23, NUMBER_24),
    TRIPLE_25_26_27(NUMBER_25, NUMBER_26, NUMBER_27),
    TRIPLE_28_29_30(NUMBER_28, NUMBER_29, NUMBER_30),
    TRIPLE_31_32_33(NUMBER_31, NUMBER_32, NUMBER_33),
    TRIPLE_34_35_36(NUMBER_34, NUMBER_35, NUMBER_36);

    override fun valueMultiplier(): Int = 11

    override fun isDrawn(drawnField: NumberField): Boolean = numbersInPair.any { it.isDrawn(drawnField) }
}

enum class QuarterFiled(private vararg val pairsInQuarter: PairField): RouletteField {
    QUARTER_1_2_4_5(PAIR_1_4, PAIR_2_5),  QUARTER_2_3_5_6(PAIR_2_5, PAIR_3_6),
    QUARTER_4_5_7_8(PAIR_4_7, PAIR_5_8),  QUARTER_5_6_8_9(PAIR_5_8, PAIR_6_9),
    QUARTER_7_8_10_11(PAIR_7_10, PAIR_8_11),  QUARTER_8_9_11_12(PAIR_8_11, PAIR_9_12),
    QUARTER_10_11_13_14(PAIR_10_13, PAIR_11_14),  QUARTER_11_12_14_15(PAIR_11_14, PAIR_12_15),
    QUARTER_13_14_16_17(PAIR_13_16, PAIR_14_17),  QUARTER_14_15_17_18(PAIR_14_17, PAIR_15_18),
    QUARTER_16_17_19_20(PAIR_16_17, PAIR_17_20),  QUARTER_17_18_20_21(PAIR_17_20, PAIR_18_21),
    QUARTER_19_20_22_24(PAIR_19_22, PAIR_20_23),  QUARTER_20_21_23_24(PAIR_20_23, PAIR_21_24),
    QUARTER_22_23_25_26(PAIR_22_25, PAIR_23_26),  QUARTER_23_24_26_27(PAIR_23_26, PAIR_24_27),
    QUARTER_25_26_28_29(PAIR_25_28, PAIR_26_29),  QUARTER_26_27_29_30(PAIR_26_29, PAIR_27_30),
    QUARTER_28_29_31_32(PAIR_28_31, PAIR_29_32),  QUARTER_29_30_32_33(PAIR_29_32, PAIR_30_33),
    QUARTER_31_32_34_35(PAIR_31_34, PAIR_32_35),  QUARTER_32_33_35_36(PAIR_32_35, PAIR_33_36);

    override fun valueMultiplier(): Int = 8

    override fun isDrawn(drawnField: NumberField): Boolean = pairsInQuarter.any { it.isDrawn(drawnField) }
}

enum class HalfDozenFiled(private vararg val pairsInHalfDozen: PairField): RouletteField {
    HALF_DOZEN_1_6(PAIR_1_4, PAIR_2_5, PAIR_3_6),
    HALF_DOZEN_4_9(PAIR_4_7, PAIR_5_8, PAIR_6_9),
    HALF_DOZEN_7_12(PAIR_7_10, PAIR_8_11, PAIR_9_12),
    HALF_DOZEN_10_15(PAIR_10_13, PAIR_11_14, PAIR_12_15),
    HALF_DOZEN_13_18(PAIR_13_16, PAIR_14_17, PAIR_15_18),
    HALF_DOZEN_16_21(PAIR_16_19, PAIR_17_20, PAIR_18_21),
    HALF_DOZEN_19_24(PAIR_19_22, PAIR_20_23, PAIR_21_24),
    HALF_DOZEN_22_27(PAIR_22_25, PAIR_23_26, PAIR_24_27),
    HALF_DOZEN_25_30(PAIR_25_28, PAIR_26_29, PAIR_27_30),
    HALF_DOZEN_28_33(PAIR_28_31, PAIR_29_32, PAIR_30_33),
    HALF_DOZEN_31_36(PAIR_31_34, PAIR_32_35, PAIR_33_36);

    override fun valueMultiplier(): Int = 5

    override fun isDrawn(drawnField: NumberField): Boolean = pairsInHalfDozen.any { it.isDrawn(drawnField) }
}

enum class DozenFiled(private vararg val halfOdDozensInDozen: HalfDozenFiled): RouletteField {
    DOZEN_1_12(HALF_DOZEN_1_6, HALF_DOZEN_7_12),
    DOZEN_13_24(HALF_DOZEN_13_18, HALF_DOZEN_19_24),
    DOZEN_25_36(HALF_DOZEN_25_30, HALF_DOZEN_31_36);

    override fun valueMultiplier(): Int = 2

    override fun isDrawn(drawnField: NumberField): Boolean = halfOdDozensInDozen.any { it.isDrawn(drawnField) }
}

enum class ColumnField(private vararg val pairsInColumn: PairField): RouletteField {
    COLUMN_1_34(PAIR_1_4, PAIR_7_10,  PAIR_13_16, PAIR_19_22, PAIR_25_28, PAIR_31_34),
    COLUMN_2_35(PAIR_2_5, PAIR_8_11,  PAIR_14_17, PAIR_20_23, PAIR_26_29, PAIR_32_35),
    COLUMN_3_36(PAIR_3_6, PAIR_9_12,  PAIR_15_18, PAIR_21_24, PAIR_27_30, PAIR_33_36);

    override fun valueMultiplier(): Int = 2

    override fun isDrawn(drawnField: NumberField): Boolean = pairsInColumn.any { it.isDrawn(drawnField) }
}

enum class HalfBoardField(private vararg val halfDozensInHalfBoard: HalfDozenFiled): RouletteField {
    HALF_BOARD_1_18(HALF_DOZEN_1_6, HALF_DOZEN_7_12, HALF_DOZEN_13_18),
    HALF_BOARD_19_36(HALF_DOZEN_19_24, HALF_DOZEN_25_30, HALF_DOZEN_31_36);

    override fun valueMultiplier(): Int = 1

    override fun isDrawn(drawnField: NumberField): Boolean = halfDozensInHalfBoard.any { it.isDrawn(drawnField) }
}

enum class ColorField: RouletteField {
    RED,
    BLACK;

    override fun isDrawn(drawnField: NumberField): Boolean = drawnField.color == this
    override fun valueMultiplier(): Int = 1
}

enum class EvenField: RouletteField {
    EVEN;

    override fun valueMultiplier(): Int = 1

    override fun isDrawn(drawnField: NumberField): Boolean = drawnField.isEven()
}

enum class OddField: RouletteField {
    ODD;

    override fun valueMultiplier(): Int = 1

    override fun isDrawn(drawnField: NumberField): Boolean = drawnField.isOdd()
}
