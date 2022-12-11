package com.kupriyanov.compositionapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kupriyanov.compositionapp.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.main_container, GameFinishedFragment()).commit()
    }
}