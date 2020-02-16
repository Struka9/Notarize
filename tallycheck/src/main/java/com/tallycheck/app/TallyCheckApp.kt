package com.tallycheck.app

import android.app.Application
import android.content.res.Configuration
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security

class TallyCheckApp : Application() {
    // Called when the application is starting, before any other application objects have been created.
// Overriding this method is totally optional!
    override fun onCreate() {
        super.onCreate()
        setupBouncyCastle()
    }

    // Called by the system when the device configuration changes while your component is running.
// Overriding this method is totally optional!
    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
    }

    // This is called when the overall system is running low on memory,
// and would like actively running processes to tighten their belts.
// Overriding this method is totally optional!
    override fun onLowMemory() {
        super.onLowMemory()
    }

    //WHY: https://github.com/web3j/web3j/issues/915
    private fun setupBouncyCastle() {
        val provider = Security.getProvider(
            BouncyCastleProvider.PROVIDER_NAME
        )
            ?: // Web3j will set up the provider lazily when it's first used.
            return
        if (provider.javaClass == BouncyCastleProvider::class.java) { // BC with same package name, shouldn't happen in real life.
            return
        }
        // Android registers its own BC provider. As it might be outdated and might not include
        // all needed ciphers, we substitute it with a known BC bundled in the app.
        // Android's BC has its package rewritten to "com.android.org.bouncycastle" and because
        // of that it's possible to have another BC implementation loaded in VM.
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME)
        Security.insertProviderAt(BouncyCastleProvider(), 1)
    }
}