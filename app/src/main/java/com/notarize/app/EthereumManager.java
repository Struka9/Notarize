package com.notarize.app;

import android.util.Log;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

import java.util.concurrent.ExecutionException;

public class EthereumManager {


    public Web3j connectToNetwork(String network) {
        Web3j web3 = Web3j.build(new HttpService(network));

        try {
            Log.d("EthereumManager", "Trying to connect to " + network);
            Web3ClientVersion clientVersion = web3.web3ClientVersion().sendAsync().get();
            if (!clientVersion.hasError()) {
                Log.d("EthereumManager", "Connected");
            } else {
                Log.d("EthereumManager", "Error Connecting to " + network);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.d("EthereumManager", "Exception Connecting to " + network);
        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.d("EthereumManager", "Exception Connecting to " + network);
        }

        return web3;
    }
}
