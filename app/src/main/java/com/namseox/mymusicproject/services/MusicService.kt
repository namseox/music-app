package com.namseox.mymusicproject.services

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.os.Binder
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.namseox.mymusicproject.R
import com.namseox.mymusicproject.acitivity.TrackActivity
import com.namseox.mymusicproject.helpers.Const
import com.namseox.mymusicproject.media.MediaManager
import com.namseox.mymusicproject.model.Song

class MusicService : Service() {
    private var data: ByteArray? = null
    private val binder = MusicBinder()
    private val musicReceiver = MusicReceiver()
    private lateinit var notificationView: RemoteViews
    private lateinit var builder: Notification.Builder
    private lateinit var notificationCompat: NotificationCompat.Builder
    private val musicFilter = IntentFilter()
    private var isStop: Boolean = false
    private var isPushNotif: Boolean = false


    fun getIsPushNotif(): Boolean = isPushNotif

    override fun onBind(intent: Intent?): IBinder {

        runForeground()
        return binder
    }

    override fun onCreate() {
        super.onCreate()


        musicFilter.addAction(Const.ACTION_STOP)
        musicFilter.addAction(Const.ACTION_PREVIOUS)
        musicFilter.addAction(Const.ACTION_NEXT)
        musicFilter.addAction(Const.ACTION_PAUSE_SONG)
        musicFilter.addAction(Const.ACTION_START_FOREGROUND)
        musicFilter.addAction(Const.ACTION_SEND_DATA)
        musicFilter.addAction(Const.ACTION_STOP)
        registerReceiver(musicReceiver, musicFilter)
        LocalBroadcastManager.getInstance(this).registerReceiver(musicReceiver, musicFilter)


    }

    fun setContextFromMS(context: Context?) {
        MediaManager.setContext(context)
    }


    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    inner class MusicBinder : Binder() {
        fun getMusicService(): MusicService {

            return this@MusicService
        }
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var stringFromFragment: String? = intent?.getStringExtra("string param 1")
        if (stringFromFragment == "online") {
            MediaManager.playPauseSong(1)
        } else {
            MediaManager.playPauseSong(2)
        }
        return START_STICKY
    }

    private var runnable: Runnable = object : Runnable {
        override fun run() {
            if (isStop == false) {
                updateNotif()
                startForeground(123, builder.build())
                Handler().postDelayed(this, 500)
            }
        }
    }

    private fun updateNotif() {
        notificationView.setTextViewText(R.id.tv_title, MediaManager.getCurrentSong().title)
        notificationView.setTextViewText(R.id.tv_artist, MediaManager.getCurrentSong().artistsNames)
        if (MediaManager.mediaPlayer.isPlaying) {
            notificationView.setImageViewResource(
                R.id.ibPause,
                R.drawable.ic_baseline_pause_24
            )
        } else {
            notificationView.setImageViewResource(
                R.id.ibPause,
                R.drawable.ic_baseline_play_arrow_24
            )
        }

        val mmr = MediaMetadataRetriever()
        mmr.setDataSource(MediaManager.getCurrentSong().path)
        data = mmr.embeddedPicture
        if (data != null) {
            val bitmap = BitmapFactory.decodeByteArray(data, 0, data!!.size)
            notificationView.setImageViewBitmap(R.id.ivNoti, bitmap)
        } else {
            notificationView.setImageViewResource(R.id.ivNoti, R.drawable.ic_music)
        }


    }

