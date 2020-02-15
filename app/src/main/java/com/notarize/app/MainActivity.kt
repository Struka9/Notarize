package com.notarize.app


import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import org.web3j.crypto.Credentials
import org.web3j.crypto.WalletUtils
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.tx.Transfer
import org.web3j.utils.Convert
import java.io.File
import java.lang.Exception
import java.math.BigDecimal


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        uploadButton.setOnClickListener{
            Log.d("TEST", "FIRST LOG MESSAGE")

            /*
            // Create a request body with file and image media type
            val fileReqBody = RequestBody.create(MediaType.parse("text/plain"), "This is the file content")
            val fileName = "file3.txt"

            val ipfsManager = IpfsManager(
                getString(R.string.pinata_header_1),
                getString(R.string.pinata_header_2))

            ipfsManager.uploadFile(fileName, fileReqBody)
             */

            var ethereumManager = EthereumManager()
            var web3 = ethereumManager.connectToNetwork(getString(R.string.testnet_infura_endpoint))

            /*var sharedPreferences = getSharedPreferences(getString(R.string.k_WalletSharedPreferences), Context.MODE_PRIVATE);
            var walletFileName = sharedPreferences.getString(getString(R.string.k_WalletFileName), "")

            var password = getString(R.string.temp_password)

            var walletPath = getFilesDir().getAbsolutePath()
            var walletDir = File(walletPath)

            var credentials : Credentials? = null

            var validCredentials = false

            if (walletFileName == null || walletFileName.equals("")) {
                //There is no wallet, create the wallet
                try {
                    var walletName = WalletUtils.generateNewWalletFile(password, walletDir)
                    Log.d("TEST", "Wallet name: " + walletName)

                    //Save the walletName
                    var editor = sharedPreferences.edit()
                    editor.putString(getString(R.string.k_WalletFileName), walletName)
                    editor.commit()

                    var walletFile = File(walletDir, walletName)
                    credentials = WalletUtils.loadCredentials(password, walletFile)
                    validCredentials = true
                    Log.d("TEST" , "Your wallet address is: " + credentials.address)
                } catch (e : Exception) {
                    e.printStackTrace()
                }
            } else {
                Log.d("TEST", "Loading Wallet name: " + walletFileName)
                var walletFile = File(walletDir, walletFileName)
                credentials = WalletUtils.loadCredentials(password, walletFile)
                validCredentials = true
                Log.d("TEST" , "Your wallet address is: " + credentials.address)
            }*/

            var credentials : Credentials? = ethereumManager.loadCredentials(this)
            if (credentials != null) {
                var receipt = Transfer.sendFunds(
                    web3,
                    credentials,
                    getString(R.string.temp_destination_address),
                    BigDecimal(10000000),
                    Convert.Unit.GWEI).sendAsync().get()

                Log.d("TEXT", "Transaction receipt: " + receipt.transactionHash)

            } else {
                Log.d("TEST", "Credentials are not valid :S")
            }


        }
    }
}
