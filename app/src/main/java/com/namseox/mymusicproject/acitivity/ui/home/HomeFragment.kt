package com.namseox.mymusicproject.acitivity.ui.home

import android.Manifest
import android.app.ActivityManager
import android.content.*
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.namseox.mymusicproject.R
import com.namseox.mymusicproject.acitivity.TrackActivity
import com.namseox.mymusicproject.acitivity.ui.playlist.AddPlaylistActivity
import com.namseox.mymusicproject.adapter.MainRecycleAdapter
import com.namseox.mymusicproject.adapter.PlaylistOffRecycleAdapter
import com.namseox.mymusicproject.databinding.FragmentHomeBinding
import com.namseox.mymusicproject.db.PlayListManager
import com.namseox.mymusicproject.helpers.Const
import com.namseox.mymusicproject.interfaces.IBaseAdapter
import com.namseox.mymusicproject.interfaces.ICategoryItemClickListener
import com.namseox.mymusicproject.interfaces.ILongClickPlaylist
import com.namseox.mymusicproject.interfaces.getSong
import com.namseox.mymusicproject.media.MediaManager
import com.namseox.mymusicproject.model.AllCategory
import com.namseox.mymusicproject.model.Playlist
import com.namseox.mymusicproject.model.Song
import com.namseox.mymusicproject.services.MusicService
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment(), View.OnClickListener, getSong, ILongClickPlaylist {

    private var data: ByteArray? = null

    //    private lateinit var homeViewModel: OnlViewModel
    private var _binding: FragmentHomeBinding? = null
    private var mainCategoryRecycler: RecyclerView? = null
    private lateinit var playlistRecyclerView: RecyclerView
    private lateinit var playlistSongAdapter: PlaylistOffRecycleAdapter
    private lateinit var mainRecyclerAdaper: MainRecycleAdapter
    private val songCharts2: MutableList<Song> = mutableListOf()
    private val recommendedSong: MutableList<Song> = mutableListOf()
    private val allCategory: MutableList<AllCategory> = mutableListOf()
    private val intentFilter = IntentFilter()
    private val updatePlayNewSong = UpdatePlayNewSong()
    var playList: ArrayList<Playlist>? = arrayListOf()
    var listName: ArrayList<String>? = arrayListOf()
    var isFirstRun: Boolean = true
    val intent = Intent()
    private lateinit var homeViewModel: HomeViewModel


    //    private var trackActivity: TrackActivity? = null
    private var musicService = MusicService()


    var root: View? = null
    var mSongList: List<Song> = emptyList()


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        root = binding!!.root

        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.getAllPlaylist(requireContext())
        homeViewModel.getNamePlaylist(requireContext())
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE

            ) != PackageManager.PERMISSION_DENIED
        ) {
            mSongList = MediaManager.getAllSongFromStorage(requireContext())
            MediaManager.mSongList = mSongList as ArrayList<Song>
            homeViewModel.getAllSongOffline()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                123
            )
        }

//        this.trackActivity = TrackActivity()

        binding!!.toolbar.inflateMenu(R.menu.menu_offline)
        binding!!.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.sort_a -> {
                    Log.d("ttt", MediaManager.sort.toString())
                    MediaManager.sort = 1
                    dataOffline()

                    true
                }
                R.id.sort_z -> {
                    MediaManager.sort = 2
                    dataOffline()

                    true
                }
                R.id.sort_time -> {
                    MediaManager.sort = 3
                    dataOffline()

                    true
                }
                R.id.search -> {
                    intent.setClass(requireActivity(), SearchActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> {
                    super.onOptionsItemSelected(it)
                }
            }

        }
        dataOffline()

        setMainCategoryRecycler()
        setPlaylistRecycler()

        setOnClick()
        if (isMyServiceRunning(MusicService::class.java))
            showBottomLayout(true)
        else
            showBottomLayout(false)


        this.runnable.run()

        intentFilter.addAction(Const.ACTION_SEND_DATA)
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(updatePlayNewSong, intentFilter)


        //not working with MarqueeTextView
