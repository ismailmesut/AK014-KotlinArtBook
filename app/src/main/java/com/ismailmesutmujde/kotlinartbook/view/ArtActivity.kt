package com.ismailmesutmujde.kotlinartbook.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.ismailmesutmujde.kotlinartbook.R
import com.ismailmesutmujde.kotlinartbook.databinding.ActivityArtBinding
import java.io.ByteArrayOutputStream

class ArtActivity : AppCompatActivity() {

    private lateinit var  bindingArtActivity : ActivityArtBinding

    private lateinit var  activityResultLauncher : ActivityResultLauncher<Intent>
    private lateinit var  permissionLauncher : ActivityResultLauncher<String>

    var selectedBitmap : Bitmap? = null

    private lateinit var database : SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingArtActivity = ActivityArtBinding.inflate(layoutInflater)
        val view = bindingArtActivity.root
        setContentView(view)

        database = this.openOrCreateDatabase("Arts", MODE_PRIVATE, null)

        registerLauncher()

        val intent = intent
        val info = intent.getStringExtra("info")

        if  (info.equals("new")) {
            // yeni item'ı gösterir
            bindingArtActivity.artNameText.setText("")
            bindingArtActivity.artistNameText.setText("")
            bindingArtActivity.yearText.setText("")
            bindingArtActivity.imageView.setImageResource(R.drawable.selectimage)
            bindingArtActivity.saveButton.visibility = View.VISIBLE
        } else {
            // eski item'ı gösterir
            bindingArtActivity.saveButton.visibility = View.INVISIBLE
            val selectedId = intent.getIntExtra("id", 1)
            val cursor = database.rawQuery("SELECT * FROM arts WHERE id = ?", arrayOf(selectedId.toString()))
            val artNameIx = cursor.getColumnIndex("artname")
            val artistNameIx = cursor.getColumnIndex("artistname")
            val yearIx = cursor.getColumnIndex("year")
            val imageIx = cursor.getColumnIndex("image")

            while (cursor.moveToNext()) {
                bindingArtActivity.artNameText.setText(cursor.getString(artNameIx))
                bindingArtActivity.artistNameText.setText(cursor.getString(artistNameIx))
                bindingArtActivity.yearText.setText(cursor.getString(yearIx))

                val byteArray = cursor.getBlob(imageIx)
                val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                bindingArtActivity.imageView.setImageBitmap(bitmap)

            }
            cursor.close()
        }
    }

    fun saveButtonClicked(view : View) {

        val artName = bindingArtActivity.artNameText.text.toString()
        val artistName = bindingArtActivity.artistNameText.text.toString()
        val year = bindingArtActivity.yearText.text.toString()

        if (selectedBitmap != null) {
            val smallBitmap = makeSmallerBitmap(selectedBitmap!!, 300)

            // bir görseli veriye çevirmek
            val outputStream = ByteArrayOutputStream()
            smallBitmap.compress(Bitmap.CompressFormat.PNG, 50, outputStream)
            val byteArray = outputStream.toByteArray()

            try {
                // create database
                //val database = this.openOrCreateDatabase("Arts", MODE_PRIVATE, null)
                // create table
                database.execSQL("CREATE TABLE IF NOT EXISTS arts (id INTEGER PRIMARY KEY, artname VARCHAR, artistname VARCHAR, year VARCHAR, image BLOB)")

                val sqlString = "INSERT INTO arts (artname, artistname, year, image) VALUES (?, ?, ?, ?)"
                val statement = database.compileStatement(sqlString)
                statement.bindString(1, artName)
                statement.bindString(2, artistName)
                statement.bindString(3, year)
                statement.bindBlob(4, byteArray)
                statement.execute()


            }catch (e: Exception) {
                e.printStackTrace()
            }

            val intent = Intent(this@ArtActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)

        }

    }

    private fun makeSmallerBitmap (image : Bitmap,maximumSize : Int) : Bitmap {
        var width = image.width
        var height = image.height

        var bitmapRatio : Double = width.toDouble() / height.toDouble()

        if (bitmapRatio > 1) {
            // landscape (yatay)
            width = maximumSize
            val scaledHeight = width / bitmapRatio
            height = scaledHeight.toInt()
        } else {
            // portrait (dikey)
            height = maximumSize
            val scaledWidth = height * bitmapRatio
            width = scaledWidth.toInt()
        }

        return Bitmap.createScaledBitmap(image,100,100, true)
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