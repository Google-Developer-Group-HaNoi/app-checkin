package com.lxn.gdghanoicheckin.features.scan

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.*
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import com.lxn.gdghanoicheckin.databinding.FragmentScanBinding
import com.lxn.gdghanoicheckin.features.confirm.ConfirmQrFragment.Companion.KEY
import com.lxn.gdghanoicheckin.utils.*
import kotlinx.coroutines.delay

class ScanFragment : Fragment() {
    private lateinit var codeScanner: CodeScanner
    private lateinit var fragmentScanBinding: FragmentScanBinding

    companion object {
        val PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        const val PERMISSION_REQUEST_CODE = 101
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentScanBinding = FragmentScanBinding.inflate(layoutInflater, container, false)
        return fragmentScanBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initScanner()
        callWithLifecycleStarted {
            delay(500)
            if (areAllPermissionsGranted()) {
                codeScanner.startPreview()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        codeScanner.releaseResources()
    }

    private fun initScanner() {
        codeScanner = CodeScanner(requireActivity(), fragmentScanBinding.scannerView).apply {
            camera = CodeScanner.CAMERA_BACK
            autoFocusMode = AutoFocusMode.CONTINUOUS
            formats = listOf(BarcodeFormat.QR_CODE)
            scanMode = ScanMode.SINGLE
            isAutoFocusEnabled = true
            isTouchFocusEnabled = false
            decodeCallback = DecodeCallback(::handleScannedBarcode)
            errorCallback = ErrorCallback(::showError)
        }
    }

    private fun handleScannedBarcode(result: Result) {
        val barcode = BarcodeParser.parseResult(result)
        navigateToConfirm(barcode)
    }

    private fun navigateToConfirm(barcode: String) {
        launchWithMain {
            delay(200)
            findNavController().navigate(
                com.lxn.gdghanoicheckin.R.id.action_scanFragment_to_confirmQrFragment,
                bundleOf(KEY to barcode)
            )
        }
    }


    private fun showError(error: Throwable?) {
        logError(error?.message.toString())
    }

    private fun areAllPermissionsGranted(): Boolean {
        return PermissionsHelper.areAllPermissionsGranted(requireContext(), PERMISSIONS)
    }

}