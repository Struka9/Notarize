package com.notarize.app

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.BarcodeFormat
import com.google.zxing.integration.android.IntentIntegrator
import com.notarize.app.ext.*
import com.notarize.app.views.send_fragment.SendFragmentViewModel
import kotlinx.android.synthetic.main.fragment_send.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.web3j.utils.Convert
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber
import java.math.BigDecimal
import java.math.RoundingMode

class SendFragment : DialogFragment() {
    private var dialog: AlertDialog? = null
    private val sendViewModel: SendFragmentViewModel by sharedViewModel()

    private val qrScan: IntentIntegrator by lazy {
        IntentIntegrator.forSupportFragment(this@SendFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_send, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        et_send_amount.addTextChangedListener(afterTextChanged = { text: Editable? ->
            sendViewModel.amount.value = try {
                BigDecimal.valueOf(text?.toString()?.toDouble() ?: 0.0)
            } catch (e: Exception) {
                Timber.e(e)
                BigDecimal.ZERO
            }
        })

        et_sendto_address.addTextChangedListener(afterTextChanged = { text ->
            sendViewModel.destinationAddress.value = text?.toString()
        })

        bt_read_qr.setOnClickListener {
            launchQrReader()
        }

        bt_cancel.setOnClickListener {
            findNavController().popBackStack()
        }

        bt_send.setOnClickListener {
            sendViewModel.sendToAddress()
        }

        sendViewModel.transactionStatus.observe(viewLifecycleOwner, Observer { resource ->
            if (resource != null) {
                when (resource.status) {
                    Status.SUCCESS -> {
                        findNavController().popBackStack()
                        dialog?.cancel()
                        dialog = null
                        val txHash = resource.data
                        Snackbar.make(
                            view.parent as View,
                            getString(R.string.tx_submitted),
                            Snackbar.LENGTH_LONG
                        )
                            .setAction(R.string.view) {
                                this@SendFragment.viewWebpage(
                                    getString(
                                        R.string.format_address_ether_scan,
                                        txHash
                                    )
                                )
                            }.show()
                    }
                    Status.LOADING -> {
                        dialog?.cancel()

                        dialog = view.context.createLoadingDialog()
                        dialog?.show()
                    }
                    Status.IDLE -> {
                        dialog?.cancel()
                    }
                    Status.ERROR -> {
                        dialog?.cancel()
                        dialog = null

                        Snackbar.make(
                            view,
                            getString(R.string.error_submitting_tx),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {

            } else {
                val text = result.contents
                if (!text.isValidQrContent()) {
                    view?.let {
                        Snackbar.make(
                            it,
                            getString(R.string.message_not_valid_qr_code),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                } else {
                    val address = text.getAddress()
                    val weiAmount = text.getWeiAmount()

                    et_sendto_address.setText(address)
                    weiAmount?.let {
                        et_send_amount.setText(
                            Convert.fromWei(it, Convert.Unit.ETHER).setScale(
                                4,
                                RoundingMode.HALF_UP
                            ).toString()
                        )
                    }
                }
            }
        }
    }

    @AfterPermissionGranted(RC_CAMERA)
    private fun launchQrReader() {
        val perms = arrayOf(Manifest.permission.CAMERA)
        if (EasyPermissions.hasPermissions(requireContext(), *perms)) {
            qrScan.initiateScan(mutableListOf(BarcodeFormat.QR_CODE.name))
        } else {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.rationale_camera),
                RC_CAMERA,
                *perms
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        private const val RC_CAMERA = 1
    }
}