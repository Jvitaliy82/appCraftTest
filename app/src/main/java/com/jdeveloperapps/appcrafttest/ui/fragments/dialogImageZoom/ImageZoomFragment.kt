package com.jdeveloperapps.appcrafttest.ui.fragments.dialogImageZoom

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import coil.load
import com.jdeveloperapps.appcrafttest.R

class ImageZoomFragment : DialogFragment() {

    private val args: ImageZoomFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(requireContext()).inflate(
            R.layout.image_zool_layout,
            activity?.findViewById<RelativeLayout>(R.id.layout_dialog_container)
        )
        view.findViewById<com.jsibbold.zoomage.ZoomageView>(R.id.zoom_image)
            .load(args.albumDetail.url)
        return AlertDialog.Builder(requireContext())
            .setView(view)
            .create()
    }
}