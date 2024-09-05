package com.ismailmesutmujde.kotlinartbook.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.ismailmesutmujde.kotlinartbook.databinding.ActivityArtBinding

class ArtActivity : AppCompatActivity() {

    private lateinit var  bindingArtActivity : ActivityArtBinding

    private lateinit var  activityResultLauncher : ActivityResultLauncher<Intent>
    private lateinit var  permissionLauncher : ActivityResultLauncher<String>

    var selectedBitmap : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingArtActivity = ActivityArtBinding.inflate(layoutInflater)
        val view = bindingArtActivity.root
        setContentView(view)

        registerLauncher()
    }

    fun saveButtonClicked(view : View) {

    }

    fun selectImage(view : View) {

        //
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ) {
            // Android 33+ -> READ_MEDIA_IMAGES
            // explicitly requesting permission from the user (kullanıcıdan açıkça izin istemek)
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                // permission denied (izin reddedildi)

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_MEDIA_IMAGES)) {
                    // rationale
                    Snackbar.make(view, "Permission needed for gallery!", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", View.OnClickListener {
                        // request permission
                        permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                    }).show()
                } else {
                    // request permission
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                }

            } else {
                // permission granted (izin verildi)
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }
        } else {
            // Android 32- -> READ_EXTERNAL_STORAGE
            // explicitly requesting permission from the user (kullanıcıdan açıkça izin istemek)
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // permission denied (izin reddedildi)

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // rationale
                    Snackbar.make(view, "Permission needed for gallery!", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", View.OnClickListener {
                        // request permission
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }).show()
                } else {
                    // request permission
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }

            } else {
                // permission granted (izin verildi)
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }
        }
    }

    private fun registerLauncher() {

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ActivityResultCallback { result ->
            if (result.resultCode == RESULT_OK) {
                val intentFromResult = result.data

                if(intentFromResult != null) {
                    val imageData = intentFromResult.data
                    //bindingArtActivity.imageView.setImageURI(imageData)

                    if (imageData != null) {
                        try {
                            if (Build.VERSION.SDK_INT >= 28) {
                                val source = ImageDecoder.createSource(this@ArtActivity.contentResolver, imageData)
                                selectedBitmap = ImageDecoder.decodeBitmap(source)
                                bindingArtActivity.imageView.setImageBitmap(selectedBitmap)
                            } else {
                                selectedBitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageData)
                                bindingArtActivity.imageView.setImageBitmap(selectedBitmap)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        })

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if(result) {
                // permission granted
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            } else {
                // permission denied
                Toast.makeText(this@ArtActivity, "Permission needed!", Toast.LENGTH_LONG).show()
            }
        }

    }


}