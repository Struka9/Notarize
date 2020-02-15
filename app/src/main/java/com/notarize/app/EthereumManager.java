package com.notarize.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

import java.io.File;
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

    public Credentials loadCredentials(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.k_WalletSharedPreferences), Context.MODE_PRIVATE);
        String walletFileName = sharedPreferences.getString(context.getString(R.string.k_WalletFileName), "");

        String password = context.getString(R.string.temp_password);

        String walletPath = context.getFilesDir().getAbsolutePath();
        File walletDir = new File(walletPath);

        Credentials credentials = null;


        if (walletFileName == null || walletFileName.equals("")) {
            //There is no wallet, create the wallet
            try {
                String walletName = WalletUtils.generateNewWalletFile(password, walletDir);
                Log.d("TEST", "Wallet name: " + walletName);

                //Save the walletName
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(context.getString(R.string.k_WalletFileName), walletName);
                editor.commit();

                File walletFile = new File(walletDir, walletName);
                credentials = WalletUtils.loadCredentials(password, walletFile);
                Log.d("TEST" , "Your wallet address is: " + credentials.getAddress());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.d("TEST", "Loading Wallet name: " + walletFileName);
            File walletFile = new File(walletDir, walletFileName);
            try {
                credentials = WalletUtils.loadCredentials(password, walletFile);
                Log.d("TEST" , "Your wallet address is: " + credentials.getAddress());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return credentials;
    }
}
