package com.notarize.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.tallycheck.tallycheckcontract.R;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.io.File;

public class EthereumManager {

    public Web3j connectToNetwork(String network) {
        return Web3j.build(new HttpService(network));
    }

    public Credentials loadCredentials(Context context, String sharedPreferencesKey, String password) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.k_WalletSharedPreferences), Context.MODE_PRIVATE);
        String walletFileName = sharedPreferences.getString(sharedPreferencesKey, "");

        String walletPath = context.getFilesDir().getAbsolutePath();
        File walletDir = new File(walletPath);

        Credentials credentials = null;


        if (walletFileName == null || walletFileName.equals("")) {
            //There is no wallet, create the wallet
            try {
                String walletName = WalletUtils.generateLightNewWalletFile(password, walletDir);
                Log.d("TEST", "Wallet name: " + walletName);

                //Save the walletName
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(sharedPreferencesKey, walletName);
                editor.commit();

                File walletFile = new File(walletDir, walletName);
                credentials = WalletUtils.loadCredentials(password, walletFile);
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
