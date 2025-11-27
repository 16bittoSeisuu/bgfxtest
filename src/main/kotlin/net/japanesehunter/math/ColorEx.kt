package net.japanesehunter.math

// region constants

/**
 * Fully transparent black (0% RGB, 0% alpha).
 *
 * @return reusable transparent color.
 */
val Color.Companion.transparent: Color
  get() = TransparentColor

/**
 * Opaque black (0% RGB, 100% alpha).
 *
 * @return reusable opaque black color.
 */
val Color.Companion.black: Color
  get() = BlackColor

/**
 * Opaque white (100% RGB, 100% alpha).
 *
 * @return reusable opaque white color.
 */
val Color.Companion.white: Color
  get() = WhiteColor

/**
 * Opaque red (100% red, 0% green/blue, 100% alpha).
 *
 * @return reusable opaque red color.
 */
val Color.Companion.red: Color
  get() = RedColor

/**
 * Opaque green (100% green, 0% red/blue, 100% alpha).
 *
 * @return reusable opaque green color.
 */
val Color.Companion.green: Color
  get() = GreenColor

/**
 * Opaque blue (100% blue, 0% red/green, 100% alpha).
 *
 * @return reusable opaque blue color.
 */
val Color.Companion.blue: Color
  get() = BlueColor

/**
 * Opaque cyan (0% red, 100% green/blue, 100% alpha).
 *
 * @return reusable opaque cyan color.
 */
val Color.Companion.cyan: Color
  get() = CyanColor

/**
 * Opaque magenta (100% red/blue, 0% green, 100% alpha).
 *
 * @return reusable opaque magenta color.
 */
val Color.Companion.magenta: Color
  get() = MagentaColor

/**
 * Opaque yellow (100% red/green, 0% blue, 100% alpha).
 *
 * @return reusable opaque yellow color.
 */
val Color.Companion.yellow: Color
  get() = YellowColor

// endregion

// region constructors

/**
 * Creates a [Color] from channel proportions, reusing common constants when possible.
 *
 * @param red red channel as a proportion.
 * @param green green channel as a proportion.
 * @param blue blue channel as a proportion.
 * @param alpha alpha channel as a proportion; defaults to opaque.
 * @return a color composed of the provided channels, reusing transparent/black/white when matching.
 */
fun Color.Companion.rgba(
  red: Proportion,
  green: Proportion,
  blue: Proportion,
  alpha: Proportion = Proportion.full,
): Color {
  if (alpha == Proportion.empty && red == Proportion.empty && green == Proportion.empty && blue == Proportion.empty) {
    return transparent
  }
  if (alpha == Proportion.full && red == Proportion.empty && green == Proportion.empty && blue == Proportion.empty) {
    return black
  }
  if (alpha == Proportion.full && red == Proportion.full && green == Proportion.full && blue == Proportion.full) {
    return white
  }
  return ColorImpl(red, green, blue, alpha)
}

/**
 * Creates a [Color] from channel percentages, clamping each to 0.0..100.0.
 *
 * @param redPercent red channel percent.
 * @param greenPercent green channel percent.
 * @param bluePercent blue channel percent.
 * @param alphaPercent alpha channel percent.
 * @return a color composed of clamped channel proportions, reusing common constants when matching.
 * @throws IllegalArgumentException if any channel percent is not finite.
 */
fun Color.Companion.rgbaPercent(
  redPercent: Double,
  greenPercent: Double,
  bluePercent: Double,
  alphaPercent: Double = 100.0,
): Color {
  require(redPercent.isFinite()) { "Red percent must be finite" }
  require(greenPercent.isFinite()) { "Green percent must be finite" }
  require(bluePercent.isFinite()) { "Blue percent must be finite" }
  require(alphaPercent.isFinite()) { "Alpha percent must be finite" }

  return rgba(
    Proportion.percentClamping(redPercent),
    Proportion.percentClamping(greenPercent),
    Proportion.percentClamping(bluePercent),
    Proportion.percentClamping(alphaPercent),
  )
}

// endregion

// region internal implementation

private data class ColorImpl(
  override val red: Proportion,
  override val green: Proportion,
  override val blue: Proportion,
  override val alpha: Proportion,
) : Color

private val TransparentColor = ColorImpl(
  Proportion.empty,
  Proportion.empty,
  Proportion.empty,
  Proportion.empty,
)

private val BlackColor = ColorImpl(
  Proportion.empty,
  Proportion.empty,
  Proportion.empty,
  Proportion.full,
)

private val WhiteColor = ColorImpl(
  Proportion.full,
  Proportion.full,
  Proportion.full,
  Proportion.full,
)

private val RedColor = ColorImpl(
  Proportion.full,
  Proportion.empty,
  Proportion.empty,
  Proportion.full,
)

private val GreenColor = ColorImpl(
  Proportion.empty,
  Proportion.full,
  Proportion.empty,
  Proportion.full,
)

private val BlueColor = ColorImpl(
  Proportion.empty,
  Proportion.empty,
  Proportion.full,
  Proportion.full,
)

private val CyanColor = ColorImpl(
  Proportion.empty,
  Proportion.full,
  Proportion.full,
  Proportion.full,
)

private val MagentaColor = ColorImpl(
  Proportion.full,
  Proportion.empty,
  Proportion.full,
  Proportion.full,
)

private val YellowColor = ColorImpl(
  Proportion.full,
  Proportion.full,
  Proportion.empty,
  Proportion.full,
)

// endregion
