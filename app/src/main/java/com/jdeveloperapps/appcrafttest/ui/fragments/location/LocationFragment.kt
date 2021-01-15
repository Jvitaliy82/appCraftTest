package com.jdeveloperapps.appcrafttest.ui.fragments.location

import android.Manifest
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.jdeveloperapps.appcrafttest.R
import com.jdeveloperapps.appcrafttest.databinding.FragmentLocationBinding
import com.jdeveloperapps.appcrafttest.services.TrackingService
import com.jdeveloperapps.appcrafttest.util.Constants.ACTION_START_OR_RESUME_SERVICE
import com.jdeveloperapps.appcrafttest.util.Constants.ACTION_STOP_SERVICE
import com.jdeveloperapps.appcrafttest.util.Constants.REQUEST_CODE_LOCATION_PERMISSIONS
import com.jdeveloperapps.appcrafttest.util.TrackingUtility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_location.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class LocationFragment : Fragment(R.layout.fragment_location), EasyPermissions.PermissionCallbacks {

    private var isTracking = false

    private var _binding: FragmentLocationBinding? = null
    val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLocationBinding.bind(view)

        binding.apply {
            button_start_stop.setOnClickListener {
                requestPermissions()
                toggleStartStop()
            }
        }

        observeToTrackingService()
    }

    private fun observeToTrackingService() {
        TrackingService.isTracking.observe(viewLifecycleOwner) {
            updateTracking(it)
        }
        TrackingService.userLocation.observe(viewLifecycleOwner) {
            binding.textLocation.text = String.format("lat:%s long:%s", it.latitude, it.longitude)
        }
    }

    private fun toggleStartStop() {
        if (isTracking) {
            sendCommandToService(ACTION_STOP_SERVICE)
        } else {
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
        }
    }

    private fun updateTracking(isTracking: Boolean) {
        this.isTracking = isTracking
        if (!isTracking) {
            binding.buttonStartStop.text = "Start"
        } else {
            binding.buttonStartStop.text = "Stop"
        }
    }

    private fun sendCommandToService(action: String) =
        Intent(requireContext(), TrackingService::class.java).also {
            it.action = action
            requireContext().startService(it)
        }

    private fun requestPermissions() {
        if (TrackingUtility.hasLocationPermission(requireContext())) {
            return
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                "you need to accept location permissions to use this app.",
                REQUEST_CODE_LOCATION_PERMISSIONS,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "you need to accept location permissions to use this app.",
                REQUEST_CODE_LOCATION_PERMISSIONS,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }


}