package com.example.github.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.github.R
import com.example.github.ui.adapter.FavoriteAdapter
import com.example.github.data.database.DatabaseContract.CONTENT_URI
import com.example.github.util.helper.MappingHelper
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
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

        title = applicationContext.resources.getString(R.string.favorite_title)
    }

    override fun onSupportNavigateUp(): Boolean {
        val intent = Intent(this, MainActivity::class.java)
        onBackPressed()
        startActivity(intent)
        return super.onSupportNavigateUp()
    }

    private fun showRecyclerView() {
        rvFavorite.adapter = adapter
        rvFavorite.layoutManager = LinearLayoutManager(this)
    }

}