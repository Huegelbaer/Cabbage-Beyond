package com.cabbagebeyond.ui

import android.widget.ImageButton
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.cabbagebeyond.model.Role
import android.graphics.drawable.Drawable
import android.widget.ImageView


@BindingAdapter("roleList")
fun bindTextViewToRoleList(textView: TextView, roles: List<Role>) {
    textView.text = roles.joinToString("\n") { it.name }
}

@BindingAdapter("textAsList")
fun bindTextViewToListOfStrings(textView: TextView, roles: List<String>) {
    textView.text = roles.joinToString("\n")
}

@BindingAdapter("descriptionContent", "descriptionParameter")
fun bindContentDescription(button: ImageButton, content: String, parameter: String) {
    button.contentDescription = String.format(content, parameter)
}

@BindingAdapter("srcCompat")
fun bindSrcCompat(imageView: ImageView, resource: Int) {
    imageView.setImageResource(resource)
}