package com.ismailmesutmujde.kotlinartbook.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.ismailmesutmujde.kotlinartbook.R
import com.ismailmesutmujde.kotlinartbook.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var bindingMainActivity: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingMainActivity = ActivityMainBinding.inflate(layoutInflater)
        val view = bindingMainActivity.root
        setContentView(view)
    }

    // onCreateOptionsMenu : options menüyü, Main Activity'e bağlama işlemi yapılır.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        // inflater
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.art_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // onOptionsItemSelected : options menüye tıklanırsa hangi işlemin yapılacağı burada yapılır.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add_art_item) {
            val intent = Intent(this@MainActivity, ArtActivity::class.java)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }

}