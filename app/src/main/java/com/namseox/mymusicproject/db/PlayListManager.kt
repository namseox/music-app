package com.namseox.mymusicproject.db

import android.content.ContentValues
import android.content.Context
import com.namseox.mymusicproject.acitivity.ui.home.HomeFragment
import com.namseox.mymusicproject.acitivity.ui.playlist.FragmentPlaylist
import com.namseox.mymusicproject.dao.Const
import com.namseox.mymusicproject.model.Playlist

class PlayListManager(private val context: Context) {
    private val playlistHelper = PlaylistOpenHelper(context)
    private val db = playlistHelper.writableDatabase

    fun addPlayList(playlist:Playlist){
//        val sql = "INSERT INTO ${Const.TABLE_PLAYLIST}(${Const.COL_NAME},${Const.COL_LINK}) VALUES ('${playlist.namePlaylist}','${playlist.link}');"
//        sql theo android:
//        ADD:
        val value = ContentValues()
        value.put(Const.COL_NAME,playlist.namePlaylist)
        value.put(Const.COL_LINK,playlist.link)
        db.insert(Const.TABLE_PLAYLIST,null,value)

//        update:
//        val value = ContentValues()
//        value.put(Const.COL_NAME,playlist.namePlaylist)
//        value.put(Const.COL_LINK,playlist.link)
//        db.update(Const.TABLE_PLAYLIST,value,"${Const.COL_LINK}=? AND... ", arrayOf(playlist.link.toString()) )
    }
    fun deletePlayList(name: String){
        db.delete(Const.TABLE_PLAYLIST,"${Const.COL_NAME}=?", arrayOf(name))
    }
    fun getAllNamePlaylist(): ArrayList<String>{
        val listName = arrayListOf<String>()
         val sql = "SELECT ${Const.COL_NAME} FROM ${Const.TABLE_PLAYLIST} GROUP BY ${Const.COL_NAME}"
        val cursor = db.rawQuery(sql, null)
        if (cursor.moveToFirst()){
            do {
                val name = cursor.getString(0)
                listName.add(name)
            }while (cursor.moveToNext())
        }
        return listName
    }
    fun getAllPlaylist(): ArrayList<Playlist> {
        val listplaylist = arrayListOf<Playlist>()

        val sql= "SELECT * FROM ${Const.TABLE_PLAYLIST}"
        val cursor = db.rawQuery(sql, null)
        if (cursor.moveToFirst()){
            do {
                val name = cursor.getString(0)
                val link = cursor.getString(1)
                val playlist= Playlist(name,link)
                listplaylist.add(playlist)
            }while (cursor.moveToNext())
        }

        return listplaylist
    }
}