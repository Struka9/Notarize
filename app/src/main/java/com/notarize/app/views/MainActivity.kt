package com.notarize.app.views

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.notarize.app.R
import com.notarize.app.ext.hide
import com.notarize.app.ext.show
import com.notarize.app.services.TallyLockEventService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navController = findNavController(R.id.nav_host)
        val appBarConfiguration = AppBarConfiguration(navController.graph, drawer)
        toolbar.setupWithNavController(navController, drawer)

        navigation_view.setupWithNavController(navController)

        // Launch the observer service
        startService(Intent(this, TallyLockEventService::class.java))

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.photoPreview ->
                    toolbar.hide()
                else -> toolbar.show()

            }
        }
    }
}