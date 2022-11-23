package com.namseox.mymusicproject.interfaces

import com.namseox.mymusicproject.model.Song

interface IBaseAdapter<D> {
    fun getItemCount(): Int
    fun getData(position: Int): D
    fun onClickItem(position: Int)

}