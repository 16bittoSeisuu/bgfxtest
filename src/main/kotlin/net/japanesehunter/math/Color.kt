package net.japanesehunter.math

/**
 * Represents an RGBA color whose channels are expressed as [Proportion] values (0.0..100.0%).
 */
interface Color {
  /**
   * Red channel as a proportion.
   */
  val red: Proportion

  /**
   * Green channel as a proportion.
   */
  val green: Proportion

  /**
   * Blue channel as a proportion.
   */
  val blue: Proportion

  /**
   * Alpha channel as a proportion.
   */
  val alpha: Proportion

  companion object
}
