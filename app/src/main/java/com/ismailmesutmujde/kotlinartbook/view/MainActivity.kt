package com.ismailmesutmujde.kotlinartbook.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ismailmesutmujde.kotlinartbook.R
import com.ismailmesutmujde.kotlinartbook.adapter.ArtAdapter
import com.ismailmesutmujde.kotlinartbook.databinding.ActivityMainBinding
import com.ismailmesutmujde.kotlinartbook.model.Art


class MainActivity : AppCompatActivity() {

    private lateinit var bindingMainActivity: ActivityMainBinding
    private lateinit var artList : ArrayList<Art>
    private lateinit var artAdapter : ArtAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingMainActivity = ActivityMainBinding.inflate(layoutInflater)
        val view = bindingMainActivity.root
        setContentView(view)

        artList = ArrayList<Art>()
        artAdapter = ArtAdapter(artList)
        bindingMainActivity.recyclerView.layoutManager = LinearLayoutManager(this)
        bindingMainActivity.recyclerView.adapter = artAdapter

        try {

            val database = this.openOrCreateDatabase("Arts", MODE_PRIVATE, null)

            val cursor = database.rawQuery("SELECT * FROM arts", null)
            val artNameIx = cursor.getColumnIndex("artname")
            val idIx = cursor.getColumnIndex("id")

            while (cursor.moveToNext()) {
                val name = cursor.getString(artNameIx)
                val id = cursor.getInt(idIx)
                val art = Art(name, id)
                artList.add(art)
            }

            artAdapter.notifyDataSetChanged()

            cursor.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }
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
            intent.putExtra("info","new")
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }

}