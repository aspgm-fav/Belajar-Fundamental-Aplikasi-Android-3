package com.example.github.ui.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.github.R
import com.example.github.ui.adapter.UserAdapter
import com.example.github.data.model.User
import com.example.github.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mAdapter: UserAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeToolbar()
        initializeAdapter()
        intializeVieModel()
    }

    private fun initializeToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolBar)
        setSupportActionBar(toolbar)
    }

    private fun initializeAdapter() {
        mAdapter = UserAdapter()
        mAdapter.notifyDataSetChanged()
    }

    private fun intializeVieModel() {
        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
        mainViewModel.getGitSearch().observe(this, Observer { usermodel ->
            if (usermodel != null){
                mAdapter.setData(usermodel)
                showLoading(false)
                rvMain.visibility = View.VISIBLE
                tvSearch.visibility = View.GONE
                ivSearch.visibility = View.GONE
                showRecyclerView()
            }
        })
    }

    private fun showRecyclerView() {
        rvMain.layoutManager = LinearLayoutManager(this)
        rvMain.adapter = mAdapter

        mAdapter.setOnitemClickCallback(object : UserAdapter.OnitemClickCallback{
            override fun onItemClicked(data: User) {
                val intentMain = Intent(this@MainActivity, DetailActivity::class.java)
                intentMain.putExtra(DetailActivity.EXTRA_STATE, data)
                intentMain.putExtra(DetailActivity.EXTRA_MAIN, "mainactivity")
                startActivity(intentMain)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isEmpty()) {
                    return true
                } else {
                    showLoading(true)
                    mainViewModel.setSearchUserGit(query)
                }
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_favorite -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_setting -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return false
    }

    private fun showLoading(state: Boolean){
        when (state) {
            true -> {
                pbLoading.visibility = View.VISIBLE
                tvLoading . visibility = View . VISIBLE
                tvSearch.visibility = View.GONE
                ivSearch . visibility = View . GONE
            }
            false -> {
                pbLoading.visibility = View.GONE
                tvLoading.visibility = View.GONE
            }
        }
    }
}