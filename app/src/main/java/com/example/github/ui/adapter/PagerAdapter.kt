package com.example.github.ui.adapter

import android.content.Context
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.github.ui.fragment.FollowersFragment
import com.example.github.ui.fragment.FollowingFragment
import com.example.github.R
import com.example.github.ui.fragment.RepositoryFragment

class PagerAdaper(private val mContext: Context, fm: FragmentManager): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var username: String? = "username"

    @StringRes
    private val ints = intArrayOf(
        R.string.tab_repository,
        R.string.tab_followers,
        R.string.tab_following
    )

    override fun getItem(position: Int): Fragment {
        val fragment: Fragment?
        when (position) {
            0 -> {
                fragment =
                    RepositoryFragment()
                val bundle = Bundle()
                bundle.putString(RepositoryFragment.EXTRA_REPOSITORY, getData())
                fragment.arguments = bundle
            }

            1 -> {
                fragment =
                    FollowersFragment()
                val bundle = Bundle()
                bundle.putString(FollowersFragment.EXTRA_FOLLOWERS, getData())
                fragment.arguments = bundle
            }

            else -> {
                fragment =
                    FollowingFragment()
                val bundle = Bundle()
                bundle.putString(FollowingFragment.EXTRA_FOLLOWING, getData())
                fragment.arguments = bundle
            }
        }
        return fragment
    }

    fun setData(loginName: String){
        username = loginName
    }

    private fun getData(): String? {
        return username
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(ints[position])
    }
    override fun getCount(): Int {
        return 3
    }

}