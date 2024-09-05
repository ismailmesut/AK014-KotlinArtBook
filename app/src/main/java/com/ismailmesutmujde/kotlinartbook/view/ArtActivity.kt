package com.ismailmesutmujde.kotlinartbook.view

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ismailmesutmujde.kotlinartbook.databinding.ActivityArtBinding

class ArtActivity : AppCompatActivity() {

    private lateinit var  bindingArtActivity : ActivityArtBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingArtActivity = ActivityArtBinding.inflate(layoutInflater)
        val view = bindingArtActivity.root
        setContentView(view)


    }

    fun selectImage(view : View) {

    }

    fun saveButtonClicked(view : View) {

    }
}