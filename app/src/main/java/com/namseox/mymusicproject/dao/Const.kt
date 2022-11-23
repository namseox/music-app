package com.namseox.mymusicproject.dao

object Const {
    const val TABLE_PLAYLIST = "Table_PlayList"
    const val COL_NAME ="col_name"
    const val COL_LINK = "col_link"

    const val TABLE_SONG = "Table_Song"
    const val COL_ID = "col_Id"
    const val COL_TITLE = "col_Title"
    const val COL_ARTISTSNAMES = "col_ArtistsNames"
    const val COL_THUMBNAIL= "col_thumbnail"
    const val COL_THUMBNAILMEDIA = "col_thumbnailMedium"
    const val COL_LYRIC = "col_lyric"
    const val COL_LISTEN = "col_listen"
    const val COL_DURATION = "col_duration"
    const val COL_PATH = "col_path"
    const val COL_FILENAME = "col_filename"
    const val COL_ALBUM = "col_album"
    const val COL_ISLIKED = "col_isLiked"

    const val QUEUE_CREAT_TABLE_PLAYLIST =
        "CREATE TABLE $TABLE_PLAYLIST($COL_NAME TEXT, $COL_LINK TEXT)"
    const val QUEUE_CREAT_TABLE_SONG =
        "CREATE TABLE $TABLE_SONG($COL_ID TEXT, $COL_TITLE TEXT" +
                "$COL_ARTISTSNAMES TEXT,$COL_THUMBNAIL TEXT,$COL_THUMBNAILMEDIA TEXT," +
                "$COL_LYRIC TEXT,$COL_LISTEN INTEGER,$COL_DURATION INTEGER," +
                "$COL_PATH TEXT,$COL_FILENAME TEXT,$COL_ALBUM TEXT," +
                "$COL_ISLIKED TEXT)"
}