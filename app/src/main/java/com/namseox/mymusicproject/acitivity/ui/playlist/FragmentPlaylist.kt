package com.namseox.mymusicproject.acitivity.ui.playlist

import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.namseox.mymusicproject.R
import com.namseox.mymusicproject.acitivity.TrackActivity
import com.namseox.mymusicproject.adapter.ItemPlaylistAdapter
import com.namseox.mymusicproject.databinding.FragmentPlaylistBinding
import com.namseox.mymusicproject.db.PlayListManager
import com.namseox.mymusicproject.helpers.Const
import com.namseox.mymusicproject.interfaces.getSong
import com.namseox.mymusicproject.media.MediaManager
import com.namseox.mymusicproject.model.Playlist
import com.namseox.mymusicproject.model.Song
import com.namseox.mymusicproject.services.MusicService
import com.squareup.picasso.Picasso


class FragmentPlaylist : Fragment(), getSong,View.OnClickListener{
private lateinit var binding :FragmentPlaylistBinding
private lateinit var itemPLAdapter : ItemPlaylistAdapter
    var listSong: ArrayList<Song> = ArrayList()
    var listPL: ArrayList<Playlist> = ArrayList()
    private var musicService = MusicService()
    private var data: ByteArray? = null
    private lateinit var mSong:Song

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistBinding.inflate(layoutInflater)
        binding.tvNamePl.setText(MediaManager.namePlaylist)

