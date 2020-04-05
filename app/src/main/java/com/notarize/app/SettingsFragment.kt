package com.notarize.app

import android.content.ClipboardManager
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.notarize.app.ext.copyTextToClipboard
import com.notarize.app.ext.createLoadingDialog
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : PreferenceFragmentCompat() {
    private val clipboardManager: ClipboardManager by inject()
    private val settingsFragmentViewModel: SettingsFragmentViewModel by viewModel()
    private var dialog: AlertDialog? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        return when (preference?.key) {
            getString(R.string.key_import_wallet) -> {
                dialog?.cancel()
                dialog = AlertDialog.Builder(requireContext())
                    .setMessage(getString(R.string.message_restore_wallet_warning))
                    .setNegativeButton(R.string.cancel) { dialogInterface, which ->
                        dialogInterface.cancel()
                    }
                    .setPositiveButton(R.string.do_it) { _, _ ->
                        val view = LayoutInflater.from(requireContext())
                            .inflate(R.layout.dialog_restore_wallet, null, false)
                        val mnemonicEt = view.findViewById<EditText>(R.id.et_mnemonic)

                        dialog?.cancel()
                        dialog = AlertDialog
                            .Builder(requireContext())
                            .setView(view)
                            .setNegativeButton(R.string.cancel) { _, _ ->
                                dialog?.dismiss()
                            }
                            .setPositiveButton(R.string.ok) { _, _ ->
                                val mnemonic = mnemonicEt.text.toString().trim()
                                if (mnemonic.isNullOrBlank()) {
                                    Toast.makeText(
                                        requireContext(),
                                        getString(R.string.empty_mnemonic),
                                        Toast.LENGTH_LONG
                                    ).show()
                                    return@setPositiveButton
                                }

                                dialog?.cancel()
                                dialog = requireContext().createLoadingDialog()
                                settingsFragmentViewModel.importWallet(mnemonic)
                                    .observe(viewLifecycleOwner, Observer { resource ->
                                        when (resource.status) {
                                            Status.SUCCESS -> {
                                                dialog?.cancel()
                                                Toast.makeText(
                                                    requireContext(),
                                                    getString(R.string.wallet_restored),
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                            Status.LOADING -> {
                                                dialog?.show()
                                            }
                                            Status.ERROR -> {
                                                dialog?.cancel()
                                                Toast.makeText(
                                                    requireContext(),
                                                    getString(R.string.generic_something_wrong),
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        }
                                    })
                            }
                            .show()
                    }
                    .show()
                true
            }

            getString(R.string.key_export_wallet) -> {
                val pkLiveData = settingsFragmentViewModel.exportWallet()
                pkLiveData.observe(viewLifecycleOwner, Observer { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            val view = LayoutInflater.from(requireContext())
                                .inflate(R.layout.dialog_simple_text, null, false)
                            val pkTv = view.findViewById<TextView>(R.id.tv_message)
                            pkTv.text = resource.data

                            pkTv.setOnLongClickListener {
                                requireContext().copyTextToClipboard(
                                    resource.data,
                                    getString(R.string.account_mnemonic),
                                    getString(R.string.message_mnemonic_copied)
                                )
                                return@setOnLongClickListener true
                            }

                            dialog?.cancel()
                            dialog = AlertDialog.Builder(requireContext())
                                .setPositiveButton(R.string.ok) { dialog, which ->
                                    dialog.dismiss()
                                }
                                .setView(view)
                                .show()
                        }
                        Status.LOADING -> {
                            dialog?.cancel()
                            dialog = requireContext().createLoadingDialog()
                            dialog?.show()
                        }
                        Status.ERROR -> {
                        }
                    }
                })
                true
            }

            getString(R.string.key_generate_wallet) -> {
                dialog?.cancel()
                dialog = AlertDialog.Builder(requireContext())
                    .setMessage(getString(R.string.message_generate_wallet_warning))
                    .setNegativeButton(R.string.cancel) { dialogInterface, which ->
                        dialogInterface.cancel()
                    }
                    .setPositiveButton(R.string.do_it) { _, _ ->
                        dialog?.dismiss()
                        dialog = requireContext().createLoadingDialog()
                        settingsFragmentViewModel.generateNewWallet()
                            .observe(viewLifecycleOwner, Observer {
                                when (it.status) {
                                    Status.SUCCESS -> {
                                        dialog?.dismiss()
                                        dialog = null
                                    }

                                    Status.ERROR -> {

                                    }
                                }
                            })
                    }
                    .show()
                true
            }

            else -> super.onPreferenceTreeClick(preference)
        }
    }
}
