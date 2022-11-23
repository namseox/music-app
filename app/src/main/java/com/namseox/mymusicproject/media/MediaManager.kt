package com.namseox.mymusicproject.media

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat
import com.namseox.mymusicproject.helpers.Const
import com.namseox.mymusicproject.helpers.Const.MEDIA_IDLE
import com.namseox.mymusicproject.helpers.Const.MEDIA_PAUSED
import com.namseox.mymusicproject.helpers.Const.MEDIA_PLAYING
import com.namseox.mymusicproject.helpers.Const.MEDIA_STOPPED
import com.namseox.mymusicproject.helpers.SharePreferencesController
import com.namseox.mymusicproject.model.Song
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

@SuppressLint("StaticFieldLeak")
object MediaManager {
    public var intNen: Int = 0
    var choice: Boolean = true
    const val PERMISION_ALL = 265


    var mediaPlayer: MediaPlayer = MediaPlayer()
    var mSongList = arrayListOf<Song>()         /*off*/
    var mSongList2 = arrayListOf<Song>()
    var listSong: List<Song> = listOf()
    var namePlaylist: String = ""
    private var currentSong = -1
    var mediaState = MEDIA_IDLE
    var context1: Context? = null
    var sort = 0
    var j: Int = 0
    var intPlayMusic: Int = 0

    fun changeMediaState(state: Int) {
        mediaState = state
    }

    fun setContext(context: Context?) {
        context1 = context
    }

    fun nextSong() {
        val sharePreference: SharePreferencesController =
            SharePreferencesController.getInstance(context1!!)
        val loop = sharePreference.getInt(Const.MEDIA_CURRENT_STATE_LOOP, Const.MEDIA_STATE_NO_LOOP)
        val shuffle = sharePreference.getBoolean(Const.MEDIA_SHUFFLE, Const.MEDIA_SHUFFLE_TRUE)
        if (loop != Const.MEDIA_STATE_LOOP_ONE) {
            if (shuffle) {
                currentSong = Random.nextInt(mSongList.size)
            } else {
                if (currentSong > mSongList.size - 2) {
                    currentSong = 0
                } else {
                    currentSong++
                }
            }
        }
        if (MediaManager.intNen == 1) {
            playPauseSong(1, true)
        } else {
            playPauseSong(2, true)
        }
    }


    fun playPauseSong(i: Int, isNews: Boolean = false) {

        if (mediaState == MEDIA_IDLE || mediaState == MEDIA_STOPPED || isNews) {
            if (i == 1) {
                choice = true
                mediaPlayer.reset()
                Log.d("...", listSong.size.toString())
                val song = listSong[currentSong]
                Log.d("...", song.lyric.toString())
                mediaPlayer.setDataSource(song.lyric)
                //         mediaPlayer.setDataSource("https://mp3-s1-zmp3.zmdcdn.me/ea859fb645f2acacf5e3/2153293163773944052?authen=exp=1667267765~acl=/ea859fb645f2acacf5e3/*~hmac=258634802f8cfb82e950c9e04a4f7e6a&fs=MTY2NzA5NDk2NTmUsICyMXx3ZWJWNHwxNzEdUngMjM2LjU2LjY4")
                mediaPlayer.prepare()
                mediaPlayer.start()
                mediaState = MEDIA_PLAYING
            } else {
                //chay tu dau

                choice = false
                mediaPlayer.reset()
                val song = mSongList[currentSong]
                mediaPlayer.setDataSource(song.path)
                mediaPlayer.prepare()
                mediaPlayer.start()
                mediaState = MEDIA_PLAYING
            }

        } else if (mediaState == MEDIA_PLAYING) {
            mediaPlayer.pause()
            mediaState = MEDIA_PAUSED
        } else if (mediaState == MEDIA_PAUSED) {
            mediaPlayer.start()
            mediaState = MEDIA_PLAYING
        }
        mediaPlayer.setOnCompletionListener { nextSong() }
    }

    fun preSong() {
        val sharePreference = SharePreferencesController.getInstance(context1!!)
        val loop = sharePreference.getInt(Const.MEDIA_CURRENT_STATE_LOOP, Const.MEDIA_STATE_NO_LOOP)
        val shuffle = sharePreference.getBoolean(Const.MEDIA_SHUFFLE, Const.MEDIA_SHUFFLE_TRUE)
        if (loop != Const.MEDIA_STATE_LOOP_ONE) {
            if (shuffle) {
                currentSong = Random.nextInt(mSongList.size)
            } else {
                if (currentSong <= 0) {
                    currentSong = mSongList.size - 1
                } else {
                    currentSong--
                }
            }
        }
        playPauseSong(1, true)
    }

    fun mediaStop() {
        mediaPlayer.stop()
        context1 = null
    }

    fun setCurrentSong(index: Int) {
        currentSong = index

    }


    fun getAllSongFromStorage(context: Context): List<Song> {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val PERMISSION = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(
                (context as Activity),
                PERMISSION,
                PERMISION_ALL
            )
        } else {
            val columnsName =
                arrayOf(
                    MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.DISPLAY_NAME,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.ARTIST,
                )


            val cursor = context.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                columnsName,
                null,
                null,
                null,
                null
            )!!
            val indexData = cursor.getColumnIndex(columnsName[0])
            val indexTitle = cursor.getColumnIndex(columnsName[1])
            val indexDisplayName = cursor.getColumnIndex(columnsName[2])
            val indexDuration = cursor.getColumnIndex(columnsName[3])
            val indexAlbum = cursor.getColumnIndex(columnsName[4])
            val indexArtist = cursor.getColumnIndex(columnsName[5])

            val hasData = cursor.moveToFirst()
            if (hasData) {
                mSongList.clear()
                while (!cursor.isAfterLast) {
                    val data = cursor.getString(indexData)
                    val title = cursor.getString(indexTitle)
                    val displayName = cursor.getString(indexDisplayName)
                    val duration = cursor.getLong(indexDuration)
                    val album = cursor.getString(indexAlbum)
                    val artist = cursor.getString(indexArtist)

                    if (duplicateChecking(title, mSongList)) {
                        cursor.moveToNext()
                        continue
                    }
                    mSongList.add(
                        Song(
                            Const.DEFAULT_SONG_ID,
                            title,
                            artist,
                            null.toString(),
                            null.toString(),
                            null,
                            Const.DEFAULT_SONG_LISTEN,
                            duration.toInt(),
                            data,
                            displayName,
                            album,
                            true

                        )
                    )
                    cursor.moveToNext()
                }
            }
        }
        return mSongList
    }


    private fun duplicateChecking(title1: String, mListSong: List<Song>): Boolean {
        for (item in mListSong.listIterator()) {
            if (item.title == title1)
                return true
        }
        return false
    }

    fun isChangePosition(index: Int): Boolean {
        return index != currentSong
    }

    fun getCurrentSong(): Song {
        return mSongList[currentSong]

    }

    fun getCurrentSongOnl(): Song {
        return listSong[currentSong]
    }

    fun getCurrentPossion(): Int = currentSong
    fun getMySongList(): ArrayList<Song> = mSongList
    fun getMySongOnlineList(): List<Song> = listSong
}