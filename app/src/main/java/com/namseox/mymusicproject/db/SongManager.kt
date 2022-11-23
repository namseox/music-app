package com.namseox.mymusicproject.db

import android.content.ContentValues
import android.content.Context
import com.namseox.mymusicproject.dao.Const
import com.namseox.mymusicproject.model.Song

class SongManager(private val context: Context) {
    private val songHelper = SongOpenHelper(context)
    private val db = songHelper.writableDatabase

    fun addSong(song: Song) {
        val value = ContentValues()
        value.put(Const.COL_ID, song.id)
        value.put(Const.COL_TITLE, song.title)
        value.put(Const.COL_ARTISTSNAMES, song.artistsNames)
        value.put(Const.COL_THUMBNAIL, song.thumbnail)
        value.put(Const.COL_THUMBNAILMEDIA, song.thumbnailMedium)
        value.put(Const.COL_LYRIC, song.lyric)
        value.put(Const.COL_LISTEN, song.listen)
        value.put(Const.COL_DURATION, song.duration)
        value.put(Const.COL_PATH, song.path)
        value.put(Const.COL_FILENAME, song.fileName)
        value.put(Const.COL_ALBUM, song.album)
        if (song.isLiked) {
            value.put(Const.COL_ISLIKED, 1)
        } else {
            value.put(Const.COL_ISLIKED, 0)
        }
        db.insert(Const.TABLE_SONG,null,value)

    }

}