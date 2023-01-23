package com.cabbagebeyond.ui.collection

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import com.cabbagebeyond.R

abstract class CollectionListFragment : Fragment() {

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
            R.id.app_bar_filter_list -> {
                viewModel.onSelectFilter()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    open fun setupViewModelObservers() {
        viewModel.emptyListState.observe(viewLifecycleOwner) {
            it?.let {
                showEmptyState(it.title, it.message, it.button, it.action)
            } ?: run {
                showList()
            }
        }
    }

    abstract fun showEmptyState(title: String, message: String, buttonTitle: String?, action: (() -> Unit)?)

    abstract fun showList()
}