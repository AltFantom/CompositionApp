package com.kupriyanov.compositionapp.presentation

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.kupriyanov.compositionapp.R

@BindingAdapter("requiresAnswers")
fun bindRequiredAnswers(textView: TextView, count: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.required_score),
        count
    )
}

@BindingAdapter("scoreAnswers")
fun bindScoreAnswers(textView: TextView, count: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.score_answers),
        count
    )
}

@BindingAdapter("requiredPercentage")
fun bindRequiredPercentage(textView: TextView, count: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.required_percentage),
        count
    )
}

@BindingAdapter("scorePercentage")
fun bindScorePercentage(textView: TextView, count: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.score_percentage),
        count
    )
}

@BindingAdapter("emojiResult")
fun bindEmojiResult(view: ImageView, winner: Boolean) {
    val emojiResId = if (winner) {
        R.drawable.ic_smile
    } else {
        R.drawable.ic_sad
    }
    Glide.with(view.context).load(emojiResId).into(view)
}