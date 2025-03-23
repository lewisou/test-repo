package co.nz.tsb.interview.bankrecmatchmaker.view

import android.content.Intent
import android.graphics.Color
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withChild
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import co.nz.tsb.interview.bankrecmatchmaker.R
import co.nz.tsb.interview.bankrecmatchmaker.core.MatchItem
import co.nz.tsb.interview.bankrecmatchmaker.di.MockedRemoteRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.containsString
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.text.DecimalFormat


@HiltAndroidTest
class FindMatchActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var records: List<MatchItem>

    @Before
    fun init() {
        hiltRule.inject()
        records = MockedRemoteRepository.getRecords()
    }

    @Test
    fun records_should_be_loaded() {
        launchWithTotal(0.0f)
        val decimalFormat = DecimalFormat("#,###.00")

        records.take(5).forEach {
            onView(withText(it.paidTo)).check(matches(isDisplayed()))
            onView(withText(decimalFormat.format(it.total))).check(matches(isDisplayed()))
            onView(withText(it.docType)).check(matches(isDisplayed()))
            onView(withText(it.transactionDate)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun all_records_should_be_loaded() {
        launchWithTotal(0.0f)
        val decimalFormat = DecimalFormat("#,###.00")

        onView(withId(R.id.recycler_view)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                records.size - 1
            )
        )

        records.takeLast(5).forEach {
            onView(withText(it.paidTo)).check(matches(isDisplayed()))
            onView(withText(decimalFormat.format(it.total))).check(matches(isDisplayed()))
            onView(withText(it.docType)).check(matches(isDisplayed()))
            onView(withText(it.transactionDate)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun the_remain_is_correctly_displayed() {
        val nonMatchTotal = (records.minBy { it.total }.total / 2)
        launchWithTotal(nonMatchTotal)

        val totalTxt = String.format("%.2f", nonMatchTotal)
        onView(withText(containsString(totalTxt)))
            .check(matches(isDisplayed()))
    }

    @Test
    fun the_single_match_item_should_be_selected() {
        launchWithTotal(records.first().total)
        onView(
            allOf(
                withClassName(containsString(AppCompatCheckBox::class.java.name)),
                isChecked(),
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun remain_should_be_updated_after_selection() {
        launchWithTotal(
            records.take(2).map { it.total }.sum()
        )

        onView(withId(R.id.recycler_view))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        onView(
            allOf(
                withId(R.id.match_text),
                withText(containsString("${records[1].total.toLong()}"))
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun no_item_should_be_selected_if_no_match_found() {
        launchWithTotal(0.0f)
        onView(
            allOf(
                withClassName(containsString(AppCompatCheckBox::class.java.name)),
                isChecked()
            )
        ).check(doesNotExist())
    }

    @Test
    fun multi_items_match_should_be_highlighted() {
        val total = records.take(3).map { it.total }.sum()
        launchWithTotal(total)

        records.take(3).forEach {
            onView(
                allOf(
                    withHighLighted(true),
                    hasDescendant(withText(containsString(it.paidTo))),
                )
            ).check(matches(isDisplayed()))
        }
    }

    @Test
    fun selected_items_should_not_be_highlighted() {
        val total = records.take(3).map { it.total }.sum()
        launchWithTotal(total)

        onView(withId(R.id.recycler_view))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        onView(
            allOf(
                withHighLighted(false),
                hasDescendant(withText(containsString(records.first().paidTo))),
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun scroll_to_the_auto_selected_item() {
        // Match and auto-select the last item.
        // Ensure the view scrolls to display the last item.
        launchWithTotal(records.last().total)

        onView(
            withText(containsString(records.last().paidTo)),
        ).check(matches(isDisplayed()))
    }

    private fun launchWithTotal(total: Float) {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val intent = Intent(appContext, FindMatchActivity::class.java).apply {
            putExtra(FindMatchActivity.TARGET_MATCH_VALUE, total.toFloat())
        }

        ActivityScenario.launch<FindMatchActivity>(intent)
    }

    companion object {
        fun withHighLighted(highLighted: Boolean): BoundedMatcher<View?, CheckedListItem> {
            return object : BoundedMatcher<View?, CheckedListItem>(CheckedListItem::class.java) {
                public override fun matchesSafely(item: CheckedListItem): Boolean {
                    return highLighted == item.highlight
                }

                override fun describeTo(description: Description) {
                    description.appendText("with highlighted: $highLighted")
                }
            }
        }
    }
}