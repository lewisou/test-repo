package co.nz.tsb.interview.bankrecmatchmaker.repository

import co.nz.tsb.interview.bankrecmatchmaker.core.MatchItem
import co.nz.tsb.interview.bankrecmatchmaker.core.Repository
import java.text.SimpleDateFormat
import java.util.Locale

class FakeRemoteRepository : Repository {
    override fun getRecords() = MOCKED_DATA

    companion object {
        private val MOCKED_DATA = buildMockData().sortedByDescending {
            SimpleDateFormat("dd MMM", Locale.ENGLISH).parse (it.transactionDate)
        }

        private fun buildMockData(): List<MatchItem> {
            val items: MutableList<MatchItem> = ArrayList()
            items.add(
                MatchItem(
                    "City Limousines",
                    "30 Aug",
                    249.00f,
                    "Sales Invoice"
                )
            )
            items.add(
                MatchItem(
                    "Ridgeway University",
                    "12 Sep",
                    618.50f,
                    "Sales Invoice"
                )
            )
            items.add(
                MatchItem(
                    "Cube Land",
                    "22 Sep",
                    495.00f,
                    "Sales Invoice"
                )
            )
            items.add(
                MatchItem(
                    "Bayside Club",
                    "23 Sep",
                    234.00f,
                    "Sales Invoice"
                )
            )
            items.add(
                MatchItem(
                    "SMART Agency",
                    "12 Sep",
                    250f,
                    "Sales Invoice"
                )
            )
            items.add(
                MatchItem(
                    "PowerDirect",
                    "11 Sep",
                    108.60f,
                    "Sales Invoice"
                )
            )
            items.add(
                MatchItem(
                    "PC Complete",
                    "17 Sep",
                    216.99f,
                    "Sales Invoice"
                )
            )
            items.add(
                MatchItem(
                    "Truxton Properties",
                    "17 Sep",
                    181.25f,
                    "Sales Invoice"
                )
            )
            items.add(
                MatchItem(
                    "MCO Cleaning Services",
                    "17 Sep",
                    170.50f,
                    "Sales Invoice"
                )
            )
            items.add(
                MatchItem(
                    "Gateway Motors",
                    "18 Sep",
                    1411.35f,
                    "Sales Invoice"
                )
            )
            return items
        }
    }
}
