package com.namseox.mymusicproject.adapter

import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.namseox.mymusicproject.R
import com.namseox.mymusicproject.databinding.ItemPlaylistBinding
import com.namseox.mymusicproject.databinding.ItemSongBinding
import com.namseox.mymusicproject.interfaces.ILongClickPlaylist
import com.namseox.mymusicproject.interfaces.getSong
import com.namseox.mymusicproject.model.AllCategory
import com.namseox.mymusicproject.model.Playlist
import com.namseox.mymusicproject.model.Song
import java.util.ArrayList

class PlaylistOffRecycleAdapter(val listName: ArrayList<String>?, private val onCLick: getSong, private val onLongClick: ILongClickPlaylist) : RecyclerView.Adapter<PlaylistOffRecycleAdapter.ViewHolder>( ){
    class ViewHolder(val binding: ItemPlaylistBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind (item : String){
            binding.tvPlaylists.text = item
        }
    }




    override fun getItemCount(): Int = listName!!.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return PlaylistOffRecycleAdapter.ViewHolder(
            ItemPlaylistBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listName!![position])

        holder.binding.itemPlaylist.setOnClickListener {
            onCLick.playPlaylist(listName[position])
        }
        holder.binding.itemPlaylist.setOnLongClickListener {
            onLongClick.longClickPlaylist(position, it, listName[position])
        }

    }


}
