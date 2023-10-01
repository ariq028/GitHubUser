package com.example.githubuser.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.githubuser.database.local.FavoriteRoomDatabase
import com.example.githubuser.database.local.FavoriteUser

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private val favoriteDao = FavoriteRoomDatabase.getDatabase(application).favoriteDao()

    val favoriteUsers: LiveData<List<FavoriteUser>> = favoriteDao.getAllFavorite()

}