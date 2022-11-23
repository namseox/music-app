package com.namseox.mymusicproject.adapter

import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.namseox.mymusicproject.databinding.ActivityAddPlaylistBinding.inflate
import com.namseox.mymusicproject.databinding.ItemSongBinding
import com.namseox.mymusicproject.databinding.ItemSongplaylistBinding
import com.namseox.mymusicproject.interfaces.getSong
import com.namseox.mymusicproject.model.Song

class ItemPlaylistAdapter(private val listSong: ArrayList<Song>, private val onCLick: getSong): RecyclerView.Adapter<ItemPlaylistAdapter.ViewHolder>( ) {
    class ViewHolder(val binding: ItemSongplaylistBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind (item : Song){
            binding.tvNamesong.text = item.title
            binding.tvNamesinger.text = item.artistsNames
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
    override fun getItemCount(): Int= listSong.count()



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listSong[position])

        holder.binding.songPlayList.setOnClickListener {
            onCLick.getClickPlayList(listSong[position], position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ItemPlaylistAdapter.ViewHolder(
            ItemSongplaylistBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

}