package com.example.githubuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuser.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailUser: DetailUserResponse

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_OBJECT = "extra_object"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        val username = intent.getStringExtra(EXTRA_USERNAME)
        if (username != null) {
            val apiService = ApiConfig.getApiService()
            val call = apiService.getDetailUser(username)
            call.enqueue(object : Callback<DetailUserResponse> {
                override fun onResponse(
                    call: Call<DetailUserResponse>,
                    response: Response<DetailUserResponse>
                ) {
                    if (response.isSuccessful) {
                        detailUser = response.body()!!

                        // Retrieve the data you need and update the UI
                        binding.tvName.text = detailUser.name
                        binding.tvUsername.text = detailUser.login
                        binding.tvFollowing.text = getString(R.string.following, detailUser.following)
                        binding.tvFollowers.text = getString(R.string.followers, detailUser.followers)
                        Glide.with(binding.root.context)
                            .load(detailUser.avatarUrl)
                            .into(binding.ivImage)
                    }
                }

                override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                    Toast.makeText(this@DetailActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}