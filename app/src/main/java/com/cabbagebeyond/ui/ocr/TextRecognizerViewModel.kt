package com.cabbagebeyond.ui.ocr

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class TextRecognizerViewModel(): ViewModel() {

    enum class State {
        RESET, RUNNING, FINISHED
    }

    private var _state = MutableLiveData(State.RESET)
    val state: LiveData<State>
        get() = _state

    private var _result = MutableLiveData<String>()
    val result: LiveData<String>
        get() = _result

    private var _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable>
        get() = _error

    fun startTextRecognition(context: Context, imageUri: Uri) {
        _state.value = State.RUNNING

        val image = InputImage.fromFilePath(context, imageUri)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        recognizer.process(image)
            .addOnSuccessListener { texts ->
                _state.value = State.FINISHED
                _result.value = texts.text
            }
            .addOnFailureListener { e -> // Task failed with an exception
                _state.value = State.RESET
                _error.value = e
            }
    }
}