package net.japanesehunter.math

/**
 * Represents a percentage-based proportion constrained to the range 0.0..100.0 inclusive.
 */
interface Proportion {
  /**
   * Percentage value in the range 0.0..100.0 inclusive.
   */
  val percent: Double

  companion object
}
