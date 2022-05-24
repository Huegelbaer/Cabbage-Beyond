package com.cabbagebeyond

class EmptyListStateModel(
    var title: String,
    var message: String,
    var buttonTitle: String?,
    private var buttonAction: (() -> Unit)?
) {

    fun onClick() {
        buttonAction?.let { it() }
    }
}