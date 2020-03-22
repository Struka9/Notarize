package com.notarize.app.views.main_activity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.notarize.app.R
import com.notarize.app.ext.asCurrency
import com.notarize.app.ext.hide
import com.notarize.app.ext.show
import com.notarize.app.services.TallyLockEventService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val mainActivityViewModel: MainActivityViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navController = findNavController(R.id.nav_host)
        val appBarConfiguration = AppBarConfiguration(navController.graph, drawer)
        toolbar.setupWithNavController(navController, drawer)

        navigation_view.setupWithNavController(navController)

        val header = navigation_view.getHeaderView(0)
        val walletMoney = header.findViewById<TextView>(R.id.tv_account_money)
        val walletAddress = header.findViewById<TextView>(R.id.tv_account_address)

        mainActivityViewModel.walletAddress.observe(this, Observer {
            walletAddress.text = it
        })

        mainActivityViewModel.walletMoney.observe(this, Observer {
            walletMoney.text = it.asCurrency(getString(R.string.symbol_eth))
        })

        // Launch the observer service
        startService(
            Intent(
                this,
                TallyLockEventService::class.java
            )
        )

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.photoPreview ->
                    toolbar.hide()
                else -> toolbar.show()

            }
        }
    }
}