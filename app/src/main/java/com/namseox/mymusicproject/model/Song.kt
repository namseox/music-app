package com.namseox.mymusicproject.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Song(
    @SerializedName("_id")
    var id: String,
    @SerializedName("name_song")
    val title: String,
    @SerializedName("artists")
    var artistsNames: String,

    var thumbnail: String,
    var thumbnailMedium: String,
    @SerializedName("link")
    var lyric: String?,

    var listen: Int?,

    var duration : Int?,

    var path: String ,

    var fileName: String = "",

    @SerializedName("album_id")
    var album: String = "",

    var isLiked: Boolean = false,



) : Serializable