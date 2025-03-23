package co.nz.tsb.interview.bankrecmatchmaker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.nz.tsb.interview.bankrecmatchmaker.core.MatchItem
import co.nz.tsb.interview.bankrecmatchmaker.core.Matcher
import co.nz.tsb.interview.bankrecmatchmaker.core.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
internal class MatchViewModel @Inject constructor(
    private val repository: Repository,
    private val computingDispatcher: CoroutineDispatcher
) : ViewModel() {

    // LiveData is used because the activity is based in Java.
    private val _liveRemain = MutableLiveData<Double>()
    val liveRemain : LiveData<Double> = _liveRemain

    private val _liveSelection = MutableLiveData<Set<Int>>()
    val liveSelection : LiveData<Set<Int>> = _liveSelection

    private val _liveHints = MutableLiveData<Set<Int>>()
    val liveHints : LiveData<Set<Int>> = _liveHints

    private val _liveRecords = MutableLiveData<List<MatchItem>>()
    val liveRecords : LiveData<List<MatchItem>> = _liveRecords

    private val _liveAutoSelection = MutableLiveData<Int>()
    val liveAutoSelection : LiveData<Int> = _liveAutoSelection

    private var total: Double = 0.0
    private val selectedRecords: MutableSet<Int> = mutableSetOf()

    private lateinit var records: List<MatchItem>

    fun init(total: Double) {
        this.total = total
        selectedRecords.clear()

        viewModelScope.launch {
            records = repository.getRecords()
            _liveRecords.value = records
            _liveSelection.value = selectedRecords.toSet()

            calculateHints()
        }
    }

    fun selectItem(idx: Int, selected: Boolean) {
        if(selected && selectedRecords.contains(idx)) return
        if(!selected && !selectedRecords.contains(idx)) return

        if (selected) {
            selectedRecords.add(idx)
        } else {
            selectedRecords.remove(idx)
        }

        _liveSelection.value = selectedRecords.toSet()

        calculateHints(false)
    }

    private fun calculateHints(autoSelect: Boolean = true) {
        val remain = total - selectedRecords.map { records[it].total }.sum()

        _liveRemain.value = remain
        _liveHints.value = setOf()

        // no hints when remain reaches 0
        if(remain < 0.0001) {
            return
        }

        val matcher = Matcher(remain, records.mapIndexed { index, matchItem ->
            if(selectedRecords.contains(index)) {
                null // Selected items are excluded from hint calculations.
            } else {
                matchItem.total.toDouble()
            }
        })

        matcher.firstMatch()?.let {
            if(autoSelect) {
                selectItem(it, true)
                _liveAutoSelection.value = it
            } else {
                _liveHints.value = setOf(it)
            }
            return
        }

        // Copy the selected set before switching to backend threads.
        val selected = selectedRecords.toSet()
        viewModelScope.launch(computingDispatcher) {
            matcher.multiMatch(3).let { hints ->
                if (hints.isNotEmpty()) {
                    withContext(Dispatchers.Main) {
                        _liveHints.value = hints.toSet() - selected
                    }
                }
            }
        }
    }
}
