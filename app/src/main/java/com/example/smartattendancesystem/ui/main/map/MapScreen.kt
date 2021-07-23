package com.example.smartattendancesystem.ui.main.map

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartattendancesystem.model.LocationModel
import com.example.smartattendancesystem.services.TrackingService
import com.example.smartattendancesystem.ui.main.attendance.sendServiceCommand
import com.example.smartattendancesystem.util.Constants
import com.example.smartattendancesystem.util.rememberFlowWithLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import timber.log.Timber


@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel()
){

    val mapView = rememberMapViewWithLifecycle()
    val snackbarHostState = remember{ SnackbarHostState() }
    val context = LocalContext.current
    val Lectlocation by rememberFlowWithLifecycle(flow = viewModel.location)
        .collectAsState(initial = LocationModel())
    val myLocation by rememberFlowWithLifecycle(flow = viewModel.myLocation)
        .collectAsState(initial = LocationModel())
    val isTracking by TrackingService.tracking.observeAsState()

    if(!viewModel.locationState.value){
        Toast.makeText(context, "Location is Required", Toast.LENGTH_LONG).show()
    }else{
        sendServiceCommand(Constants.ACTION_START_OR_RESUME_SERVICE, context)
    }


    if (isTracking == true){
        viewModel.retrieveLocation()
    }

    MapScreen(
        mapView,
        snackbarHostState,
        viewModel.locationState.value,
        Lectlocation,
        myLocation
    )


}



@Composable
internal fun MapScreen(
    mapView: MapView,
    snackbarHostState: SnackbarHostState,
    locationState : Boolean,
    Lectlocation: LocationModel,
    myLocation : LocationModel
    ){

    Scaffold(
        content = {
            if(locationState){
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp)
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    AndroidView({mapView}) {mapview ->
                        mapview.getMapAsync {map->
                            mapViewSetUp(map, Lectlocation, myLocation)
                        }
                    }
                }
            }
        }
    )


}


private fun mapViewSetUp(
    map: GoogleMap,
    locationModel: LocationModel,
    myLocation: LocationModel,
){
    map.uiSettings.isZoomControlsEnabled = true
    val pickUp =  LatLng(-35.016, 143.321)
    val destination = LatLng(locationModel.latitude, locationModel.longitude)
    map.moveCamera(CameraUpdateFactory.newLatLngZoom(destination, 6f))
    val markerOptions = MarkerOptions()
        .title("class room")
        .position(pickUp)
    map.addMarker(markerOptions)
    val markerOptionsDestination = MarkerOptions()
        .title("Restaurant Hubert")
        .position(destination)
    map.addMarker(markerOptionsDestination)
    map.addPolyline(
        PolylineOptions().add( pickUp,
            LatLng(-34.747, 145.592),
            LatLng(-34.364, 147.891),
            LatLng(-33.501, 150.217),
            LatLng(-32.306, 149.248),
            destination))
}