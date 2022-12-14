package com.namseox.mymusicproject.acitivity.ui.liked_song

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.namseox.mymusicproject.databinding.FragmentLikedSongBinding

class LikedSongFragment : Fragment() {

    private lateinit var likedSongViewModel: LikedSongViewModel
    private var _binding: FragmentLikedSongBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        likedSongViewModel =
            ViewModelProvider(this).get(LikedSongViewModel::class.java)

        _binding = FragmentLikedSongBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textLikedSong
        likedSongViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}