        val playlistManager = PlayListManager(requireContext())
        listPL = playlistManager.getAllPlaylist()
        for (pls:Playlist in listPL){
            if (pls.namePlaylist==MediaManager.namePlaylist){
                for (song:Song in MediaManager.mSongList){
                    if (song.path==pls.link){
                        listSong.add(song)
                        Log.d("...",listSong.size.toString())
                    }
                }

            }
        }
        itemPLAdapter = ItemPlaylistAdapter(listSong,this)
        binding.rclPlaylist.adapter = itemPLAdapter
        binding.rclPlaylist.layoutManager = LinearLayoutManager(FragmentPlaylist().context, LinearLayoutManager.VERTICAL, false)
        if (isMyServiceRunning(MusicService::class.java))
            showBottomLayout(true)
        else
            showBottomLayout(false)
        this.runnable.run()
        MediaManager.namePlaylist = ""
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.out.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentPlaylist_to_homeFragment2)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getClickCheckBox(song: Song) {
        TODO("Not yet implemented")
    }

    override fun playPlaylist(playlist: String) {
        TODO("Not yet implemented")
    }

    override fun getClickPlayList(song: Song,position:Int ) {
        MediaManager.intPlayMusic = 2
        mSong = song
        MediaManager.intNen = 2
        if (isMyServiceRunning(MusicService::class.java)) {
            val intent = Intent(requireContext(), MusicService::class.java)
            requireContext().startService(intent)
        }
        requireContext().bindService(
            Intent(requireContext(), MusicService::class.java),
            connection,
            Context.BIND_AUTO_CREATE
        )
        if (MediaManager.isChangePosition(position)) {
            MediaManager.setCurrentSong(position)
            val intent = Intent()
            intent.action = Const.ACTION_PLAY_NEW
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            musicService.playPauseSong(true, 2)
            binding!!.bottomMenu.tvBottomTitleSong.text =
                MediaManager.getCurrentSong().title
            binding!!.bottomMenu.tvBottomNameArtist.text =
                MediaManager.getCurrentSong().artistsNames
        }
        showBottomLayout(true)


//                    MediaManager.getCurrentSong().artistsNames
//                trackActivity.showBottomLayout(true)
//                mainActivity.setInforBottomLayout(
//                    list.get(position).getDisplayName(),
//                    list.get(position).getArtist()
//                )

    }
    fun showBottomLayout(isShow: Boolean) {
        if (isShow) {
            binding!!.bottomMenu.root.visibility = View.VISIBLE
        } else {
            binding!!.bottomMenu.root.visibility = View.GONE
        }
    }
    val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder: MusicService.MusicBinder = service as MusicService.MusicBinder
            musicService = binder.getMusicService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }

    }
    fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager =
            ContextCompat.getSystemService(
                requireContext(),
                MusicService::class.java
            ) as? ActivityManager
        if (manager == null) {
            return false
        } else {
            for (service in manager.getRunningServices(Int.MAX_VALUE)) {
                if (serviceClass.name == service.service.className) {
                    return true
                }
            }
            return false
        }
    }

    override fun onClick(v: View?) {
        val intent = Intent()
        when (v!!.id) {

            R.id.imv_next -> {
                intent.action = Const.ACTION_NEXT
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)


            }
            R.id.imv_previous -> {
                intent.action = Const.ACTION_PREVIOUS
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            }
            R.id.imv_pause_play -> {
                intent.action = Const.ACTION_PAUSE_SONG
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            }
            R.id.ll_detail_title_song -> {
                val clickIntent = Intent(requireContext(), TrackActivity::class.java)
                startActivity(clickIntent)

            }
        }
    }
    private var runnable: Runnable = object : Runnable {
        override fun run() {
//            if (mf != null) {
//                if (binding.bottomMenu.root.isVisible && mf!!.isVisible) {
//                    updateInforBottomLayout()
//                }
//            }
//            mf = myFragment?.findFragmentById(R.id.nav_home)
//            if (mf!= null) {
//                Log.d("doanpt", "Home fragment")
//            }
//            else
//                Log.d("doanpt", "Non Home Fragment")
//            if (binding.bottomMenu.root.isVisible)
            updateInforBottomLayout(MediaManager.intPlayMusic)
            Handler().postDelayed(this, 200)
        }
    }

    private fun updateInforBottomLayout(i:Int) {
//        if (isMyServiceRunning(MusicService::class.java))
//            showBottomLayout(false)
//        else
//            showBottomLayout(true)
        if(i!=1) {
            if (binding != null) {
                if (MediaManager.mediaPlayer.isPlaying) {
                    binding!!.bottomMenu.imvPausePlay.setImageResource(R.drawable.ic_baseline_pause_24)
                } else {
                    binding!!.bottomMenu.imvPausePlay.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                }
                if (MediaManager.getCurrentPossion() != -1) {

                    val mmr = MediaMetadataRetriever()
                    mmr.setDataSource(MediaManager.getCurrentSong().path)
                    data = mmr.embeddedPicture
                    if (data != null) {
                        val bitmap = BitmapFactory.decodeByteArray(data, 0, data!!.size)
                        binding!!.bottomMenu.imvImageSong.setImageBitmap(bitmap)
                    } else {
                        binding!!.bottomMenu.imvImageSong.setImageResource(R.drawable.ic_music)
                    }
                    binding!!.bottomMenu.imvImageSong.adjustViewBounds = true

                    binding!!.bottomMenu.tvBottomTitleSong.text =
                        MediaManager.getCurrentSong().title
                    binding!!.bottomMenu.tvBottomNameArtist.text =
                        MediaManager.getCurrentSong().artistsNames
                }
            }
        }else{
            if (MediaManager.getCurrentPossion() != -1) {
                var uri: Uri = Uri.parse(MediaManager.getCurrentSongOnl().thumbnail)
                if (uri != null) {
                    Picasso.get()
                        .load(uri)
                        .into(binding!!.bottomMenu.imvImageSong)

                } else {
                    binding!!.bottomMenu.imvImageSong.setImageResource(R.drawable.ic_music)
                }
                binding!!.bottomMenu.imvImageSong.adjustViewBounds = true

                binding!!.bottomMenu.tvBottomTitleSong.text =
                    MediaManager.getCurrentSongOnl().title
                binding!!.bottomMenu.tvBottomNameArtist.text =
                    MediaManager.getCurrentSongOnl().artistsNames
            }
        }

    }

}