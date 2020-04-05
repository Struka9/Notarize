package com.notarize.app.views.main_activity

import android.app.Dialog
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.notarize.app.R
import com.notarize.app.Resource
import com.notarize.app.Status
import com.notarize.app.di.repos.ICredentialsRepo
import com.notarize.app.ext.createDialog
import com.notarize.app.ext.toQrBitmap
import kotlinx.coroutines.Job
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.utils.Convert
import timber.log.Timber
import java.math.BigDecimal

class MainActivityViewModel(
    private val credentialsRepo: ICredentialsRepo,
    private val web3: Web3j
) :
    ViewModel() {

    private val job = Job()

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    private val _receiveEthDialog = MutableLiveData<Dialog>()
    val receiveEthDialog = _receiveEthDialog

    fun onShowReceiveDialogClick(activity: AppCompatActivity) {
        val address = walletAddress.value ?: return

        if (address.isBlank()) return

        val dialogView = View.inflate(activity, R.layout.receive_eth, null)
        val dialog = activity.createDialog(dialogView)
        val addressTxt = dialogView.findViewById<TextView>(R.id.tv_account_address)
        val qrImg = dialogView.findViewById<ImageView>(R.id.img_qr_code)

        addressTxt?.text = address
        val bitmap = "ethereum:${address}".toQrBitmap()
        qrImg?.setImageBitmap(bitmap)

        dialog.setOnCancelListener {
            _receiveEthDialog.postValue(null)
        }

        _receiveEthDialog.value = dialog
    }

    fun walletMoney(): LiveData<Resource<BigDecimal>> =
        Transformations.switchMap(credentialsRepo.credentials) { credentials ->
            return@switchMap liveData<Resource<BigDecimal>>() {
                try {
                    emit(Resource(Status.LOADING))
                    val response =
                        web3.ethGetBalance(credentials.address, DefaultBlockParameterName.LATEST)
                            .send()

                    if (response.error == null) {
                        val wei = response.balance
                        emit(
                            Resource(
                                Status.SUCCESS, data = Convert.fromWei(
                                wei.toString(),
                                Convert.Unit.ETHER
                                )
                            )
                        )
                    }
                } catch (e: Throwable) {
                    Timber.e(e)
                    emit(Resource(Status.ERROR))
                }
            }
        }

    val walletAddress: LiveData<String> =
        Transformations.map(credentialsRepo.credentials) {
            return@map it.address
        }
}