package com.namseox.mymusicproject.acitivity.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.namseox.mymusicproject.adapter.PlaylistAdapter
import com.namseox.mymusicproject.databinding.FragmentAddPlaylistBinding
import com.namseox.mymusicproject.interfaces.getSong
import com.namseox.mymusicproject.media.MediaManager
import com.namseox.mymusicproject.model.Song


class FragmentAddPlaylist : Fragment(), getSong {
    private lateinit var binding : FragmentAddPlaylistBinding
    private lateinit var songRecyclerView : RecyclerView
    private lateinit var songAdapter : PlaylistAdapter
    private lateinit var listSong: ArrayList<Song>
//    private  val viewModel:AddPlaylistViewModel by activityViewModels{
//        AddPlaylistViewModelFagtory(app = Application())
//    }
//    fun getApplication(): Application {
//        return mApplication
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddPlaylistBinding.inflate(inflater,container,false)
//setSongRecycler()
        songAdapter = PlaylistAdapter(MediaManager.getMySongList() , this)
        binding.rclListoff.adapter = songAdapter
        binding.rclListoff.layoutManager = LinearLayoutManager(FragmentAddPlaylist().context, LinearLayoutManager.VERTICAL, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.out.setOnClickListener {
            findNavController().navigateUp()

        }
        binding.btnYes.setOnClickListener {
            for(song : Song in listSong){
//                val name = binding.tvNamePlaylist.text.toString()
//                viewModel.insertAddPlaylist(Playlist(namePlaylist = name,song.path))
            }
        }

    }

    override fun getClickCheckBox(song: Song) {
        listSong = ArrayList()
      listSong.add(song)

    }

    override fun playPlaylist(playlist: String) {
        TODO("Not yet implemented")
    }

    override fun getClickPlayList(song: Song, int: Int) {
        TODO("Not yet implemented")
    }


}