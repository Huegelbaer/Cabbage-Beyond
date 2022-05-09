package com.cabbagebeyond

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.core.view.setPadding
import androidx.fragment.app.DialogFragment
import kotlin.reflect.KProperty1

/**
 * TODO: document your custom view class.
 */
class FilterDialogFragment(private val onFilter: () -> Unit) : DialogFragment() {

    private data class FilterData<T : Any>(
        var values: List<T>,
        var selected: T?,
        var title: KProperty1<T, String>,
        var onSelect: (item: T?) -> Unit
    )

    private var _filterData = mutableListOf<Pair<String, FilterData<*>>>()

    private lateinit var linearLayout: LinearLayout

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            linearLayout = LinearLayout(context).apply {
                this.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                this.orientation = LinearLayout.VERTICAL
                this.setPadding(resources.getDimension(R.dimen.default_margin).toInt())
            }

            val scrollView = ScrollView(context)
            scrollView.addView(linearLayout)

            _filterData.forEach { (title, filterData) ->
                setupFilterChipGroup(title, filterData)
            }

            AlertDialog.Builder(it)
                .setView(scrollView)
                .setPositiveButton(R.string.menu_filter) { _, _ ->
                    onFilter()
                }
                .setNegativeButton(R.string.dialog_button_cancel) { _, _ -> }
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun <T : Any> addFilterChipGroup(
        title: String,
        values: List<T>,
        selected: T?,
        titleProperty: KProperty1<T, String>,
        onSelect: (item: T?) -> Unit
    ) {
        val data = FilterData(values, selected, titleProperty, onSelect)
        _filterData.add(Pair(title, data))
    }

    private fun <T : Any> setupFilterChipGroup(title: String, data: FilterData<T>) {
        FilterChipGroup(requireContext()).apply {
            this.title = title
            this.prepareChipGroup(data.values, data.selected, data.title, data.onSelect)
            linearLayout.addView(this)
        }
    }
}