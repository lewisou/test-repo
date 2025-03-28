package co.nz.tsb.interview.bankrecmatchmaker.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Checkable
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatCheckBox
import co.nz.tsb.interview.bankrecmatchmaker.R

class CheckedListItem : LinearLayout, Checkable {
    private var checkBox: AppCompatCheckBox? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        val layoutInflater = LayoutInflater.from(context)
        orientation = HORIZONTAL
        checkBox = layoutInflater.inflate(R.layout.list_item_checkbox, this, false) as AppCompatCheckBox
        addView(checkBox, 0)
    }

    override fun setChecked(checked: Boolean) {
        checkBox!!.isChecked = checked
    }

    override fun isChecked(): Boolean {
        return checkBox!!.isChecked
    }

    override fun toggle() {
        checkBox!!.toggle()
    }

    var highlight : Boolean = false
        set(value) {
            field = value
            if (value) {
                setBackgroundColor(Color.CYAN)
            } else {
                setBackgroundColor(Color.WHITE)
            }
        }
}