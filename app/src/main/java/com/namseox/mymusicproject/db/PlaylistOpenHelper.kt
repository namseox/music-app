package com.namseox.mymusicproject.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.namseox.mymusicproject.dao.Const

class PlaylistOpenHelper(context : Context) : SQLiteOpenHelper(context,"playlist.db",null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(Const.QUEUE_CREAT_TABLE_PLAYLIST )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}