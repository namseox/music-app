//package com.namseox.mymusicproject.dao
//
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//import com.namseox.mymusicproject.model.Playlist
//
//@Dao
//interface SongDao {
//    @Query("SELECT * FROM playlist")
//    suspend fun getPlayList(): List<Playlist>
//
//    @Insert(onConflict = OnConflictStrategy.ABORT)
//    fun insertPlaylist(playlist: Playlist)
//}