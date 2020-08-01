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
import com.example.github.viewmodel.FollowingViewModel
import kotlinx.android.synthetic.main.fragment_following.*

class FollowingFragment : Fragment() {

    companion object {
        const val EXTRA_FOLLOWING = "extra_following"
    }

    private lateinit var mAdapter: UserAdapter
    private lateinit var followingViewModel: FollowingViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_following, container, false)
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
        rvFollowing.layoutManager = LinearLayoutManager(context)
        rvFollowing.adapter = mAdapter
    }

    private fun initializeViewModel() {
        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowingViewModel::class.java)

        if (arguments != null) {
            val username = arguments?.getString(EXTRA_FOLLOWING)
            followingViewModel.setFollowing(username)
        }

        followingViewModel.getFollowing().observe(viewLifecycleOwner, Observer { usermodel ->
            if (usermodel != null){
                mAdapter.setData(usermodel)
                showLoading(false)
                if (mAdapter.itemCount == 0) {
                    tvNothingFollowing.visibility = View.VISIBLE
                }
                else rvFollowing.visibility = View.VISIBLE
            }
        })
    }

    private fun showLoading(state: Boolean) {
        when (state) {
            true -> {
                pbLoading.visibility = View.VISIBLE
                tvLoading.visibility = View.VISIBLE
                rvFollowing.visibility = View.GONE
            }
            false -> {
                pbLoading.visibility = View.GONE
                tvLoading.visibility = View.GONE
            }
        }
    }
}
