package net.japanesehunter.math

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.floats.plusOrMinus
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs

class ProportionTest : FunSpec({

  context("constants") {
    test("empty/full are reused and have expected percents") {
      Proportion.empty.percent shouldBe 0.0
      Proportion.full.percent shouldBe 100.0

      Proportion.percentClamping(0.0) shouldBeSameInstanceAs Proportion.empty
      Proportion.percentClamping(100.0) shouldBeSameInstanceAs Proportion.full
    }
  }

  context("constructors") {
    test("Int.percent and Double.percent clamp to bounds") {
      (-10).percent shouldBeSameInstanceAs Proportion.empty
      120.0.percent shouldBeSameInstanceAs Proportion.full
      50.percent.percent shouldBe 50.0
    }

    test("percentClamping clamps extremes and rejects non-finite") {
      Proportion.percentClamping(-1.0) shouldBeSameInstanceAs Proportion.empty
      Proportion.percentClamping(1e9) shouldBeSameInstanceAs Proportion.full
      shouldThrow<IllegalArgumentException> { Proportion.percentClamping(Double.NaN) }
      shouldThrow<IllegalArgumentException> { Proportion.percentClamping(Double.POSITIVE_INFINITY) }
      shouldThrow<IllegalArgumentException> { Proportion.percentClamping(Double.NEGATIVE_INFINITY) }
    }

    test("percent rejects non-finite values") {
      shouldThrow<IllegalArgumentException> { Double.NaN.percent }
      shouldThrow<IllegalArgumentException> { Double.POSITIVE_INFINITY.percent }
      shouldThrow<IllegalArgumentException> { Double.NEGATIVE_INFINITY.percent }
    }

    test("percentOrNull returns null outside range or non-finite") {
      Proportion.percentOrNull(-0.1) shouldBe null
      Proportion.percentOrNull(100.1) shouldBe null
      Proportion.percentOrNull(Double.NaN) shouldBe null
      Proportion.percentOrNull(Double.POSITIVE_INFINITY) shouldBe null
      Proportion.percentOrNull(-1e-12) shouldBe null
      Proportion.percentOrNull(100.0 + 1e-12) shouldBe null
    }

    test("percentOrNull returns constants at bounds") {
      Proportion.percentOrNull(0.0) shouldBeSameInstanceAs Proportion.empty
      Proportion.percentOrNull(-0.0) shouldBeSameInstanceAs Proportion.empty
      Proportion.percentOrNull(100.0) shouldBeSameInstanceAs Proportion.full
      Proportion.percentOrNull(42.0)?.percent shouldBe 42.0
    }
  }

  context("arithmetic") {
    test("addition and subtraction clamp and reuse constants") {
      val thirty = 30.percent
      val forty = 40.percent
      (thirty + forty).percent shouldBe 70.0
      (thirty + forty + forty).percent shouldBe 100.0
      (thirty + forty + forty) shouldBeSameInstanceAs Proportion.full
      (thirty - forty) shouldBeSameInstanceAs Proportion.empty
    }

    test("multiplication scales percent and clamps; validates scalar/value") {
      (50.percent * 2.0) shouldBeSameInstanceAs Proportion.full
      (50.percent * 0.5).percent shouldBe 25.0
      (50.percent * -2.0) shouldBeSameInstanceAs Proportion.empty
      (50.percent * 10_000.0) shouldBeSameInstanceAs Proportion.full
      (2.0 * 50.percent) shouldBe (1.0 plusOrMinus 1e-9)
      shouldThrow<IllegalArgumentException> { 50.percent * Double.NaN }
      shouldThrow<IllegalArgumentException> { Double.NaN * 50.percent }
      shouldThrow<IllegalArgumentException> { Double.POSITIVE_INFINITY * 50.percent }
    }

    test("division by scalar returns proportion and clamps") {
      (50.percent / 2.0).percent shouldBe 25.0
      (50.percent / -2.0) shouldBeSameInstanceAs Proportion.empty
      shouldThrow<IllegalArgumentException> { 50.percent / 0.0 }
      shouldThrow<IllegalArgumentException> { 50.percent / Double.NaN }
    }

    test("double divided by proportion uses rate") {
      (10.0 / 50.percent) shouldBe (20.0 plusOrMinus 1e-9)
      (10.0 / Proportion.empty).isInfinite() shouldBe true
    }
  }

  context("extensions") {
    test("rate and ratef reflect percent") {
      val p = 75.percent
      p.rate shouldBe (0.75 plusOrMinus 1e-9)
      p.ratef shouldBe (0.75f plusOrMinus 1e-6f)
    }

    test("rate boundaries match empty/full") {
      Proportion.empty.rate shouldBe 0.0
      Proportion.full.rate shouldBe 1.0
    }
  }
})
