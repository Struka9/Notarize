package com.notarize.app


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.jsonwebtoken.Jwts
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.web3j.crypto.Credentials
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.tx.gas.DefaultGasProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var fileHash = intent.getStringExtra(EXTRA_FILE_HASH);

        uploadButton.setOnClickListener{
            val ipfsManager = IpfsManager(
                getString(R.string.pinata_header_1),
                getString(R.string.pinata_header_2))

            val ethereumManager = EthereumManager()
            val web3 = ethereumManager.connectToNetwork(getString(R.string.testnet_infura_endpoint))
            val password = getString(R.string.temp_password)
            val notaryCredentials : Credentials? = ethereumManager.loadCredentials(this, getString(R.string.k_WalletFileName), password)

            var jwtToken = Jwts.builder()
                .setIssuer(notaryCredentials?.address)
                .setIssuedAt(Date())
                .claim("fileHash", fileHash).compact()


            var content = jwtToken

            // Create a request body with file and image media type
            val fileReqBody = RequestBody.create(MediaType.parse("application/jwt"), content)
            val fileName = "filehash.jwt"


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


                        val smartContract: TallyLock = TallyLock.load(getString(R.string.tallyLockSmartContractAddress),
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

        }
    }
}
