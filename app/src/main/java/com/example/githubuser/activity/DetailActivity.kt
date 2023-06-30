package com.example.githubuser.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.adapter.SectionsPagerAdapter
import com.example.githubuser.databinding.ActivityDetailBinding
import com.example.githubuser.viewmodel.DetailViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val avatarUrl = intent.getStringExtra(EXTRA_URL)

        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]

        username?.let { viewModel.loadDetailUser(it) }
        avatarUrl?.let { viewModel.loadDetailUser(it) }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }


        CoroutineScope(Dispatchers.IO).launch {
            val count = username?.let { viewModel.checkUser(it) }
            withContext(Dispatchers.Main) {
                if (count != null) {
                    binding.toggleFav.isChecked = count != null && count > 0
                }
            }
        }

        binding.toggleFav.setOnClickListener {
            if (binding.toggleFav.isChecked) {
                if (username != null) {
                    if (avatarUrl != null) {
                        viewModel.getAllFavorite(username, avatarUrl)
                    }
                }
            } else {
                if (username != null) {
                    viewModel.removeFromFavorite(username)
                }
            }
        }

        viewModel.detailUser.observe(this) { detailUser ->
            binding.tvName.text = detailUser.name
            binding.tvUsername.text = detailUser.login
            binding.tvFollowing.text = getString(R.string.following, detailUser.following)
            binding.tvFollowers.text = getString(R.string.followers, detailUser.followers)
            Glide.with(binding.root.context)
                .load(detailUser.avatarUrl)
                .into(binding.ivImage)
        }

        val sectionsPagerAdapter = username?.let { SectionsPagerAdapter(this, it) }
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setTabTextColors(Color.GRAY, Color.RED)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_URL = "extra_url"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}