package co.nz.tsb.interview.bankrecmatchmaker.core

class Utils {
    companion object {
        fun <T> findDifference(set1: Set<T>, set2: Set<T>): Set<T> {
            return (set1 - set2) + (set2 - set1)
        }
    }
}