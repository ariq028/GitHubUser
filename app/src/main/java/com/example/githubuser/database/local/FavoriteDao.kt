package com.example.githubuser.database.local


import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favoriteUser: FavoriteUser)

    @Query("DELETE FROM favorite_user WHERE favorite_user.username = :username")
    fun delete(username: String): Int

    @Query("SELECT * from favorite_user ORDER BY username ASC")
    fun getAllFavorite(): LiveData<List<FavoriteUser>>

    @Query("SELECT count(*) FROM favorite_user WHERE favorite_user.username = :username")
    fun checkUser(username: String): Int
}