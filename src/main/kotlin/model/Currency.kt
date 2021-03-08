package model

enum class Currency(
    private val USDPart: Double
) {
    USD(1.0), EUR(1.19), RUB(0.013);

    companion object {
        fun convert(amount: Double, from: Currency, to: Currency): Double {
            return amount * from.USDPart / to.USDPart
        }
    }
}