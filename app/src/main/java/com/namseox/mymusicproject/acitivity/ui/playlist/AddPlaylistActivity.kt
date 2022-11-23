package com.namseox.mymusicproject.acitivity.ui.playlist

//import com.namseox.mymusicproject.acitivity.ui.home.AddPlaylistViewModel
//import com.namseox.mymusicproject.acitivity.ui.home.AddPlaylistViewModelFagtory

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.namseox.mymusicproject.MainActivity
import com.namseox.mymusicproject.R
import com.namseox.mymusicproject.adapter.PlaylistAdapter
import com.namseox.mymusicproject.databinding.ActivityAddPlaylistBinding
import com.namseox.mymusicproject.db.PlayListManager
import com.namseox.mymusicproject.interfaces.getSong
import com.namseox.mymusicproject.media.MediaManager
import com.namseox.mymusicproject.model.Playlist
import com.namseox.mymusicproject.model.Song


class AddPlaylistActivity : AppCompatActivity(), getSong {

    private lateinit var binding: ActivityAddPlaylistBinding

    private lateinit var songAdapter: PlaylistAdapter
    val listSong: ArrayList<Song> = ArrayList()

//    private  val viewModel: AddPlaylistViewModel by viewModels{
//        AddPlaylistViewModelFagtory(application)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)
//setSongRecycler()
        if(MediaManager.namePlaylist!!.isNotEmpty()){
            binding.tvNamePlaylist.setText(MediaManager.namePlaylist)
            MediaManager.namePlaylist = ""
        }else{
            binding.tvNamePlaylist.setText("Name Playlist")
        }

        songAdapter = PlaylistAdapter(MediaManager.getMySongList(), this)
        binding.rclListoff.adapter = songAdapter
        binding.rclListoff.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val playlistManager = PlayListManager(this)
        binding.out.setOnClickListener {
            finish()
        }

        binding.btnYes.setOnClickListener {
//            for(song : Song in listSong){
//                val name = binding.tvNamePlaylist.text.toString()
//                viewModel.insertAddPlaylist(Playlist(namePlaylist = name,song.path))
//            }
            for (song: Song in listSong) {
                val name = binding.tvNamePlaylist.text.toString()
                playlistManager.addPlayList(Playlist(namePlaylist = name, song.path))
            }

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    override fun getClickCheckBox(song: Song) {
        val checkbox: CheckBox = findViewById(R.id.cb_playlist)
        if (checkbox.isChecked) {
            listSong.add(song)
        } else {
            listSong.remove(song)
        }
    }

    override fun playPlaylist(playlist: String) {
        TODO("Not yet implemented")
    }

    override fun getClickPlayList(song: Song, int: Int) {
        TODO("Not yet implemented")
    }


}