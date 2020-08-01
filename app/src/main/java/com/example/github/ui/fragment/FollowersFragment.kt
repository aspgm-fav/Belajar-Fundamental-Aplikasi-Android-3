package com.example.github.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.github.R
import com.example.github.ui.adapter.UserAdapter
import com.example.github.viewmodel.FollowersViewModel
import kotlinx.android.synthetic.main.fragment_followers.*

class FollowersFragment : Fragment() {

    companion object{
        const val EXTRA_FOLLOWERS = "followers_name"
    }

    private lateinit var mAdapter: UserAdapter
    private lateinit var followersViewModel: FollowersViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading(true)

        initializeAdapter()
        initializeRecyclerView()
        initializeViewModel()
    }

    private fun initializeAdapter() {
        mAdapter = UserAdapter()
        mAdapter.notifyDataSetChanged()
    }

    private fun initializeRecyclerView() {
        rvFollowers.layoutManager = LinearLayoutManager(context)
        rvFollowers.adapter = mAdapter
    }

    private fun initializeViewModel() {
        followersViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowersViewModel::class.java)

        if (arguments != null) {
            val username = arguments?.getString(EXTRA_FOLLOWERS)
            followersViewModel.setFollowers(username.toString())
        }

        followersViewModel.getFollowers().observe(viewLifecycleOwner, Observer { usermodel ->
            if (usermodel != null) {
                mAdapter.setData(usermodel)
                showLoading(false)
                if (mAdapter.itemCount == 0) {
                    tvNothingFollowers.visibility = View.VISIBLE
                }
                else rvFollowers.visibility = View.VISIBLE
            }
        })
    }

    private fun showLoading(state: Boolean) {
        when (state) {
            true -> {
                pbLoading.visibility = View.VISIBLE
                tvLoading.visibility = View.VISIBLE
                rvFollowers.visibility = View.GONE
            }
            false -> {
                pbLoading.visibility = View.GONE
                tvLoading.visibility = View.GONE
            }
        }
    }
}