//        bindingRowItemBinding = CatRowItemBinding.inflate(layoutInflater)
//        offCatTv = bindingRowItemBinding.tvTitle
//        offCatTv!!.isSelected = true
//        offCatTv!!.setHorizontallyScrolling(true)
//        val tabLayout = view?.findViewById<TabLayout>(R.id.viewPaper)
//        val viewPaper2 = view?.findViewById<ViewPager2>(R.id.pager)
//        val adapter = PageAdapter(childFragmentManager,lifecycle)
//        viewPaper2?.adapter =adapter
//        TabLayoutMediator(tabLayout!!,viewPaper2!!){ tab, posion ->
//            when(posion){
//                0 ->{
//                    tab.text = "Playlist"
//                }
//                1->{
//                    tab.text = "Album"
//                }
//                2->{
//                    tab.text = "Singer"
//                }
//            }
//
//        }.attach()


        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.menu_offline,menu)
//        super.onCreateOptionsMenu(menu, inflater)
//    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        _binding = FragmentHomeBinding.inflate(layoutInflater)
//        setMainCategoryRecycler()
//        homeViewModel.getSongCharts()
//        homeViewModel.getRecommendedSong()
//        setMainCategoryRecycler()
//        initComponent()

        homeViewModel.namePlaylist.observe(viewLifecycleOwner) {
            listName!!.clear()
            listName!!.addAll(it)

            Log.d("kkk", "onViewCreated: ${listName!!.size}")
            Log.d("kkk", it.size.toString())
            setPlaylistRecycler()


        }
        homeViewModel.allPlaylist.observe(viewLifecycleOwner) {
            playList!!.clear()
            playList!!.addAll(it)
            Log.d("qwer3", it.size.toString())
            setPlaylistRecycler()


        }
        homeViewModel.allSonglistOffline.observe(viewLifecycleOwner) {
            allCategory.clear()
            allCategory.add(AllCategory("Offline", it))
            initMainCate()
        }
        binding!!.imgAddPlaylist.setOnClickListener {


            intent.setClass(requireActivity(), AddPlaylistActivity::class.java)
            startActivity(intent)

        }
    }
