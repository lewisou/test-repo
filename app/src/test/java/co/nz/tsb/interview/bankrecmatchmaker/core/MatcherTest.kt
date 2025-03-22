package co.nz.tsb.interview.bankrecmatchmaker.core

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import kotlin.math.min

class MatcherTest {

    @Test
    fun `fistMatch() works`() {
        val total = 100.98
        val matcher = Matcher(total,
            listOf(total / 2, total, total / 3),)

        assertThat(
            matcher.firstMatch()
        ).isEqualTo(1)
    }

    @Test
    fun `multiMatch() correctly handles cases where two consecutive elements sum to the total`() {
        val element1 = 20.1
        val element2 = 30.2
        val total = element1 + element2
        val wrongElement = min(element1, element2)  / 2.0

        val matcher = Matcher(total,
            listOf(
                wrongElement,
                element1,
                element2,
                wrongElement)
        )

        assertThat(matcher.multiMatch(2))
            .isEqualTo(setOf(1, 2))
    }

    @Test
    fun `multiMatch() correctly handles cases where three consecutive elements sum to the total`() {
        val element1 = 20.1
        val element2 = 30.2
        val element3 = 90.26

        val total = element1 + element2 + element3
        val wrongElement = minOf(element1, element2, element3)  / 2.0

        val matcher = Matcher(total,
            listOf(
                wrongElement,
                element1,
                element2,
                element3,
                wrongElement)
        )

        assertThat(matcher.multiMatch(3))
            .isEqualTo(setOf(1, 2, 3))
    }

    @Test
    fun `multiMatch() correctly handles cases where null elements in the list`() {
        val element1 = 20.1
        val element2 = null
        val element3 = 90.26

        val total = element1 + element3
        val wrongElement = minOf(element1, element3)  / 2.0

        val matcher = Matcher(total,
            listOf(
                wrongElement,
                element1,
                element2,
                element3,
                wrongElement)
        )

        assertThat(matcher.multiMatch(3))
            .isEqualTo(setOf(1, 2, 3))
    }

}