    private fun runForeground() {


        notificationView = RemoteViews(packageName, R.layout.layout_notification)

        val nextIntent = Intent(Const.ACTION_NEXT)
        val nextAction =
            PendingIntent.getBroadcast(
                this,
                1, nextIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        notificationView.setOnClickPendingIntent(R.id.ibNext, nextAction)

        val preIntent = Intent(Const.ACTION_PREVIOUS)
        val preAction =
            PendingIntent.getBroadcast(
                this,
                1, preIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        notificationView.setOnClickPendingIntent(R.id.ibPre, preAction)

        val playPauseIntent = Intent(Const.ACTION_PAUSE_SONG)
        val playPauseAction =
            PendingIntent.getBroadcast(
                this,
                1, playPauseIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        notificationView.setOnClickPendingIntent(R.id.ibPause, playPauseAction)

        val stopIntent = Intent(Const.ACTION_STOP)
        val stopAction =
            PendingIntent.getBroadcast(
                this,
                1,
                stopIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        notificationView.setOnClickPendingIntent(R.id.ibNotiClose, stopAction)


        val clickIntent = Intent(this, TrackActivity::class.java)
        clickIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        clickIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val clickAction = PendingIntent.getActivity(
            this,
            0,
            clickIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        notificationView.setOnClickPendingIntent(R.id.layout_notifi, clickAction)


//        val mediaSessionCompat = MediaSession(this, "tag")

        builder = Notification.Builder(this)
        builder.setContentText("This is music app")
        builder.setContentTitle("Music")


//        builder.addAction(R.drawable.ic_baseline_skip_next_24, "Next", nextAction)
//        builder.addAction(R.drawable.ic_baseline_skip_previous_24, "Pre", preAction)
//        builder.addAction(R.drawable.ic_baseline_pause_24, "Pause", playPauseAction)
//        builder.style = Notification.MediaStyle()
//            .setShowActionsInCompactView(0, 1, 2)
//            .setMediaSession(mediaSessionCompat.sessionToken)
        builder.setSmallIcon(R.drawable.ic_baseline_music_note_24)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setCustomContentView(notificationView)
        } else {
            builder.setContent(notificationView)
        }

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("123", "123", NotificationManager.IMPORTANCE_LOW)
            notificationManager.createNotificationChannel(channel)
            builder.setChannelId(channel.id)
        }

        val notification = builder.build()
//        notificationManager.notify(1, notification)

        startForeground(123, notification)
        isPushNotif = true
        runnable.run()

    }


    fun nextSong() {
        MediaManager.nextSong()
        sendData()
    }

    fun preSong() {
        MediaManager.preSong()
        sendData()
    }

    fun playPauseSong(isNews: Boolean = false, i : Int) {
        MediaManager.playPauseSong(i,isNews)
        sendData()

    }

    private fun sendData() {
        val intent = Intent(Const.ACTION_SEND_DATA)
        intent.putExtra(Const.KEY_TITLE_SONG, MediaManager.getCurrentSong().title)
        intent.putExtra(Const.KEY_NAME_ARTIST, MediaManager.getCurrentSong().artistsNames)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    inner class MusicReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {

            when (intent?.action) {

                Const.ACTION_PLAY_NEW -> {
                    isStop = false

                    val song: Song = MediaManager.getCurrentSong()
                    notificationView.setTextViewText(R.id.tv_title, song.title)
                    notificationView.setTextViewText(R.id.tv_artist, song.artistsNames)
                    startForeground(123, builder.build())
                    isPushNotif = true
                }

                Const.ACTION_NEXT -> {
                    isStop = false

                    val song: Song = MediaManager.getCurrentSong()
                    notificationView.setTextViewText(R.id.tv_title, song.title)
                    notificationView.setTextViewText(R.id.tv_artist, song.artistsNames)
                    startForeground(123, builder.build())
                    isPushNotif = true

                    nextSong()
                }
                Const.ACTION_PREVIOUS -> {
                    isStop = false

                    val song: Song = MediaManager.getCurrentSong()
                    notificationView.setTextViewText(R.id.tv_title, song.title)
                    notificationView.setTextViewText(R.id.tv_artist, song.artistsNames)
                    startForeground(123, builder.build())
                    isPushNotif = true

                    preSong()
                }
                Const.ACTION_PAUSE_SONG -> {
                    isStop = false

                    if (MediaManager.mediaPlayer.isPlaying) {
                        notificationView.setImageViewResource(
                            R.id.ibPause,
                            R.drawable.ic_baseline_play_arrow_24
                        )
                    } else {
                        notificationView.setImageViewResource(
                            R.id.ibPause,
                            R.drawable.ic_baseline_pause_24
                        )
                    }
                    val song: Song = MediaManager.getCurrentSong()
                    notificationView.setTextViewText(R.id.tv_title, song.title)
                    notificationView.setTextViewText(R.id.tv_artist, song.artistsNames)
                    startForeground(123, builder.build())
                    isPushNotif = true

                        MediaManager.playPauseSong(0)


                }
                Const.ACTION_STOP -> {
                    isStop = true
                    MediaManager.mediaPlayer.stop()
                    MediaManager.changeMediaState(Const.MEDIA_STOPPED)
                    stopSelf()
                    val intentStop = Intent(Const.ACTION_STOP_ALL)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    this@MusicService.stopForeground(true)
                    isPushNotif = false


                }

            }
        }

    }


    override fun onDestroy() {
        unregisterReceiver(musicReceiver)
        stopSelf()
        this@MusicService.stopForeground(false)
//        MediaManager.setContext(null)
        super.onDestroy()

    }

}