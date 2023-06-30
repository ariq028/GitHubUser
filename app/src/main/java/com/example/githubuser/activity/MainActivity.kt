package com.example.githubuser.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.GithubResponse
import com.example.githubuser.R
import com.example.githubuser.adapter.UserAdapter
import com.example.githubuser.api.ApiConfig
import com.example.githubuser.database.datastore.SettingPreferences
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    private val settingPreferences by lazy {
        SettingPreferences.getInstance(applicationContext.dataStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            settingPreferences.getThemeSetting().collect { isDarkModeActive ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvGithub.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, 0)
        binding.rvGithub.addItemDecoration(itemDecoration)
        binding.rvGithub.setHasFixedSize(true)

        viewModel.listUser.observe(this) {
            val adapter = UserAdapter(it)
            binding.rvGithub.layoutManager = LinearLayoutManager(this)
            binding.rvGithub.adapter = adapter
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.tvItem -> {
                val moveIntent = Intent(this@MainActivity, DetailActivity::class.java)
                startActivity(moveIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.option_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(q: String): Boolean {
                val apiService = ApiConfig.getApiService()
                apiService.getGithub(q).enqueue(object : Callback<GithubResponse> {
                    override fun onResponse(
                        call: Call<GithubResponse>,
                        response: Response<GithubResponse>
                    ) {
                        if (response.isSuccessful) {
                            val githubResponse = response.body()
                            val users = githubResponse?.items
                            if (users != null && users.isNotEmpty()) {
                                binding.rvGithub.adapter = UserAdapter(users)
                                binding.rvGithub.layoutManager =
                                    LinearLayoutManager(applicationContext)
                                binding.rvGithub.setHasFixedSize(true)
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    "No results found",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "onFailure: ${response.code()} ${response.message()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                        Toast.makeText(
                            applicationContext,
                            "onFailure: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })

                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        val favoriteItem = menu?.findItem(R.id.menu_favorite)
        favoriteItem?.setOnMenuItemClickListener {
            val intent = Intent(this, FavoriteActivity::class.java)
            startActivity(intent)
            true
        }

        val darkMode = menu?.findItem(R.id.menu_dark)
        darkMode?.setOnMenuItemClickListener {
            val intentDark = Intent(this, DarkModeActivity::class.java)
            startActivity(intentDark)
            true
        }

        return true
    }
}