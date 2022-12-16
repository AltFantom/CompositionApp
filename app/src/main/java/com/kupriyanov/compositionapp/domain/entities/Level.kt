package com.kupriyanov.compositionapp.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class Level : Parcelable {
    EASY, NORMAL, HARD, VERY_HARD
}