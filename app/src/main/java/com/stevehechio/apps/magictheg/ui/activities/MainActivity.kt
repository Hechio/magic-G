package com.stevehechio.apps.magictheg.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.stevehechio.apps.magictheg.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}