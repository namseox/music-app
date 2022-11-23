package com.namseox.mymusicproject.acitivity

import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.namseox.mymusicproject.R
import com.namseox.mymusicproject.databinding.ActivityTrackBinding
import com.namseox.mymusicproject.helpers.Const
import com.namseox.mymusicproject.helpers.SharePreferencesController
import com.namseox.mymusicproject.media.MediaManager
import com.namseox.mymusicproject.model.Song
import com.namseox.mymusicproject.services.MusicService
import com.squareup.picasso.Picasso

class TrackActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityTrackBinding
    private val songList: ArrayList<Song> = MediaManager.getMySongList()
    private val onlSOngList: List<Song> = MediaManager.getMySongOnlineList()
    var data: ByteArray? = null
    private var musicService = MusicService()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)

        binding = ActivityTrackBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        MediaManager.setContext(this)
        musicService.setContextFromMS(this)
        initView()
        runnable.run()

    }


    private fun initView() {
        binding.activityTrackPrevious.setOnClickListener(this)
        binding.activityTrackNext.setOnClickListener(this)
        binding.activityTrackPlayPause.setOnClickListener(this)
        binding.activityTrackToggleShuffle.setOnClickListener(this)
        binding.activityTrackPlaybackSetting.setOnClickListener(this)
        binding.activityTrackProgressbar.setOnSeekBarChangeListener(object :
            OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser == true) {
                    MediaManager.mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        binding.isLikedTrack.setOnClickListener(this)
        binding.activityTrackToolbar.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val intent = Intent()
        when (v!!.id) {

            R.id.activity_track_next -> {
                intent.action = Const.ACTION_NEXT
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            }
            R.id.activity_track_previous -> {
                intent.action = Const.ACTION_PREVIOUS
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            }
            R.id.activity_track_play_pause -> {
                intent.action = Const.ACTION_PAUSE_SONG
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            }
            R.id.is_liked_track -> {
                MediaManager.getCurrentSong().isLiked = !MediaManager.getCurrentSong().isLiked
            }
            R.id.activity_track_toggle_shuffle -> {
                changeStateShuffle()
            }
            R.id.activity_track_playback_setting -> {
                changeStateLoop()
            }
            R.id.activity_track_toolbar -> {
                finish()
            }
        }
    }

    //chua the thay doi data?
    private fun checkIsLikedSong() {
        if (MediaManager.getCurrentSong().isLiked) {
            binding.isLikedTrack.setImageResource(R.drawable.heart)
        } else {
            binding.isLikedTrack.setImageResource(R.drawable.like)
        }
    }

    private fun updateSongUI() {
        updateInforBottomLayout()
        if (MediaManager.intNen == 1) {
            binding.activityTrackTitle.text = MediaManager.getCurrentSongOnl().title
            binding.activityTrackArtist.text = MediaManager.getCurrentSongOnl().artistsNames
            binding.activityTrackProgressCurrent.text =
                milliSecondsToTimer(MediaManager.mediaPlayer.currentPosition.toLong())
            binding.activityTrackProgressMax.text =
                milliSecondsToTimer(MediaManager.mediaPlayer.duration.toLong())
        } else {
            binding.activityTrackTitle.text = MediaManager.getCurrentSong().title
            binding.activityTrackArtist.text = MediaManager.getCurrentSong().artistsNames
            binding.activityTrackProgressCurrent.text =
                milliSecondsToTimer(MediaManager.mediaPlayer.currentPosition.toLong())
            binding.activityTrackProgressMax.text =
                milliSecondsToTimer(MediaManager.mediaPlayer.duration.toLong())
        }

        binding.activityTrackProgressbar.progress = MediaManager.mediaPlayer.currentPosition
        binding.activityTrackProgressbar.max = MediaManager.mediaPlayer.duration
        updateBottomUI()
        checkIsLikedSong()

    }

    private fun updateInforBottomLayout() {
        if (MediaManager.mediaPlayer.isPlaying) {
            binding.activityTrackPlayPause.setImageResource(R.drawable.ic_baseline_pause_24)
        } else {
            binding.activityTrackPlayPause.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        }
    }

    private var runnable: Runnable = object : Runnable {
        override fun run() {
            updateSongUI()
            setImageDetail()
            setImageNextSong()
            Handler().postDelayed(this, 200)
        }
    }

    private fun updateBottomUI() {
        if (MediaManager.intNen == 2) {
            binding.nextTrackLabel.text =
                "Next: " + songList[(MediaManager.getCurrentPossion() + 1).mod(songList.size)].title
        } else {
            binding.nextTrackLabel.text =
                "Next: " + onlSOngList[(MediaManager.getCurrentPossion() + 1).mod(onlSOngList.size)].title
        }
    }


    fun milliSecondsToTimer(milliseconds: Long): String? {
        var finalTimerString = ""
        var secondsString = ""
        // Convert total duration into time
        val hours = (milliseconds / (1000 * 60 * 60)).toInt()
        val minutes = (milliseconds % (1000 * 60 * 60)).toInt() / (1000 * 60)
        val seconds = (milliseconds % (1000 * 60 * 60) % (1000 * 60) / 1000).toInt()
        // Add hours if there
        if (hours > 0) {
            finalTimerString = "$hours:"
        }
        // Prepending 0 to seconds if it is one digit
        secondsString = if (seconds < 10) {
            "0$seconds"
        } else {
            "" + seconds
        }
        finalTimerString = "$finalTimerString$minutes:$secondsString"
        // return timer string
        return finalTimerString
    }


    private fun changeStateShuffle() {
        val sharePreference: SharePreferencesController =
            SharePreferencesController.getInstance(this)
        val shuffle: Boolean =
            sharePreference.getBoolean(Const.MEDIA_SHUFFLE, Const.MEDIA_SHUFFLE_TRUE)
        if (shuffle) {
            sharePreference.putBoolean(Const.MEDIA_SHUFFLE, Const.MEDIA_SHUFFLE_FLASE)
            binding.activityTrackToggleShuffle.setImageResource(R.drawable.ic_baseline_shuffle_24)
        } else {
            sharePreference.putBoolean(Const.MEDIA_SHUFFLE, Const.MEDIA_SHUFFLE_TRUE)
            //TODO
            //FIXME
            //Thay ảnh
            binding.activityTrackToggleShuffle.setImageResource(R.drawable.ic_baseline_shuffle_24_on)
        }
    }

    private fun changeStateLoop() {
        val sharePreference: SharePreferencesController =
            SharePreferencesController.getInstance(this)
        when (sharePreference.getInt(Const.MEDIA_CURRENT_STATE_LOOP, Const.MEDIA_STATE_NO_LOOP)) {
            Const.MEDIA_STATE_LOOP_ONE -> {
                sharePreference.putInt(Const.MEDIA_CURRENT_STATE_LOOP, Const.MEDIA_STATE_LOOP_ALL)
                binding.activityTrackPlaybackSetting.setImageResource(R.drawable.ic_baseline_repeat_24_on)
            }
            Const.MEDIA_STATE_LOOP_ALL -> {
                sharePreference.putInt(Const.MEDIA_CURRENT_STATE_LOOP, Const.MEDIA_STATE_NO_LOOP)
                binding.activityTrackPlaybackSetting.setImageResource(R.drawable.ic_baseline_repeat_24)
            }
            Const.MEDIA_STATE_NO_LOOP -> {
                binding.activityTrackPlaybackSetting.setImageResource(R.drawable.ic_baseline_repeat_one_24)
                sharePreference.putInt(Const.MEDIA_CURRENT_STATE_LOOP, Const.MEDIA_STATE_LOOP_ONE)
            }
        }
    }

    private fun setImageDetail() {
        if (MediaManager.intNen == 2) {
            val mmr = MediaMetadataRetriever()
            mmr.setDataSource(songList[MediaManager.getCurrentPossion()].path)
            data = mmr.embeddedPicture
            if (data != null) {

                val bitmap = BitmapFactory.decodeByteArray(data, 0, data!!.size)
                binding.activityTrackImage.setImageBitmap(bitmap)
            } else {
                binding.activityTrackImage.setImageResource(R.drawable.ic_music)
            }
            binding.activityTrackImage.adjustViewBounds = true
        } else {
            var uri: Uri = Uri.parse(MediaManager.getCurrentSongOnl().thumbnail)
            if (uri != null) {
                Picasso.get()
                    .load(uri)
                    .into(binding!!.activityTrackImage)

            }
            binding.activityTrackImage.adjustViewBounds = true

//            binding!!.bottomMenu.tvBottomTitleSong.text =
//                MediaManager.getCurrentSongOnl().title
//            binding!!.bottomMenu.tvBottomNameArtist.text =
//                MediaManager.getCurrentSongOnl().artistsNames
        }
//        binding.activityTrackImage.layoutParams = LinearLayout.LayoutParams(500, 400)
    }

    private fun setImageNextSong() {
        if (MediaManager.intNen == 2) {
            val mmr = MediaMetadataRetriever()
            mmr.setDataSource(songList[(MediaManager.getCurrentPossion() + 1).mod(songList.size)].path)
            data = mmr.embeddedPicture
            if (data != null) {
                val bitmap = BitmapFactory.decodeByteArray(data, 0, data!!.size)
                binding.nextTrackImage.setImageBitmap(bitmap)
            } else {
                binding.nextTrackImage.setImageResource(R.drawable.ic_music)
            }
            binding.activityTrackImage.adjustViewBounds = true
        } else {
            var uri: Uri = Uri.parse(onlSOngList[MediaManager.getCurrentPossion() + 1].thumbnail)
            if (uri != null) {
                Picasso.get()
                    .load(uri)
                    .into(binding!!.nextTrackImage)
            }
        }
    }

    override fun onDestroy() {
//        musicService.setContextFromMS(null)
        super.onDestroy()
//        MediaManager.setContext(null)

    }

}