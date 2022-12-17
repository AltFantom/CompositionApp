package com.kupriyanov.compositionapp.presentation

import android.content.Context
import android.content.res.ColorStateList
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter

interface OnOptionClickListener {
    fun onOptionClick(number: Int)
}

@BindingAdapter("sum")
fun bindSum(textView: TextView, sum: Int) {
    textView.text = sum.toString()
}

@BindingAdapter("visibleNumber")
fun bindVisibleNumber(textView: TextView, visibleNumber: Int) {
    textView.text = visibleNumber.toString()
}

@BindingAdapter("enoughCount")
fun bindEnoughCount(textView: TextView, enoughCount: Boolean) {
    textView.setTextColor(getColorByState(textView.context, enoughCount))
}

@BindingAdapter("enoughPercent")
fun bindEnoughPercent(progressBar: ProgressBar, enoughPercent: Boolean) {
    progressBar.progressTintList = ColorStateList.valueOf(
        getColorByState(
            progressBar.context,
            enoughPercent
        )
    )
}

@BindingAdapter("percentOfRightAnswers")
fun bindPercentOfRightAnswers(progressBar: ProgressBar, percent: Int) {
    progressBar.setProgress(percent, true)
}

@BindingAdapter("onOptionClickListener")
fun bindOnOptionClickListener(textView: TextView, clickListener: OnOptionClickListener) {
    textView.setOnClickListener {
        clickListener.onOptionClick(textView.text.toString().toInt())
    }
}

private fun getColorByState(context: Context, goodState: Boolean): Int {
    val colorResId = if (goodState) {
        android.R.color.holo_green_light
    } else {
        android.R.color.holo_red_light
    }
    return ContextCompat.getColor(context, colorResId)
}
