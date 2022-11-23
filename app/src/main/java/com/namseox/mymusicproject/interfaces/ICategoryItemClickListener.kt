package com.namseox.mymusicproject.interfaces

import com.namseox.mymusicproject.model.Song

interface ICategoryItemClickListener {
//        fun onClickCategoryItem(view: View, isLongPress: Boolean)
        fun playSong(araList: List<Song>, position: Int)

}