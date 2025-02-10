package com.ftorres.notes.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.mapbox.geojson.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor() : ViewModel() {
    private val _selectedLocation = MutableStateFlow<Point?>(null)
    val selectedLocation: StateFlow<Point?> = _selectedLocation

    fun setSelectedLocation(point: Point) {
        _selectedLocation.value = point
    }
}