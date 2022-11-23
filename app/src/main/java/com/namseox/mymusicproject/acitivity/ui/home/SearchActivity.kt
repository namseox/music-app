package com.namseox.mymusicproject.acitivity.ui.home

import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.namseox.mymusicproject.MainActivity
import com.namseox.mymusicproject.R
import com.namseox.mymusicproject.databinding.ActivitySearchBinding
import com.namseox.mymusicproject.helpers.Const
import com.namseox.mymusicproject.media.MediaManager
import com.namseox.mymusicproject.services.MusicService

class SearchActivity : AppCompatActivity() {
    private lateinit var dialog: AlertDialog
    private lateinit var searchListView: ListView
    lateinit var listAdapter: ArrayAdapter<String>
    lateinit var searchView: SearchView
    var listSearch: ArrayList<String> = ArrayList()
    private lateinit var binding: ActivitySearchBinding
    private var musicService = MusicService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (isMyServiceRunning(MusicService::class.java))
            showBottomLayout(true)
        else
            showBottomLayout(false)
        searchListView = findViewById(R.id.listView)
        searchView = findViewById(R.id.search2)
        listSearch.clear()
        for (nameSong in MediaManager.getMySongList()) {
            listSearch.add(nameSong.title)
        }
        listAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            listSearch
        )
        searchListView.adapter = listAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (listSearch.contains(query)) {
                    listAdapter.filter.filter(query)
                } else {

                    Toast.makeText(this@SearchActivity, "not data..", Toast.LENGTH_LONG)
                        .show()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                listAdapter.filter.filter(newText)
                return false
            }

        })
        val connection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val binder: MusicService.MusicBinder = service as MusicService.MusicBinder
                musicService = binder.getMusicService()
            }

            override fun onServiceDisconnected(name: ComponentName?) {

            }

        }
        searchListView.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                MediaManager.intPlayMusic = 2
                for (song in MediaManager.getMySongList()){

                }
                MediaManager.intNen = 2
                if (isMyServiceRunning(MusicService::class.java)) {
                    val intent = Intent(this@SearchActivity, MusicService::class.java)
                    this@SearchActivity.startService(intent)
                }
                this@SearchActivity.bindService(
                    Intent(this@SearchActivity, MusicService::class.java),
                    connection,
                    Context.BIND_AUTO_CREATE
                )
                if (MediaManager.isChangePosition(position)) {
                    MediaManager.setCurrentSong(position)
                    val intent = Intent()
                    intent.action = Const.ACTION_PLAY_NEW
                    LocalBroadcastManager.getInstance(this@SearchActivity).sendBroadcast(intent)
                    musicService.playPauseSong(true, 2)
                    binding!!.bottomMenu.tvBottomTitleSong.text =
                        MediaManager.getCurrentSong().title
                    binding!!.bottomMenu.tvBottomNameArtist.text =
                        MediaManager.getCurrentSong().artistsNames
                }
                showBottomLayout(true)
                dialog.dismiss()
            }


        })
        binding.imgBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
    fun showBottomLayout(isShow: Boolean) {
        if (isShow) {
            binding!!.bottomMenu.root.visibility = View.VISIBLE
        } else {
            binding!!.bottomMenu.root.visibility = View.GONE
        }
    }

    fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager =
            ContextCompat.getSystemService(
                this,
                MusicService::class.java
            ) as? ActivityManager
        if (manager == null) {
            return false
        } else {
            for (service in manager.getRunningServices(Int.MAX_VALUE)) {
                if (serviceClass.name == service.service.className) {
                    return true
                }
            }
            return false
        }
    }


}