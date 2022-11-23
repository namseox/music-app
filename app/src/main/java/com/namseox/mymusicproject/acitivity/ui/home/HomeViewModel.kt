package com.namseox.mymusicproject.acitivity.ui.home

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.namseox.mymusicproject.db.PlayListManager
import com.namseox.mymusicproject.media.MediaManager
import com.namseox.mymusicproject.model.Playlist
import com.namseox.mymusicproject.model.Song

class HomeViewModel : ViewModel() {
    var namePlaylist: MutableLiveData<List<String>> = MutableLiveData()
    var allPlaylist: MutableLiveData<List<Playlist>> = MutableLiveData()
    var allSonglistOffline: MutableLiveData<List<Song>> = MutableLiveData()

    fun getNamePlaylist(context: Context){
        var playlistManager = PlayListManager(context)
        namePlaylist.postValue(playlistManager.getAllNamePlaylist())

    }

    fun getAllPlaylist(context: Context) {
        var managerAllPlaylist = PlayListManager(context)
        allPlaylist.postValue(managerAllPlaylist.getAllPlaylist())
    }
    fun getAllSongOffline(){

        allSonglistOffline.postValue(MediaManager.mSongList)
    }
}