package co.nz.tsb.interview.bankrecmatchmaker.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import co.nz.tsb.interview.bankrecmatchmaker.core.MatchItem
import co.nz.tsb.interview.bankrecmatchmaker.core.Repository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class MatchViewModelTest {
    // Rule for LiveData testing
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    lateinit var repository: Repository

    private lateinit var viewModel: MatchViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher) // Replace the main dispatcher for coroutines
        viewModel = MatchViewModel(
            repository,
            testDispatcher,
            )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init() returns all records`() = runTest {
        val testData = MOCK_DATA_ITEMS_2

        `when`(repository.getRecords()).thenReturn(testData)

        viewModel.init(1000.0)

        advanceUntilIdle() // Run all coroutines immediately

        assertThat(viewModel.liveRecords.value).isEqualTo(testData)
    }

    @Test
    fun `init() selects an exact match`() = runTest {
        val testData = MOCK_DATA_ITEMS_2

        `when`(repository.getRecords()).thenReturn(testData)

        viewModel.init(testData[1].total.toDouble())

        advanceUntilIdle() // Run all coroutines immediately

        assertThat(viewModel.liveSelection.value).isEqualTo(setOf(1))
    }

    @Test
    fun `init() calculates the correct remain`() = runTest {
        val testData = MOCK_DATA_ITEMS_2

        `when`(repository.getRecords()).thenReturn(testData)

        // Calculate the sum to ensure no items are auto-selected.
        val total = testData.map { it.total }.sum().toDouble()

        viewModel.init(total)

        advanceUntilIdle()

        assertThat(viewModel.liveRemain.value).isEqualTo(total)
    }

    @Test
    fun `selectItem() calculates the correct remain`() = runTest {
        val testData = MOCK_DATA_ITEMS_2

        `when`(repository.getRecords()).thenReturn(testData)

        // The total is the sum of two records.
        // When one record is selected, the other should be the remain.
        val total = testData.map { it.total }.sum().toDouble()
        viewModel.init(total)

        // Insure the records are loaded
        advanceUntilIdle()

        viewModel.selectItem(0, true)

        advanceUntilIdle()

        assertThat(viewModel.liveRemain.value)
            .isEqualTo(testData[1].total.toDouble())
    }

    @Test
    fun `selectItem() provides the correct hints`() = runTest {
        val testData = MOCK_DATA_ITEMS_2

        `when`(repository.getRecords()).thenReturn(testData)

        // The total is the sum of two records.
        // When one record is selected, the other should be hinted.
        val total = testData.map { it.total }.sum().toDouble()
        viewModel.init(total)

        // Insure the records are loaded
        advanceUntilIdle()

        viewModel.selectItem(0, true)

        advanceUntilIdle()

        assertThat(viewModel.liveHints.value)
            .isEqualTo(setOf(1))
    }

    @Test
    fun `init() provides hints when multiple records match`() = runTest {
        val testData = MOCK_DATA_ITEMS_2

        `when`(repository.getRecords()).thenReturn(testData)

        // The total is the sum of two records.
        // When one record is selected, the other should be hinted.
        val total = testData.map { it.total }.sum().toDouble()
        viewModel.init(total)

        // Insure the records are loaded
        advanceUntilIdle()

        assertThat(viewModel.liveHints.value)
            .isEqualTo(setOf(0, 1))
    }

    @Test
    fun `selectItem() correctly selects the items`() = runTest {
        val testData = MOCK_DATA_ITEMS_2

        `when`(repository.getRecords()).thenReturn(testData)

        viewModel.init(100.0)

        // Insure the records are loaded
        advanceUntilIdle()

        viewModel.selectItem(1, true)

        advanceUntilIdle()

        assertThat(viewModel.liveSelection.value)
            .isEqualTo(setOf(1))
    }

    @Test
    fun `selectItem() deselects the item when already selected`() = runTest {
        val testData = MOCK_DATA_ITEMS_2

        `when`(repository.getRecords()).thenReturn(testData)

        viewModel.init(100.0)

        // Insure the records are loaded
        advanceUntilIdle()

        viewModel.selectItem(1, true)

        advanceUntilIdle()

        viewModel.selectItem(1, false)

        advanceUntilIdle()

        assertThat(viewModel.liveSelection.value)
            .isEqualTo(setOf<Int>())
    }

    @Test
    fun `selectItem() no hints are given when the remain reaches 0`() = runTest {
        val testData = MOCK_DATA_ITEMS_2

        `when`(repository.getRecords()).thenReturn(testData)

        val total = testData.map { it.total }.sum().toDouble()
        viewModel.init(total)

        advanceUntilIdle()

        viewModel.selectItem(0, true)
        viewModel.selectItem(1, true)

        advanceUntilIdle()

        assertThat(viewModel.liveRemain.value)
            .isEqualTo(0)

        assertThat(viewModel.liveHints.value)
            .isEqualTo(setOf<Int>())
    }

    @Test
    fun `selectItem() failed to work with a special dataset with precision issues`() = runTest {
        val testData = PRECISION_PROBLEM_DATASET

        `when`(repository.getRecords()).thenReturn(testData)

        val total = testData.map { it.total }.sum().toDouble()
        viewModel.init(total)

        advanceUntilIdle()

        // Select the first and the last elements
        viewModel.selectItem(0, true)

        advanceUntilIdle()

        viewModel.selectItem(testData.size - 1, true)

        advanceUntilIdle()

        assertThat(viewModel.liveHints.value)
            .isEqualTo(testData.indices.drop(1).dropLast(1).toSet())
    }

    companion object {
        private val MOCK_DATA_ITEMS_2 = listOf(
            MatchItem(
                "City Limousines",
                "30 Aug",
                249.00f,
                "Sales Invoice"
            ),
            MatchItem(
                "Ridgeway University",
                "12 Sep",
                618.50f,
                "Sales Invoice"
            )
        )

        private val PRECISION_PROBLEM_DATASET = listOf(
            MatchItem(
                "MCO Cleaning Services",
                "17 Sep",
                170.50f,
                "Sales Invoice"
            ),
            MatchItem(
                "Ridgeway University",
                "12 Sep",
                618.50f,
                "Sales Invoice"
            ),
            MatchItem(
                "SMART Agency",
                "12 Sep",
                250f,
                "Sales Invoice"
            ),
            MatchItem(
                "PowerDirect",
                "11 Sep",
                108.60f,
                "Sales Invoice"
            ),
        )
    }
}