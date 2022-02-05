package com.cabbagebeyond.ui

import androidx.databinding.InverseMethod

object DoubleConverter {

    @JvmStatic
    @InverseMethod("toDouble")
    fun toString(value: Double): String {
        return value.toString()
    }

    @JvmStatic
    fun toDouble(value: String): Double {
        return value.toDouble()
    }
}

object IntegerConverter {

    @JvmStatic
    @InverseMethod("toInt")
    fun toString(value: Int): String {
        return value.toString()
    }

    @JvmStatic
    fun toInt(value: String): Int {
        return value.toInt()
    }
}