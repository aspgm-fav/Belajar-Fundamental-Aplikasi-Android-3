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
import com.example.github.viewmodel.RepositoryViewModel
import kotlinx.android.synthetic.main.fragment_repository.*

class RepositoryFragment : Fragment() {

    companion object {
        const val EXTRA_REPOSITORY = "extra_repository"
    }

    private lateinit var mAdapter: UserAdapter
    private lateinit var repositoryViewModel: RepositoryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_repository, container, false)
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
        rvRepository.layoutManager = LinearLayoutManager(context)
        rvRepository.adapter = mAdapter
    }

    private fun initializeViewModel() {
        repositoryViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(RepositoryViewModel::class.java)

        if (arguments != null) {
            val username = arguments?.getString(EXTRA_REPOSITORY)
            repositoryViewModel.setRepository(username.toString())
        }

        repositoryViewModel.getRepository().observe(viewLifecycleOwner, Observer { usermodel ->
            if (usermodel != null){
                mAdapter.setData(usermodel)
                showLoading(false)
                if (mAdapter.itemCount == 0) {
                    tvNothingRepository.visibility = View.VISIBLE
                }
                else rvRepository.visibility = View.VISIBLE

            }
        })
    }

    private fun showLoading(state: Boolean) {
        when (state) {
            true -> {
                pbLoading.visibility = View.VISIBLE
                tvLoading.visibility = View.VISIBLE
                rvRepository.visibility = View.GONE
            }
            false -> {
                pbLoading.visibility = View.GONE
                tvLoading.visibility = View.GONE
            }
        }
    }
}
