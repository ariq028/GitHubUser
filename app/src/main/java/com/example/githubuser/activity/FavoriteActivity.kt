package com.example.githubuser.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.adapter.FavoriteAdapter
import com.example.githubuser.databinding.ActivityFavoriteBinding
import com.example.githubuser.viewmodel.FavoriteViewModel

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var viewModel: FavoriteViewModel
    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, 0)
        binding.rvFavorite.addItemDecoration(itemDecoration)
        binding.rvFavorite.setHasFixedSize(true)

        viewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]

        favoriteAdapter = FavoriteAdapter(emptyList())
        binding.rvFavorite.adapter = favoriteAdapter

        viewModel.favoriteUsers.observe(this) { users ->
            Log.d("FavoriteViewModel", "Received ${users.size} users")
            favoriteAdapter.userList = users
            favoriteAdapter.notifyDataSetChanged()
        }
    }
}





