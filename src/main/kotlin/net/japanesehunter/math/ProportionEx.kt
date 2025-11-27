package net.japanesehunter.math

// region constants

/**
 * Proportion representing 0%.
 *
 * @return a reusable proportion with percent 0.0.
 */
val Proportion.Companion.empty: Proportion
  get() = EmptyProportion

/**
 * Proportion representing 100%.
 *
 * @return a reusable proportion with percent 100.0.
 */
val Proportion.Companion.full: Proportion
  get() = FullProportion

// endregion

// region constructors

/**
 * Creates a [Proportion] from this integer percentage, clamping values to 0..100.
 *
 * @return a proportion whose percent is clamped to 0.0..100.0.
 */
val Int.percent: Proportion
  get() = this.toDouble().percent

/**
 * Creates a [Proportion] from this percentage value, clamping values to 0..100.
 *
 * @return a proportion whose percent is clamped to 0.0..100.0.
 * @throws IllegalArgumentException if the value is not finite.
 */
val Double.percent: Proportion
  get() = Proportion.percentClamping(this)

/**
 * Creates a [Proportion] from the given percentage value by clamping to 0.0..100.0.
 *
 * @param value percentage to clamp.
 * @return a proportion whose percent is clamped to 0.0..100.0.
 * @throws IllegalArgumentException if [value] is NaN or infinite.
 */
fun Proportion.Companion.percentClamping(value: Double): Proportion {
  require(value.isFinite()) { "Percent value must be finite" }
  val normalizedPercent = value.coerceIn(0.0, 100.0)
  if (normalizedPercent == 0.0) return empty
  if (normalizedPercent == 100.0) return full
  return ProportionImpl(normalizedPercent)
}

/**
 * Creates a [Proportion] from the given percentage value.
 *
 * @param value percentage that must be finite and within 0.0..100.0 inclusive.
 * @return a proportion with the given percent, or `null` if [value] is out of range or not finite.
 */
fun Proportion.Companion.percentOrNull(value: Double): Proportion? {
  if (!value.isFinite()) return null
  if (value !in 0.0..100.0) return null
  if (value == 0.0) return empty
  if (value == 100.0) return full
  return ProportionImpl(value)
}

// endregion

// region arithmetic

/**
 * Adds two proportions and clamps the result to 0.0..100.0.
 *
 * @param other proportion to add.
 * @return summed proportion clamped to the valid range.
 */
operator fun Proportion.plus(other: Proportion): Proportion = Proportion.percentClamping(this.percent + other.percent)

/**
 * Subtracts a proportion from this one and clamps the result to 0.0..100.0.
 *
 * @param other proportion to subtract.
 * @return difference clamped to the valid range.
 */
operator fun Proportion.minus(other: Proportion): Proportion = Proportion.percentClamping(this.percent - other.percent)

/**
 * Scales this proportion's rate (0.0..1.0) by the given scalar.
 *
 * @param scalar multiplier applied to the rate value; must be finite.
 * @return rate value after scaling.
 * @throws IllegalArgumentException if [scalar] is not finite.
 */
operator fun Proportion.times(scalar: Double): Double {
  require(scalar.isFinite()) { "Scalar must be finite" }
  return this.rate * scalar
}

/**
 * Scales this proportion's rate (0.0..1.0) by the given value.
 *
 * @param proportion proportion applied to this value.
 * @return rate value after scaling.
 * @throws IllegalArgumentException if this value is not finite.
 */
operator fun Double.times(proportion: Proportion): Double {
  require(this.isFinite()) { "Value must be finite" }
  return this * proportion.rate
}

/**
 * Divides this proportion by the given scalar and clamps the result to 0.0..100.0.
 *
 * @param scalar divisor applied to the percent value; must be finite and non-zero.
 * @return proportion whose percent is the division result clamped to the valid range.
 * @throws IllegalArgumentException if [scalar] is not finite or zero.
 */
operator fun Proportion.div(scalar: Double): Proportion {
  require(scalar.isFinite()) { "Scalar must be finite" }
  require(scalar != 0.0) { "Scalar must not be zero" }
  return Proportion.percentClamping(this.percent / scalar)
}

/**
 * Divides this value by the given proportion's rate (0.0..1.0).
 *
 * @param proportion proportion divisor; must be finite and non-zero.
 * @return value divided by the proportion's rate.
 * @throws IllegalArgumentException if this value is not finite
 */
operator fun Double.div(proportion: Proportion): Double {
  require(this.isFinite()) { "Value must be finite" }
  return this / proportion.rate
}

// endregion

// region extensions

/**
 * Percent expressed as a fraction between 0.0 and 1.0.
 *
 * @return percent divided by 100.0.
 */
val Proportion.rate: Double
  get() = this.percent / 100.0

/**
 * Same as [rate] but using [Float] precision.
 *
 * @return percent divided by 100.0 as a Float.
 */
val Proportion.ratef: Float
  get() = this.rate.toFloat()

// endregion

// region internal implementation

private data class ProportionImpl(
  override val percent: Double,
) : Proportion {
  init {
    require(percent in 0.0..100.0) { "percent must be between 0.0 and 100.0" }
  }
}

private val EmptyProportion = ProportionImpl(0.0)
private val FullProportion = ProportionImpl(100.0)

// endregion
