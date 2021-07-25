package com.example.smartattendancesystem.ui.main.map

import android.annotation.SuppressLint
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
import androidx.compose.ui.graphics.Color
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
import com.google.android.gms.maps.model.*
import timber.log.Timber


@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel(),
    facialRecognitionScreen : (String) -> Unit
){

    val mapView = rememberMapViewWithLifecycle()
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

    if (isTracking == true && viewModel.distance.value > 0F && viewModel.distance.value < 50F){
        facialRecognitionScreen(viewModel.userId)
    }


    MapScreen(
        mapView,
        viewModel.locationState.value,
        Lectlocation,
        myLocation
    )


}



@Composable
internal fun MapScreen(
    mapView: MapView,
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


@SuppressLint("MissingPermission")
private fun mapViewSetUp(
    map: GoogleMap,
    locationModel: LocationModel,
    myLocation: LocationModel,
){
    map.clear()
    map.uiSettings.isZoomControlsEnabled = true
    val user =  LatLng(myLocation.latitude, myLocation.longitude)
    val classRoom = LatLng(locationModel.latitude, locationModel.longitude)
    map.isMyLocationEnabled = true
    //map.moveCamera(CameraUpdateFactory.newLatLngZoom(user, 15f))
    map.animateCamera(
        CameraUpdateFactory.newLatLngZoom(
            user,
            20f
        )
    )
    map.addCircle(
        CircleOptions()
            .center(classRoom)
            .radius(100.0)
            .strokeColor(android.graphics.Color.GREEN)
            .fillColor(android.graphics.Color.GRAY)
            .strokeWidth(4F)
    )
    val markerOptionsDestination = MarkerOptions()
        .title("Class Room")
        .position(classRoom)
    map.addMarker(markerOptionsDestination)

}