//    fun replaceFragmentContent(fragment : Fragment ){
//        if (fragment != null) {
//            val fm = requireActivity().supportFragmentManager
//            val transaction: FragmentTransaction = fm.beginTransaction()
//            transaction.replace(R.id.content_frame, fragment)
//            transaction.addToBackStack(null)
//            transaction.commit()
//        }
//    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setMainCategoryRecycler() {

        mainCategoryRecycler = root!!.findViewById(R.id.main_recycler)
        val layoutManager = LinearLayoutManager(context)
        mainCategoryRecycler!!.layoutManager = layoutManager
        initMainCate()
    }

    private fun setPlaylistRecycler() {
        playlistRecyclerView = root!!.findViewById(R.id.rcl_playlist)
        playlistSongAdapter = PlaylistOffRecycleAdapter(listName, this, this)
        playlistRecyclerView.setHasFixedSize(true);
        val gridLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        playlistRecyclerView.setAdapter(playlistSongAdapter)
        playlistRecyclerView.setLayoutManager(gridLayoutManager)



    }


    private fun initMainCate() {
        mainRecyclerAdaper = MainRecycleAdapter(requireContext(), object :
            IBaseAdapter<AllCategory> {
            override fun getItemCount(): Int = allCategory.size

            override fun getData(position: Int): AllCategory = allCategory[position]

            override fun onClickItem(position: Int) {
                TODO("Not yet implemented")
            }

        }, object : ICategoryItemClickListener {
            override fun playSong(araList: List<Song>, position: Int) {
                MediaManager.intPlayMusic = 2
                MediaManager.intNen = 2
                if (isMyServiceRunning(MusicService::class.java)) {
                    val intent = Intent(requireContext(), MusicService::class.java)
                    requireContext().startService(intent)
                }
                requireContext().bindService(
                    Intent(requireContext(), MusicService::class.java),
                    connection,
                    Context.BIND_AUTO_CREATE
                )
                if (MediaManager.isChangePosition(position)) {
                    MediaManager.setCurrentSong(position)
                    val intent = Intent()
                    intent.action = Const.ACTION_PLAY_NEW
                    LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
                    musicService.playPauseSong(true, 2)
                    binding!!.bottomMenu.tvBottomTitleSong.text =
                        MediaManager.getCurrentSong().title
                    binding!!.bottomMenu.tvBottomNameArtist.text =
                        MediaManager.getCurrentSong().artistsNames
                }
                showBottomLayout(true)


//                    MediaManager.getCurrentSong().artistsNames
//                trackActivity.showBottomLayout(true)
//                mainActivity.setInforBottomLayout(
//                    list.get(position).getDisplayName(),
//                    list.get(position).getArtist()
//                )
            }

        })
        mainCategoryRecycler!!.adapter = mainRecyclerAdaper

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 123) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                runMusicActivity()
                MediaManager.getAllSongFromStorage(requireContext())
            } else {
                Toast.makeText(requireContext(), "Not have Permission", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun dataOffline() {
        var categoryItemList: MutableList<Song> = ArrayList()
        for (item in mSongList) {
            categoryItemList.add(item)

        }

        when (MediaManager.sort) {
            1 -> {
                categoryItemList =
                    categoryItemList.sortedWith(compareBy(Song::fileName)).toMutableList()
                MediaManager.mSongList = categoryItemList as ArrayList<Song>
                homeViewModel.getAllSongOffline()

            }
            2 -> {
                categoryItemList =
                    categoryItemList.sortedWith(compareBy(Song::fileName)).toMutableList()
                Collections.reverse(categoryItemList)
                MediaManager.mSongList = categoryItemList as ArrayList<Song>
                homeViewModel.getAllSongOffline()

            }
            else -> {
                homeViewModel.getAllSongOffline()

            }

        }




    }


    val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder: MusicService.MusicBinder = service as MusicService.MusicBinder
            musicService = binder.getMusicService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }

    }

    fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager =
            getSystemService(requireContext(), MusicService::class.java) as? ActivityManager
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

    fun showBottomLayout(isShow: Boolean) {
        if (isShow) {
            binding!!.bottomMenu.root.visibility = View.VISIBLE
        } else {
            binding!!.bottomMenu.root.visibility = View.GONE
        }
    }

    private var runnable: Runnable = object : Runnable {
        override fun run() {
//            if (mf != null) {
//                if (binding.bottomMenu.root.isVisible && mf!!.isVisible) {
//                    updateInforBottomLayout()
//                }
//            }
//            mf = myFragment?.findFragmentById(R.id.nav_home)
//            if (mf!= null) {
//                Log.d("doanpt", "Home fragment")
//            }
//            else
//                Log.d("doanpt", "Non Home Fragment")
//            if (binding.bottomMenu.root.isVisible)
            updateInforBottomLayout(MediaManager.intPlayMusic)
            Handler().postDelayed(this, 200)
        }
    }

    private fun updateInforBottomLayout(i: Int) {
//        if (isMyServiceRunning(MusicService::class.java))
//            showBottomLayout(false)
//        else
//            showBottomLayout(true)
        if (binding != null) {
            if (MediaManager.mediaPlayer.isPlaying) {
                binding!!.bottomMenu.imvPausePlay.setImageResource(R.drawable.ic_baseline_pause_24)
            } else {
                binding!!.bottomMenu.imvPausePlay.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            }
            if (i != 1) {
                if (MediaManager.getCurrentPossion() != -1) {

                    val mmr = MediaMetadataRetriever()
                    mmr.setDataSource(MediaManager.getCurrentSong().path)
                    data = mmr.embeddedPicture
                    if (data != null) {
                        val bitmap = BitmapFactory.decodeByteArray(data, 0, data!!.size)
                        binding!!.bottomMenu.imvImageSong.setImageBitmap(bitmap)
                    } else {
                        binding!!.bottomMenu.imvImageSong.setImageResource(R.drawable.ic_music)
                    }
                    binding!!.bottomMenu.imvImageSong.adjustViewBounds = true

                    binding!!.bottomMenu.tvBottomTitleSong.text =
                        MediaManager.getCurrentSong().title
                    binding!!.bottomMenu.tvBottomNameArtist.text =
                        MediaManager.getCurrentSong().artistsNames
                }
            } else {

                if (MediaManager.getCurrentPossion() != -1) {
                    var uri: Uri = Uri.parse(MediaManager.getCurrentSongOnl().thumbnail)
                    if (uri != null) {
                        Picasso.get()
                            .load(uri)
                            .into(binding!!.bottomMenu.imvImageSong)

                    } else {
                        binding!!.bottomMenu.imvImageSong.setImageResource(R.drawable.ic_music)
                    }
                    binding!!.bottomMenu.imvImageSong.adjustViewBounds = true

                    binding!!.bottomMenu.tvBottomTitleSong.text =
                        MediaManager.getCurrentSongOnl().title
                    binding!!.bottomMenu.tvBottomNameArtist.text =
                        MediaManager.getCurrentSongOnl().artistsNames
                }
            }

        }
    }

    private fun setOnClick() {
        binding!!.bottomMenu.imvPausePlay.setOnClickListener(this)
        binding!!.bottomMenu.imvNext.setOnClickListener(this)
        binding!!.bottomMenu.imvPrevious.setOnClickListener(this)
        binding!!.bottomMenu.llDetailTitleSong.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val intent = Intent()
        when (v!!.id) {

            R.id.imv_next -> {
                intent.action = Const.ACTION_NEXT
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)


            }
            R.id.imv_previous -> {
                intent.action = Const.ACTION_PREVIOUS
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            }
            R.id.imv_pause_play -> {
                intent.action = Const.ACTION_PAUSE_SONG
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            }
            R.id.ll_detail_title_song -> {
                val clickIntent = Intent(requireContext(), TrackActivity::class.java)
                startActivity(clickIntent)

            }
        }
    }


    open inner class UpdatePlayNewSong : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                Const.ACTION_SEND_DATA -> setInforBottomLayout(
                    intent.getStringExtra(Const.KEY_TITLE_SONG),
                    intent.getStringExtra(Const.KEY_NAME_ARTIST)
                )
            }
        }
    }

    fun setInforBottomLayout(nameSong: String?, nameArtist: String?) {
        if (binding != null) {
            binding!!.bottomMenu.tvBottomTitleSong.text = nameSong
            binding!!.bottomMenu.tvBottomNameArtist.text = nameArtist
        }
    }


    override fun onDestroy() {

        super.onDestroy()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(updatePlayNewSong)
        if (isMyServiceRunning(MusicService::class.java)) {
            requireContext().unbindService(connection)
        }
    }


    override fun onResume() {
        musicService.setContextFromMS(requireContext())
        super.onResume()
        if (MediaManager.mediaPlayer.isPlaying)
            isFirstRun = false
        if (isFirstRun)
            showBottomLayout(false)
        else {
            showBottomLayout(true)

        }

    }

    override fun onDetach() {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(updatePlayNewSong)
        if (isMyServiceRunning(MusicService::class.java)) {
            requireContext().unbindService(connection)
        }
        super.onDetach()
    }

    override fun getClickCheckBox(song: Song) {
        TODO("Not yet implemented")
    }

    override fun playPlaylist(playlist: String) {
        MediaManager.namePlaylist = playlist
        Log.d("...", playlist)
        findNavController().navigate(R.id.action_homeFragment2_to_fragmentPlaylist)


    }

    override fun getClickPlayList(song: Song, int: Int) {
        TODO("Not yet implemented")
    }

    override fun longClickPlaylist(posion: Int, view: View, listName: String): Boolean {
        performOptionsMenuClick(posion, view, listName)
        Log.d("ooo", "ooo")
        return true
    }

    private fun performOptionsMenuClick(position: Int, view: View, listName: String) {
        val popMenu = PopupMenu(context, view)
        popMenu.inflate(R.menu.menu_playlist)

        popMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when (item?.itemId) {
                    R.id.delete_Playlist -> {
                        val playlistManager = PlayListManager(requireContext())
                        playlistManager.deletePlayList(listName)
                        homeViewModel.getNamePlaylist(requireContext())
                        homeViewModel.getAllPlaylist(requireContext())
                        return true
                    }
                    R.id.add_Playlist -> {
                        MediaManager.namePlaylist = listName
                        intent.setClass(requireActivity(), AddPlaylistActivity::class.java)
                        startActivity(intent)
                        return true
                    }
                }
                return false
            }
        })
        popMenu.show()
    }

}


