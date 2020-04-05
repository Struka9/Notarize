package com.notarize.app

import android.app.Application
import com.notarize.app.di.appModule
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber
import java.security.Security

class TallyLockApplication : Application() {
    // Called when the application is starting, before any other application objects have been created.
// Overriding this method is totally optional!
    override fun onCreate() {
        super.onCreate()
        setupBouncyCastle()

        Timber.plant(Timber.DebugTree())
        startKoin {
            androidContext(this@TallyLockApplication)
            modules(listOf(appModule))
        }
    }

    //WHY: https://github.com/web3j/web3j/issues/915
    private fun setupBouncyCastle() {
        val provider =
            Security.getProvider(BouncyCastleProvider.PROVIDER_NAME)
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