package co.nz.tsb.interview.bankrecmatchmaker.di

import co.nz.tsb.interview.bankrecmatchmaker.core.MatchItem
import co.nz.tsb.interview.bankrecmatchmaker.core.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [Modules::class]
)
class TestModules {
    @Provides
    fun provideApiService(): Repository {
        return MockedRemoteRepository
    }

    @Provides
    fun provideDispatchers(): CoroutineDispatcher = Dispatchers.Default
}

object MockedRemoteRepository : Repository {
    override fun getRecords() = MOCKED_DATA

    private val MOCKED_DATA = buildMockData().sortedByDescending {
        SimpleDateFormat("dd MMM", Locale.ENGLISH).parse (it.transactionDate)
    }

    private fun buildMockData(): List<MatchItem> {
        val dateFormatter = DateTimeFormatter.ofPattern("dd MMM")

        val now = LocalDate.now()
        return (1..30).map { i ->
            MatchItem(
                "Paid To $i",
                now.plusWeeks(-i.toLong()).format(dateFormatter),
                (i * 1000 + 0.45).toFloat(),
                "Sales Invoice $i"
            )
        }
    }

}