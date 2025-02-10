package com.ftorres.notes.presentation.screens.map

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.mapbox.maps.Style
import com.mapbox.maps.MapView
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.ftorres.notes.presentation.viewmodel.MapViewModel
import com.google.android.gms.maps.model.LatLng
import com.mapbox.maps.plugin.gestures.addOnMapClickListener


@Composable
fun MapScreen(viewModel: MapViewModel, navController: NavController) {
    val mapView = rememberMapView()

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { mapView }
    ) { view ->
        view.getMapboxMap().apply {
            loadStyleUri(Style.MAPBOX_STREETS)
            addOnMapClickListener { point ->
                viewModel.setSelectedLocation(point)
                navController.popBackStack()
                true
            }
        }
    }
}

@Composable
fun rememberMapView(): MapView {
    val context = LocalContext.current
    return remember {
        MapView(context).apply {
            getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS)
        }
    }
}
