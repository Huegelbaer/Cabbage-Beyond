package com.cabbagebeyond

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlin.reflect.KProperty1

/**
 * Chip group with dynamically added  chips.
 * To define the chip group call the function prepareChipGroup.
 */
class FilterChipGroup constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    LinearLayout(context, attrs, defStyleAttr) {

    var title: String = ""
        set(value) {
            findViewById<TextView>(R.id.filter_label).text = value
            field = value
        }

    init {
        init(attrs)
        orientation = VERTICAL
    }

    private fun init(attrs: AttributeSet?) {
        View.inflate(context, R.layout.view_filter_chip_group, this)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.FilterChipGroup)
        try {
            title = typedArray.getString(R.styleable.FilterChipGroup_title) ?: ""
        } finally {
            typedArray.recycle()
        }
    }

    /**
     * Generate a chip group with the given values.
     *
     * @param values: filterable values
     * @param selected: preselected value (optional)
     * @param titleProperty: operation to get the represented titles of the values
     * @param onSelect: action, which is called when an item is selected
     */
    fun <T : Any> prepareChipGroup(
        values: List<T>,
        selected: T?,
        titleProperty: KProperty1<T, String>,
        onSelect: (item: T?) -> Unit
    ): ChipGroup {
        val chipGroup = findViewById<ChipGroup>(R.id.filter_chip_group)

        values.forEachIndexed { index, it ->
            val title = titleProperty.get(it)
            val chip = createChip(title, index)
            chipGroup.addView(chip)
        }
        chipGroup.setOnCheckedChangeListener { _, checkedId ->
            val item = values.getOrNull(checkedId)
            onSelect(item)
        }
        selected?.let {
            val index = values.indexOf(it)
            chipGroup.check(index)
        }
        return chipGroup
    }

    private fun createChip(title: String, index: Int): Chip {
        return (inflate(context, R.layout.view_filter_chip, null) as Chip).apply {
            id = index
            tag = index
            text = title
        }
    }
}