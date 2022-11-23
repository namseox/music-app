package com.namseox.mymusicproject.acitivity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.google.android.material.snackbar.Snackbar
import com.namseox.mymusicproject.R
import com.namseox.mymusicproject.databinding.ActivityNavigationDrawerBinding
import com.namseox.mymusicproject.helpers.Const

class NavigationDrawer : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityNavigationDrawerBinding
//    private  var binHomeFragment =HomeFragment()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNavigationDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarNavigationDrawer.toolbar)

        binding.appBarNavigationDrawer.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
        }

        val navController = findNavController(R.id.nav_host_fragment_content_navigation_drawer)



    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_offline, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {

        val navController = findNavController(R.id.nav_host_fragment_content_navigation_drawer)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sort_a -> {

            }
            R.id.sort_z -> {

            }
            R.id.time -> {

            }
            R.id.sleep_timer -> {
                showSleepTimer()
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

//    private fun launchUpdate() {
//        MediaManager.getAllSongFromStorage(this)
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.nav_home, binHomeFragment.javaClass.newInstance())
//            .commit()
//
//    }

    private fun showSleepTimer() {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        val intent = Intent()
        intent.action = Const.ACTION_STOP
        stopService(intent)
        super.onDestroy()


    }
}