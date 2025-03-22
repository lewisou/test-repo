package co.nz.tsb.interview.bankrecmatchmaker.core

import com.google.common.truth.Truth
import org.junit.Test

class UtilsTest {

    @Test
    fun `findDifference() works`() {
        Truth.assertThat(Utils.findDifference(
            setOf(1, 2, 3),
            setOf(3, 4, 5)
        )).isEqualTo(
            setOf(1, 2, 4, 5)
        )
    }
}