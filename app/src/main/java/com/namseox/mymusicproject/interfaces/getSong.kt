package com.namseox.mymusicproject.interfaces

import com.namseox.mymusicproject.model.Song

interface getSong {
    fun getClickCheckBox(song: Song)
    fun playPlaylist(playlist: String)
    fun getClickPlayList(song: Song, int:Int)
}