package com.example.afinal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.afinal.databinding.FragmentGoogleMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.awaitMapLoad

class GoogleMapsFragment : Fragment() {

    private lateinit var binding: FragmentGoogleMapsBinding
    private lateinit var gMap: GoogleMap
    val args: GoogleMapsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity() as MainActivity).supportActionBar?.title = "Show Location"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGoogleMapsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            gMap = mapFragment.awaitMap()
            gMap.awaitMapLoad()

            val location = LatLng(args.latitude.toDouble(), args.longitude.toDouble())
            gMap.addMarker(MarkerOptions().position(location).title("Desired Location"))
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10f))
        }
    }
}