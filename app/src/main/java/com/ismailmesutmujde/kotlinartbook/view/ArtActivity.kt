package com.ismailmesutmujde.kotlinartbook.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
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

        // explicitly requesting permission from the user (kullanıcıdan açıkça izin istemek)
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // permission denied (izin reddedildi)

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // rationale
                Snackbar.make(view, "Permission needed for gallery!", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", View.OnClickListener {
                    // request permission

                }).show()
            } else {
                // request permission

            }

        } else {
            // permission granted (izin verildi)
            val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            // intent
        }
    }

    fun saveButtonClicked(view : View) {

    }
}