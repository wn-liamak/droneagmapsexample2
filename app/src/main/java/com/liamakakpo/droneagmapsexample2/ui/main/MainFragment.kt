package com.liamakakpo.droneagmapsexample2.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions
import com.liamakakpo.droneagmapsexample2.R
import com.liamakakpo.droneagmapsexample2.databinding.FragmentMainBinding
import com.liamakakpo.droneagmapsexample2.markers.MarkerFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

@AndroidEntryPoint
class MainFragment : Fragment(), OnMapReadyCallback {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    private lateinit var binding: FragmentMainBinding
    private var mapFragment: SupportMapFragment? = null
    private var map: GoogleMap? = null

    private val centerMarker by lazy {
        BitmapDescriptorFactory.fromBitmap(MarkerFactory.getMarker(requireContext())!!)
    }

    private val areaNumberFormat: NumberFormat by lazy {
        NumberFormat.getNumberInstance(Locale.getDefault()).apply {
            minimumFractionDigits = 0
            maximumFractionDigits = 2
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)
        mapFragment = childFragmentManager
            .findFragmentById(R.id.map_fragment) as SupportMapFragment
        binding.buttonClear.setOnClickListener {
            viewModel.clearPolygon()
        }
        mapFragment?.getMapAsync(this)
        return binding.root
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        map.uiSettings.apply {
            isMapToolbarEnabled = false
            isIndoorLevelPickerEnabled = false
        }
        map.mapType = GoogleMap.MAP_TYPE_HYBRID
        map.setOnMapClickListener {
            viewModel.addCoordinate(it)
        }
        lifecycleScope.launch {
            viewModel.viewState.collectLatest { uiModel ->
                if (uiModel.cameraUpdate != null) {
                    map.setOnCameraIdleListener(null) // clear listener to prevent infinite loop
                    map.moveCamera(uiModel.cameraUpdate)
                }
                map.setOnCameraIdleListener {
                    viewModel.onMapMoved(map.cameraPosition.target, map.cameraPosition.zoom)
                }
                if (uiModel.coordinates.isEmpty()) { // empty state
                    binding.statusLabel.setText(R.string.polygon_add_points)
                    binding.buttonClear.visibility = View.GONE
                } else if (uiModel.coordinates.size == 1) { // one-point state
                    binding.statusLabel.setText(R.string.polygon_add_one_more_point)
                    binding.buttonClear.visibility = View.VISIBLE
                } else { // valid polygon state
                    binding.statusLabel.text = getString(
                        R.string.polygon_status_format,
                        uiModel.coordinates.size,
                        areaNumberFormat.format(uiModel.areaKilometers)
                    )
                    binding.buttonClear.visibility = View.VISIBLE
                }
                plotMapItems(uiModel)
            }
        }
        viewModel.initialise()
    }

    private fun plotMapItems(uiModel: MapUiModel) {
        map?.clear()
        if (uiModel.coordinates.size > 1) { // draw polygon
            val polygonOptions = newPolygonOptions()
            polygonOptions.addAll(uiModel.coordinates)
            map?.addPolygon(polygonOptions)
        }
        uiModel.getPolygonCenter()?.let { target -> // draw marker at center of polygon
            map?.addMarker(
                MarkerOptions()
                    .position(target)
                    .icon(centerMarker)
                    .rotation(0f)
                    .anchor(0.5f, 1f)
            )
        }
    }

    private fun newPolygonOptions() = PolygonOptions()
        .strokeColor(ContextCompat.getColor(requireContext(), R.color.polygon_stroke))
        .fillColor(ContextCompat.getColor(requireContext(), R.color.polygon_fill))
}