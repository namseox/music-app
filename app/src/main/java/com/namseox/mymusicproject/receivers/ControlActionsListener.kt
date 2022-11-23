package com.namseox.mymusicproject.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.namseox.mymusicproject.extensions.sendIntent
import com.namseox.mymusicproject.util.FINISH
import com.namseox.mymusicproject.util.NEXT
import com.namseox.mymusicproject.util.PLAYPAUSE
import com.namseox.mymusicproject.util.PREVIOUS

class ControlActionsListener : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        when (action) {
            PREVIOUS, PLAYPAUSE, NEXT, FINISH -> context.sendIntent(action)
        }
    }
}
