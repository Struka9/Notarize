package com.notarize.app.views.main_activity

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.notarize.app.R
import com.notarize.app.Status
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
        val receiveButton = header.findViewById<Button>(R.id.bt_receive)
        receiveButton.setOnClickListener {
            mainActivityViewModel.onShowReceiveDialogClick(this@MainActivity)
        }

        val sendButton = header.findViewById<Button>(R.id.bt_send)
        sendButton.setOnClickListener {
            drawer.closeDrawer(Gravity.LEFT)
            navController.navigate(R.id.send)
        }

        mainActivityViewModel.receiveEthDialog.observe(this, Observer { dialog ->
            if (dialog != null) {
                drawer.closeDrawer(Gravity.LEFT)
                dialog.show()
            }
        })

        mainActivityViewModel.walletAddress.observe(this, Observer {
            walletAddress.text = it
        })

        drawer.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {
                // Do nothing
            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                // Do nothing
            }

            override fun onDrawerClosed(drawerView: View) {
                // Do nothing
            }

            override fun onDrawerOpened(drawerView: View) {
                mainActivityViewModel.walletMoney().observe(this@MainActivity, Observer {
                    when (it.status) {
                        Status.SUCCESS -> {
                            walletMoney.text = it.data?.asCurrency(getString(R.string.symbol_eth))
                        }
                        else -> {
                            walletMoney.text = "---"
                        }
                    }
                })
            }
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