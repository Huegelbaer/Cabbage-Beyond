package com.cabbagebeyond.ui

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.cabbagebeyond.model.Role


@BindingAdapter("roleList")
fun bindTextViewToRoleList(textView: TextView, roles: List<Role>) {
    val n =  roles.joinToString("\n") { it.name }
    textView.text = n
}