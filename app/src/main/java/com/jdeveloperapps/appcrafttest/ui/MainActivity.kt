package com.jdeveloperapps.appcrafttest.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.jdeveloperapps.appcrafttest.R
import com.jdeveloperapps.appcrafttest.util.Constants.ACTION_SHOW_LOCATION_FRAGMENT
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_menu.setupWithNavController(main_nav_host_fragment.findNavController())

        navigateToLocationFragmentIfNeeded(intent)
    }

    private fun navigateToLocationFragmentIfNeeded(intent: Intent?) {
        if (intent?.action == ACTION_SHOW_LOCATION_FRAGMENT) {
            main_nav_host_fragment.findNavController().navigate(R.id.action_global_locationFragment)
            intent.action = ""
        }
    }
}