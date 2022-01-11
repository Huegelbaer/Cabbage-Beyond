package com.cabbagebeyond.ui

import android.widget.ImageButton
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.cabbagebeyond.model.Role


@BindingAdapter("roleList")
fun bindTextViewToRoleList(textView: TextView, roles: List<Role>) {
    textView.text = roles.joinToString("\n") { it.name }
}

@BindingAdapter("descriptionContent", "descriptionParameter")
fun bindContentDescription(button: ImageButton, content: String, parameter: String) {
    button.contentDescription = String.format(content, parameter)
}