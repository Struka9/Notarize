package com.notarize.app


import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Type
import org.web3j.abi.datatypes.Utf8String
import org.web3j.crypto.Credentials
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.tx.gas.DefaultGasProvider
import java.lang.Exception
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        uploadButton.setOnClickListener{
            Log.d("TEST", "FIRST LOG MESSAGE")


            // Create a request body with file and image media type
            /*val fileReqBody = RequestBody.create(MediaType.parse("text/plain"), "This is the file content")
            val fileName = "file3.txt"

            val ipfsManager = IpfsManager(
                getString(R.string.pinata_header_1),
                getString(R.string.pinata_header_2))

            ipfsManager.uploadFile(fileName, fileReqBody)*/


            var ethereumManager = EthereumManager()
            var web3 = ethereumManager.connectToNetwork(getString(R.string.testnet_infura_endpoint))


            val password = getString(R.string.temp_password)
            var notaryCredentials : Credentials? = ethereumManager.loadCredentials(this, getString(R.string.k_WalletFileName), password)
            var adversaryCredentials : Credentials? = ethereumManager.loadCredentials(this, getString(R.string.k_UnauthorizedWalletFileName), password)

            //var smartContract = TallyLock.load(getString(R.string.tallyLockSmartContractAddress), web3, notaryCredentials, DefaultGasProvider.GAS_PRICE, DefaultGasProvider.GAS_LIMIT)
            var smartContract = TallyLock.load(getString(R.string.tallyLockSmartContractAddress), web3, adversaryCredentials, DefaultGasProvider.GAS_PRICE, DefaultGasProvider.GAS_LIMIT)

            var receipt = smartContract.signDocument("QmXavJhZxRfo62i4p8KbrC1ZckP6Uyhy2Z6bQ7QDKBWqE8",
                "QmXavJhZxRfo62i4p8KbrC1ZckP6Uyhy2Z6bQ7QDKBWqE8").sendAsync().whenCompleteAsync {
                t: TransactionReceipt?, u: Throwable? ->
                if (t != null ) {
                    Log.d("TEXT", "Transaction receipt: " + t.transactionHash)
                } else {

                    Log.d("TEXT", "Something happened: " + u?.message)
                }
            }



            /*
            //Send Funds
            if (notaryCredentials != null && adversaryCredentials != null) {
                try {
                    var receipt = Transfer.sendFunds(
                        web3,
                        adversaryCredentials,
                        getString(R.string.temp_destination_address),
                        BigDecimal(10000000),
                        Convert.Unit.GWEI).sendAsync().get()

                    Log.d("TEXT", "Transaction receipt: " + receipt.transactionHash)
                } catch (e : Exception) {
                    e.printStackTrace()
                }


            } else {
                Log.d("TEST", "Credentials are not valid :S")
            }*/


        }
    }
}
