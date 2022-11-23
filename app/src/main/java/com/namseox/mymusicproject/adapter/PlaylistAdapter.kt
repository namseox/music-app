package com.namseox.mymusicproject.adapter

import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.namseox.mymusicproject.R
import com.namseox.mymusicproject.databinding.ItemSongBinding
import com.namseox.mymusicproject.interfaces.IBaseAdapter
import com.namseox.mymusicproject.interfaces.ILongClickPlaylist
import com.namseox.mymusicproject.interfaces.getSong
import com.namseox.mymusicproject.model.Song

class PlaylistAdapter(private val listSong: ArrayList<Song> , private val onCLick: getSong): RecyclerView.Adapter<PlaylistAdapter.ViewHolder>( ) {
    class ViewHolder(val binding: ItemSongBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind (item : Song){
            binding.tvSong.text = item.title
            val mmr = MediaMetadataRetriever()
        mmr.setDataSource(item.path)
        var data: ByteArray = mmr.embeddedPicture!!
        if (data != null) {
            val bitmap = BitmapFactory.decodeByteArray(data, 0, data!!.size)
            binding.imgImg.setImageBitmap(bitmap)
        }
        binding.imgImg.adjustViewBounds = false

        }
    }


//    override fun onBindViewHolder(holder: Songs, position: Int) {
//        holder.tvPlaylist.text = songList[position].title
//        val mmr = MediaMetadataRetriever()
//        mmr.setDataSource(songList[position].path)
//        var data: ByteArray = mmr.embeddedPicture!!
//        if (data != null) {
//            val bitmap = BitmapFactory.decodeByteArray(data, 0, data!!.size)
//            holder.img.setImageBitmap(bitmap)
//        } else {
//            holder.img.setImageResource(R.drawable.ic_music)
//        }
//        holder.img.adjustViewBounds = false
//    }

    override fun getItemCount(): Int= listSong.count()
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listSong[position])

        holder.binding.cbPlaylist.setOnClickListener {
            onCLick.getClickCheckBox(listSong[position])
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


}
