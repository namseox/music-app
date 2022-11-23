//package com.namseox.mymusicproject.reponstory
//
//import com.namseox.mymusicproject.dao.SongDao
//import com.namseox.mymusicproject.model.Playlist
//
//class PlaylistReponstory(private val playlistDao: SongDao) {
//    suspend fun insertSudent(playlist: Playlist){
//        playlistDao.insertPlaylist(playlist)
//    }
//    suspend fun getAllStudent():List<Playlist>{
//        return playlistDao.getPlayList()
//    }
//}