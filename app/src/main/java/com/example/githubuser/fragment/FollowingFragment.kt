package com.example.githubuser.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.ItemsItem
import com.example.githubuser.adapter.ListUserAdapter
import com.example.githubuser.api.ApiConfig
import com.example.githubuser.databinding.FragmentFollowingBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingFragment : Fragment() {
    private lateinit var binding: FragmentFollowingBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val position = arguments?.getInt(ARG_POSITION, 0)
        val username = arguments?.getString(ARG_USERNAME)
        if (position == 1) {
            val layoutManager = LinearLayoutManager(requireActivity())
            binding.rvFollow.layoutManager = layoutManager
            if (username != null) {
                findFollowing(username)
            }
        } else {
            val layoutManager = LinearLayoutManager(requireActivity())
            binding.rvFollow.layoutManager = layoutManager
            if (username != null) {
                findFollowers(username)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun findFollowers(username: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setGithubData(responseBody)
                    }
                } else {
                    Log.e(TAG, "onFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun findFollowing(username: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setGithubData(responseBody)
                    }
                } else {
                    Log.e(TAG, "onFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setGithubData(itemGithub: List<ItemsItem>) {
        val listUser = ArrayList<ItemsItem>()
        for (users in itemGithub) {
            listUser.add(users)
        }
        val adapter = ListUserAdapter(listUser)
        binding.rvFollow.adapter = adapter
        binding.rvFollow.setHasFixedSize(true)
        binding.rvFollow.setItemViewCacheSize(20)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val ARG_USERNAME = "arg_username"
        const val ARG_POSITION = "arg_position"
        private const val TAG = "FollowingFragment"
    }
}