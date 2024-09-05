package com.ismailmesutmujde.kotlinartbook.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ismailmesutmujde.kotlinartbook.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var bindingMainActivity: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingMainActivity = ActivityMainBinding.inflate(layoutInflater)
        val view = bindingMainActivity.root
        setContentView(view)

    }
}