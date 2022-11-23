//package com.namseox.mymusicproject.acitivity.ui.home
//
//import android.app.ActivityManager
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ArrayAdapter
//import android.widget.ListView
//import android.widget.SearchView
//import androidx.core.content.ContextCompat
//import com.namseox.mymusicproject.R
//import com.namseox.mymusicproject.databinding.FragmentSearchBinding
//import com.namseox.mymusicproject.media.MediaManager
//import com.namseox.mymusicproject.services.MusicService
//
//
//class SearchFragment : Fragment() {
//    private lateinit var searchListView: ListView
//    lateinit var listAdapter: ArrayAdapter<String>
//    lateinit var searchView: SearchView
//    var listSearch: ArrayList<String> = ArrayList()
//    private lateinit var binding: FragmentSearchBinding
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        binding = FragmentSearchBinding.inflate(layoutInflater)
//        if (isMyServiceRunning(MusicService::class.java))
//            showBottomLayout(true)
//        else
//            showBottomLayout(false)
//        searchListView = requireView().findViewById(R.id.listView)
//        searchView = requireView().findViewById(R.id.search2)
//        listSearch.clear()
//        for (nameSong in MediaManager.getMySongList()) {
//            listSearch.add(nameSong.artistsNames)
//        }
//        listAdapter = ArrayAdapter<String>(
//            this,
//            android.R.layout.simple_list_item_1,
//            listSearch
//        )
//
//
//        return binding.root
//
//    }
//
//    fun showBottomLayout(isShow: Boolean) {
//        if (isShow) {
//            binding!!.bottomMenu.root.visibility = View.VISIBLE
//        } else {
//            binding!!.bottomMenu.root.visibility = View.GONE
//        }
//    }
//
//    fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
//        val manager =
//            ContextCompat.getSystemService(
//                requireContext(),
//                MusicService::class.java
//            ) as? ActivityManager
//        if (manager == null) {
//            return false
//        } else {
//            for (service in manager.getRunningServices(Int.MAX_VALUE)) {
//                if (serviceClass.name == service.service.className) {
//                    return true
//                }
//            }
//            return false
//        }
//    }
//
//}