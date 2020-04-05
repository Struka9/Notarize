package com.tallycheck.app

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.google.android.material.snackbar.Snackbar
import com.notarize.app.TallyLock
import com.notarize.app.toByteArray
import com.tallycheck.app.ext.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.web3j.crypto.Credentials
import org.web3j.tx.gas.DefaultGasProvider

class MainActivity : AppCompatActivity() {

    var dialog: Dialog? = null

    lateinit var tallyLock: TallyLock

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            openPickImageDialog()
        }

        initWeb3()
    }

    private fun initWeb3() {
        val ethereumManager = EthereumManager()
        val web3 = ethereumManager
            .connectToNetwork(getString(R.string.testnet_infura_endpoint))
        val password = getString(R.string.temp_password)
        val notaryCredentials: Credentials? =
            ethereumManager
                .loadCredentials(this, getString(R.string.k_WalletFile), password)

        Log.d("MainActivity", "Address => ${notaryCredentials?.address}")

        tallyLock = TallyLock.load(
            getString(R.string.tallyLockSmartContractAddress),
            web3,
            notaryCredentials,
            DefaultGasProvider.GAS_PRICE,
            DefaultGasProvider.GAS_LIMIT
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            data?.data?.let {
                img_check.setImageURI(it)
            }

            dialog = createLoadingDialog()
            dialog?.show()
            checkSigning()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun checkSigning() {
        //If Uploaded correctly send to the SmartContract for logging
        val hash = img_check
            .drawable
            .toBitmap()
            .toByteArray()
            .toSha256()
            .toHexString()

        tallyLock.getSignatureBundleUrl(hash)
            .sendAsync()
            .whenCompleteAsync { ipfsHash: String?, throwable: Throwable? ->
                if (throwable != null) {
                    Snackbar.make(
                        img_check,
                        R.string.couldnt_verify_with_contract,
                        Snackbar.LENGTH_LONG
                    ).show()
                    dialog?.cancel()
                } else {
                    if (ipfsHash.isNullOrEmpty()) {
                        Snackbar.make(
                            img_check,
                            R.string.not_signed,
                            Snackbar.LENGTH_LONG
                        ).show()
                        dialog?.cancel()
                    } else {
                        Snackbar.make(
                            img_check,
                            R.string.doc_signed,
                            Snackbar.LENGTH_LONG
                        ).show()
                        dialog?.cancel()
                    }
                }
            }
    }
}
