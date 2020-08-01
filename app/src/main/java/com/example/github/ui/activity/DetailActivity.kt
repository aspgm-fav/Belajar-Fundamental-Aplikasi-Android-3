package com.example.github.ui.activity

import android.annotation.SuppressLint
import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.github.R
import com.example.github.data.database.DatabaseContract
import com.example.github.data.database.DatabaseContract.CONTENT_URI
import com.example.github.data.model.User
import com.example.github.ui.adapter.PagerAdaper
import com.example.github.ui.fragment.FollowersFragment
import com.example.github.ui.fragment.FollowingFragment
import com.example.github.util.helper.MappingHelper
import com.example.github.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity(), View.OnClickListener {

    private var isFavorite = false
    private var userItem: User? = null
    private var fromFavorite: String? = null
    private var fromMainAcitivity: String? = null
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var uriWithId: Uri

    companion object {
        const val EXTRA_STATE = "extra_state"
        const val EXTRA_FAV = "extra_fav"
        const val EXTRA_MAIN = "extra_main"
    }

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        fromFavorite = intent.getStringExtra(EXTRA_FAV)
        fromMainAcitivity = intent.getStringExtra(EXTRA_MAIN)

        userItem = intent.getParcelableExtra(EXTRA_STATE) as User

        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + userItem?.id)

        val dataGitFav = contentResolver?.query(uriWithId, null, null, null, null)
        val dataGitObject = MappingHelper.mapCursorToArrayList(dataGitFav)
        for (data in dataGitObject) {
            if (this.userItem?.login == data.login) {
                isFavorite = true
                btnFavorite.setBackgroundColor(R.color.colorPrimary)
                btnFavorite.text = resources.getString(R.string.btn_remove_favorite)
            }
        }

        setFollowerFollowing(userItem!!)

        initializeData()
        title = userItem?.login

        btnFavorite.setOnClickListener(this)
    }

    private fun initializeData() {
        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)
        detailViewModel.setDetailUser(userItem?.login.toString())
        detailViewModel.getDetailData().observe(this, Observer { user ->
            if (user != null) {
                tvName.text = user.name

                if (user.company == "null") {
                    lineCompany.visibility = View.GONE
                } else {
                    tvCompany.text = user.company
                }

                if (user.location == "null") {
                    lineLocation.visibility = View.GONE
                } else {
                    tvLocation.text = user.location
                }

                if
                        (user.blog != null) {
                    lineBlog.visibility = View.GONE
                } else {
                    tvBlog.text = user.blog
                }

                tvRepository.text = user.repository.toString()
                tvFollowers.text = user.follower.toString()
                tvFollowing.text = user.following.toString()
            }
        })

        Glide.with(this)
            .load(userItem?.avatar)
            .apply(RequestOptions().override(90, 90))
            .into(civAvatar)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnFavorite ->
                setFavorite()
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun setIconFavorite() {
        if (isFavorite) {
            btnFavorite.setBackgroundColor(R.color.colorLight)
            btnFavorite.text = getString(R.string.btn_remove_favorite)
        } else {
            btnFavorite.setBackgroundColor(R.color.colorPrimary)
            btnFavorite.text = resources.getString(R.string.btn_add_favorite)
        }
    }

    private fun setFavorite() {
        if (isFavorite) {
            userItem?.let {
                contentResolver.delete(uriWithId, null, null)
                Toast.makeText(this, userItem?.login + this.getString(R.string.remove_from_favorite), Toast.LENGTH_SHORT).show()
                isFavorite = false
                setIconFavorite()
            }
        }else {
            val values = ContentValues()
            values.put(DatabaseContract.ColumnFavorite.LOGIN_NAME, userItem?.login)
            values.put(DatabaseContract.ColumnFavorite.AVATAR, userItem?.avatar)
            contentResolver.insert(CONTENT_URI, values)
            userItem?.login
            Toast.makeText(this, userItem?.login + this.getString(R.string.add_to_favorite), Toast.LENGTH_SHORT).show()
            isFavorite = true
            setIconFavorite()
        }
    }

    private fun setFollowerFollowing(data: User) {
        val sectionsPagerAdaper =
            PagerAdaper(
                this,
                supportFragmentManager
            )
        sectionsPagerAdaper.setData(data.login.toString())
        vpPager.adapter = sectionsPagerAdaper
        tabLayout.setupWithViewPager(vpPager)
        supportActionBar?.elevation = 0f

        val bundle = Bundle()
        val followerFragment =
            FollowersFragment()
        bundle.putString(FollowersFragment.EXTRA_FOLLOWERS, data.login)
        followerFragment.arguments = bundle
        val followingFragment =
            FollowingFragment()
        bundle.putString(FollowingFragment.EXTRA_FOLLOWING, data.login)
        followingFragment.arguments = bundle
    }
}
