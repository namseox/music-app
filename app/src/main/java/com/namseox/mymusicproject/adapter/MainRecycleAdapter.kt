package com.namseox.mymusicproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.namseox.mymusicproject.R
import com.namseox.mymusicproject.acitivity.ui.home.HomeViewModel
import com.namseox.mymusicproject.interfaces.IBaseAdapter
import com.namseox.mymusicproject.interfaces.ICategoryItemClickListener
import com.namseox.mymusicproject.model.AllCategory
import com.namseox.mymusicproject.model.Song

class MainRecycleAdapter(
    private val context: Context,
    private val adapterInterface: IBaseAdapter<AllCategory>,
    private val iCategoryItemClickListener: ICategoryItemClickListener
) :
    RecyclerView.Adapter<MainRecycleAdapter.MainViewHolder>() {

    private lateinit var homeViewModel: HomeViewModel

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var categoryTitle: TextView? = null
        var itemRecycler: RecyclerView

        init {
            categoryTitle = itemView.findViewById(R.id.cat_title)
            itemRecycler = itemView.findViewById(R.id.cat_item_recycler)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            LayoutInflater.from(context).inflate(R.layout.main_recycler_row_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val data = adapterInterface.getData(position)
//        when (Const.SORT_A) {
//            1 -> holder.categoryTitle!!.text = data.categoryTitle
//            1 -> sort(data)
//                .....
//        }
        holder.categoryTitle!!.text = data.categoryTitle
        setCatItemRecycler(holder.itemRecycler, data.categoryItem)


    }

    override fun getItemCount(): Int = adapterInterface.getItemCount()

    private fun setCatItemRecycler(recyclerView: RecyclerView, categoryItem: List<Song>) {

        val itemRecyclerAdapter =
            CategoryItemAdapter(context, categoryItem, iCategoryItemClickListener)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recyclerView.adapter = itemRecyclerAdapter

    }




}