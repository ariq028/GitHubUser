package com.example.githubuser

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    companion object {
        private const val TAG = "MainActivity"
        private const val GITHUB_ID = "Arif"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvGithub.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, 0)
        binding.rvGithub.addItemDecoration(itemDecoration)

        findUser()

    }

    private fun findUser() {
        showLoading(true)
        val client = ApiConfig.getApiService().getGithub(GITHUB_ID)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setGithubData(responseBody.items)
                    }
                } else {
                    Log.e(TAG, "onFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
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
        val adapter = UserAdapter(listUser)
        binding.rvGithub.adapter = adapter
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
        return true
    }
}