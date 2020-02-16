package com.notarize.app


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.web3j.crypto.Credentials
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.tx.gas.DefaultGasProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var fileHash = intent.getStringExtra(EXTRA_FILE_HASH);

        uploadButton.setOnClickListener{
            Log.d("TEST", "FIRST LOG MESSAGE")


            var content = fileHash

            // Create a request body with file and image media type
            val fileReqBody = RequestBody.create(MediaType.parse("text/plain"), content)
            val fileName = "file3.txt"

            val ipfsManager = IpfsManager(
                getString(R.string.pinata_header_1),
                getString(R.string.pinata_header_2))

            var ethereumManager = EthereumManager()
            var web3 = ethereumManager.connectToNetwork(getString(R.string.testnet_infura_endpoint))
            val password = getString(R.string.temp_password)
            var notaryCredentials : Credentials? = ethereumManager.loadCredentials(this, getString(R.string.k_WalletFileName), password)

            //var hashedContent = Hash.hmacSha512(content.encodeToByteArray(), notaryCredentials.)
            //var adversaryCredentials : Credentials? = ethereumManager.loadCredentials(this, getString(R.string.k_UnauthorizedWalletFileName), password)

            /*
            if (notaryCredentials != null) {
                var signatureData = Sign.signMessage(content.toByteArray(Charsets.UTF_8), notaryCredentials?.ecKeyPair)
                Log.d("TEXT", "Signature: " + signatureData.toString())
            }*/

            //Upload file to IPFS
            ipfsManager.uploadFile(fileName, fileReqBody, object : Callback<IpfsObject> {
                override fun onFailure(call: Call<IpfsObject>, t: Throwable) {
                    Log.d("TEST", t.message)
                }

                override fun onResponse(call: Call<IpfsObject>, response: Response<IpfsObject>) {

                    if (response.code() == 200) {
                        Log.d("TEST", "Something worked")
                        val body = response.body()!!
                        Log.d("TEST", "IPFSHash: " + body.IpfsHash)


                        var smartContract: TallyLock = TallyLock.load(getString(R.string.tallyLockSmartContractAddress),
                            web3,
                            notaryCredentials,
                            DefaultGasProvider.GAS_PRICE,
                            DefaultGasProvider.GAS_LIMIT)
                        //var smartContract = TallyLock.load(getString(R.string.tallyLockSmartContractAddress), web3, notaryCredentials, DefaultGasProvider.GAS_PRICE, DefaultGasProvider.GAS_LIMIT)


                        //If Uploaded correctly send to the SmartContract for logging
                        var receipt = smartContract.signDocument(
                            fileHash,
                            body.IpfsHash).sendAsync().whenCompleteAsync {
                                t: TransactionReceipt?, u: Throwable? ->
                            if (t != null ) {
                                Log.d("TEXT", "Transaction receipt: " + t.transactionHash)
                            } else {

                                Log.d("TEXT", "Something happened: " + u?.message)
                            }
                        }

                    } else {
                        Log.d("TEST", "Something Kind Of worked")
                        Log.d("TEST", "CODE " + response.code())
                    }
                }

            })









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
