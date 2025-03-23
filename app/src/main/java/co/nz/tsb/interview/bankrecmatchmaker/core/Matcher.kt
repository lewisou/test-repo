package co.nz.tsb.interview.bankrecmatchmaker.core

import kotlin.math.roundToLong

/**
 * Implement the match algorithm
 */
internal class Matcher(
    total: Double,
    numbers: List<Double?>,
) {
    // Convert double to long,
    // as comparing doubles with == can lead to precision issues.
    private val totalToMatch: Long = (total * PRECISION).roundToLong()
    private val nums: List<Long?> = numbers.map {
        if(it == null) {
            null
        } else {
            (it * PRECISION).roundToLong()
        }
    }

    /**
     * Find the first number that equals to the total
     *
     * @return the index of the matched number, or null if not found.
     */
    fun firstMatch(): Int? {
        val index = nums.indexOf(totalToMatch)
        return if (index >= 0) {
            index
        } else {
            null
        }
    }

    /**
     * The method returns a list of indices corresponding to the numbers that sum up to the total.
     * If no matching is found, the method returns an empty array.
     *
     * @param max The max number of consecutive elements that sum up to the total
     * @return The matched index array
     */
    fun multiMatch(max: Int): Set<Int> {
        for(window in 2..max) {
            if (nums.size < window) break

            var start = 0
            var last = window
            var sum = nums.subList(start, last).sumOf { it ?: 0 }
            if (sum == totalToMatch) return (start until last).toSet()

            while(last < nums.size) {
                start ++
                last ++

                sum -= nums[start - 1] ?: 0
                sum += nums[last - 1] ?: 0
                if (sum == totalToMatch) return (start until last).toSet()
            }
        }
        return setOf()
    }

    companion object {
        const val PRECISION = 100.0
    }
}