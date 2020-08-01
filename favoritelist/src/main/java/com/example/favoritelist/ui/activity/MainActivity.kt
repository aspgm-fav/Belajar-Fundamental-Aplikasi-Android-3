package com.example.favoritelist.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favoritelist.R
import com.example.favoritelist.data.database.DatabaseContract.CONTENT_URI
import com.example.favoritelist.ui.adapter.FavoriteAdapter
import com.example.favoritelist.util.helper.MappingHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeToolbar()
        adapter = FavoriteAdapter()

        GlobalScope.launch(Dispatchers.Main) {
            val deferredGit = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }

            val fav = deferredGit.await()
            if (fav.size > 0) {
                adapter.listFavorite = fav
                ivFavorite.visibility = View.GONE
                tvFavorite.visibility = View.GONE
            } else {
                adapter.listFavorite = ArrayList()
                ivFavorite.visibility = View.VISIBLE
                tvFavorite.visibility = View.VISIBLE
            }
        }

        showRecyclerView()

        title = applicationContext.resources.getString(R.string.app_name)
    }

    private fun initializeToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolBar)
        setSupportActionBar(toolbar)
    }

    private fun showRecyclerView() {
        rvFavorite.adapter = adapter
        rvFavorite.layoutManager = LinearLayoutManager(this)
        rvFavorite.setHasFixedSize(true)
    }
}