package com.namseox.mymusicproject.interfaces

import android.view.View
import com.namseox.mymusicproject.model.Song

interface ILongClickPlaylist {
    fun longClickPlaylist(posion: Int, view: View, namePlaylist: String) :Boolean
}