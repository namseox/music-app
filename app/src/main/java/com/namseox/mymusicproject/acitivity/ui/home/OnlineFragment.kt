package com.namseox.mymusicproject.acitivity.ui.home

import android.app.ActivityManager
import android.content.*
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.namseox.mymusicproject.R
import com.namseox.mymusicproject.acitivity.TrackActivity
import com.namseox.mymusicproject.adapter.MainRecycleAdapter
import com.namseox.mymusicproject.databinding.FragmentOnlineBinding
import com.namseox.mymusicproject.helpers.Const
import com.namseox.mymusicproject.interfaces.IBaseAdapter
import com.namseox.mymusicproject.interfaces.ICategoryItemClickListener
import com.namseox.mymusicproject.media.MediaManager
import com.namseox.mymusicproject.model.AllCategory
import com.namseox.mymusicproject.model.Song
import com.namseox.mymusicproject.services.MusicService
import com.squareup.picasso.Picasso
import kotlin.random.Random


class OnlineFragment : Fragment(), View.OnClickListener {

    private var data: ByteArray? = null
    private lateinit var homeViewModel: OnlViewModel
    private var _binding: FragmentOnlineBinding? = null
    private var mainCategoryRecycler: RecyclerView? = null
    private lateinit var mainRecyclerAdaper: MainRecycleAdapter
    private val songCharts2: MutableList<Song> = mutableListOf()
    private val songCharts3: MutableList<Song> = mutableListOf()
    private val songCharts4: MutableList<Song> = mutableListOf()
    private var pos: Int = 0

    //    private val recommendedSong: MutableList<Song> = mutableListOf()
    private val allCategory: MutableList<AllCategory> = mutableListOf()
    private val intentFilter = IntentFilter()
    private val updatePlayNewSong = UpdatePlayNewSong()

    //    val myFragment: FragmentManager? = null
//    var mf: Fragment? = null
    var isFirstRun: Boolean = true


    //    private var trackActivity: TrackActivity? = null
    private var musicService = MusicService()


    var root: View? = null
//    var mSongList: List<Song> = emptyList()


    private val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        homeViewModel =
            ViewModelProvider(this).get(OnlViewModel::class.java)

        _binding = FragmentOnlineBinding.inflate(inflater, container, false)
        root = binding!!.root



        setMainCategoryRecycler()
//        initComponent()
        setOnClick()
        if (isMyServiceRunning(MusicService::class.java))
            showBottomLayout(true)
        else
            showBottomLayout(false)


        this.runnable.run()

        intentFilter.addAction(Const.ACTION_SEND_DATA)
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(updatePlayNewSong, intentFilter)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.getSong()
        homeViewModel.songCharts.observe(viewLifecycleOwner) {
//            songCharts2.clear()
            if (it != null) {
                songCharts2.addAll(it)
                MediaManager.listSong = it
            }
            allCategory.add(AllCategory("Music Online", songCharts2))
            songCharts2.forEach {


            }
            mainRecyclerAdaper.notifyDataSetChanged()
        }
        //the loai nhac
        homeViewModel.songCharts.observe(viewLifecycleOwner) {
//            songCharts2.clear()
            if (it != null) {

                MediaManager.listSong = it
            }
            for (i: Int in 0..songCharts2.count()) {
                if (i < 100) {
                    var x: Int = Random.nextInt(0, songCharts2.count())
                    songCharts3.addAll(listOf(songCharts2[x]))
                }
            }
            allCategory.add(AllCategory("Top 100", songCharts3))

//            mainRecyclerAdaper.notifyDataSetChanged()
        }
        homeViewModel.songCharts.observe(viewLifecycleOwner) {

            if (it != null) {

                MediaManager.listSong = it
            }
            for (i: Int in 0..songCharts2.count()) {

                var x: Int = Random.nextInt(0, songCharts2.count())
                songCharts4.addAll(listOf(songCharts2[x]))
            }
            allCategory.add(AllCategory("Nhac pop", songCharts4))

//            mainRecyclerAdaper.notifyDataSetChanged()
        }
    }
//    private fun initComponent() {

//        _binding!!.mainRecycler.adapter = mainRecyclerAdaper
//        homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
//        homeViewModel.songCharts.observe(viewLifecycleOwner) {
//
//
//
//        }
//        homeViewModel.recommenedSong.observe(viewLifecycleOwner) {
//            recommendedSong.clear()
//            if (it != null) {
//                recommendedSong.addAll(it)
//            }
//            allCategory.add(AllCategory("Recommended For You", recommendedSong))
//            mainRecyclerAdaper.notifyDataSetChanged()
//
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
                MediaManager.intPlayMusic = 1
                MediaManager.intNen = 1
                pos = position
                Log.d("bbb", position.toString() + "---" + pos + "   " + position)
                if (isMyServiceRunning(MusicService::class.java)) {
                    val intent = Intent(requireContext(), MusicService::class.java)
                    intent.putExtra("string param 1", "online");
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
                    musicService.playPauseSong(true, 1)
                    binding!!.bottomMenu.tvBottomTitleSong.text =
                        MediaManager.getCurrentSongOnl().title
                    binding!!.bottomMenu.tvBottomNameArtist.text =
                        MediaManager.getCurrentSongOnl().artistsNames
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
            ContextCompat.getSystemService(
                requireContext(),
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
            Log.d("nnn", i.toString())
            if (i != 1 ) {
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
                    var uri: Uri = Uri.parse(songCharts2[pos].thumbnail)
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


}