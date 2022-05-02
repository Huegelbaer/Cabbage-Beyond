package com.cabbagebeyond.ui.collection

import android.app.SearchManager
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import com.cabbagebeyond.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

open class CollectionListFragment : Fragment() {

    protected open lateinit var viewModel: CollectionListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.collection_list, menu)

        // Associate searchable configuration with the SearchView
        val searchManager =
            requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager

        val queryTextListener: SearchView.OnQueryTextListener =
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                    //  val textView = findViewById(R.id.aa) as TextView
                    //textView.text = newText
                    if (newText.isEmpty()) {
                        viewModel.onSearchCanceled()
                    }
                    return true
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    viewModel.onSearch(query)
                    return true
                }
            }
        (menu.findItem(R.id.app_bar_search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
            setOnQueryTextListener(queryTextListener)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.app_bar_search -> {
                true
            }
            R.id.app_bar_sort_name -> {
                true
            }
            R.id.app_bar_sort_race -> {
                true
            }
            R.id.app_bar_sort_type -> {
                true
            }
            R.id.app_bar_sort_world -> {
                true
            }
            R.id.app_bar_filter_list -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun <T : Any> prepareChipGroup(
        chipGroup: ChipGroup,
        data: CollectionListViewModel.FilterData<T>
    ) {
        data.values.forEachIndexed { index, it ->
            val title = data.title.get(it)
            val chip = createChip(title, index)
            chipGroup.addView(chip)
        }
        data.selected?.let {
            val index = data.values.indexOf(it)
            chipGroup.check(index)
        }
    }

    private fun createChip(title: String, index: Int): Chip {
        return Chip(context).apply {
            id = index
            tag = index
            text = title
            isClickable = true
            isCheckable = true
            isCheckedIconVisible = true
            isFocusable = true
            chipBackgroundColor =
                ColorStateList.valueOf(resources.getColor(R.color.selector_chip_background))
        }
    }

}