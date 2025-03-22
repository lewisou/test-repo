package co.nz.tsb.interview.bankrecmatchmaker.core

interface Repository {
    fun getRecords(): List<MatchItem>
}