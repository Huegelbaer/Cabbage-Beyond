package com.cabbagebeyond.ui.ocr

import android.view.ActionMode
import android.view.Menu

import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import com.cabbagebeyond.R


internal class AddToPropertyActionCallback(
    private val _bodyView: TextView,
    private val _listener: ((String) -> Unit)
) : ActionMode.Callback {

    override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
        val inflater: MenuInflater = mode.menuInflater
        inflater.inflate(R.menu.text_recognition_selection, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return false
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.text_selection -> {
                _listener(extractSelection())
                return true
            }
        }
        return false
    }

    override fun onDestroyActionMode(mode: ActionMode?) {}

    private fun extractSelection(): String {
        return _bodyView.text.substring(_bodyView.selectionStart, _bodyView.selectionEnd)
    }
}