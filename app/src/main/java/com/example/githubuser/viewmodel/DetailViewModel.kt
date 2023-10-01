package com.example.githubuser.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.githubuser.api.ApiConfig
import com.example.githubuser.api.DetailUserResponse
import com.example.githubuser.database.local.FavoriteDao
import com.example.githubuser.database.local.FavoriteRoomDatabase
import com.example.githubuser.database.local.FavoriteUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    private val _detailUser = MutableLiveData<DetailUserResponse>()
    val detailUser: LiveData<DetailUserResponse> = _detailUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var userDao: FavoriteDao
    private var userDb: FavoriteRoomDatabase

    init {
        userDb = FavoriteRoomDatabase.getDatabase(application)!!
        userDao = userDb.favoriteDao()!!
    }

    fun loadDetailUser(username: String) {
        _isLoading.value = true
        val apiService = ApiConfig.getApiService()
        val call = apiService.getDetailUser(username)
        call.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailUser.value = response.body()!!
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "DetailViewModel"
    }

    fun getAllFavorite(username: String, avatarUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = FavoriteUser(
                username,
                avatarUrl
            )
            userDao.insert(user)
        }
    }

    fun checkUser(username: String) = userDao.checkUser(username)

    fun removeFromFavorite(username: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            if (username != null) {
                userDao.delete(username)
            }
        }
    }
}