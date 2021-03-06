package com.example.smartattendancesystem.ui.main.attendance

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.asFlow
import com.example.smartattendancesystem.data.repositories.LocationState
import com.example.smartattendancesystem.model.AttendanceModel
import com.example.smartattendancesystem.model.User
import com.example.smartattendancesystem.model.local.ClassModel
import com.example.smartattendancesystem.services.TrackingService
import com.example.smartattendancesystem.ui.theme.gradientGreen
import com.example.smartattendancesystem.ui.theme.typography
import com.example.smartattendancesystem.util.Constants
import com.example.smartattendancesystem.util.horizontalGradientBackground
import com.example.smartattendancesystem.util.rememberFlowWithLifecycle
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import kotlinx.coroutines.flow.collect
import timber.log.Timber


@Composable
fun Attendance(
    verify : () -> Unit,
    map : (String) -> Unit
){
    val context = LocalContext.current
    val viewModel : AttendanceViewModel = hiltViewModel()
    val viewState by rememberFlowWithLifecycle(flow = viewModel.userData)
        .collectAsState(initial = User())
    val classes by rememberFlowWithLifecycle(flow = viewModel.classes)
        .collectAsState(initial = emptyList())
    val locations = TrackingService.pathPoints.asFlow()
    val isTracking by TrackingService.tracking.observeAsState()
    val locationState by rememberFlowWithLifecycle(flow = viewModel.requestLocation)
        .collectAsState(initial = LocationState(state = false))
    val courseTitle = remember{ mutableStateOf("")}
    val snackbarHostState = remember{ SnackbarHostState() }
    val classOngoingMessage = remember{ mutableStateOf(false)}

    if(viewModel.startService.value){
        sendServiceCommand(Constants.ACTION_START_OR_RESUME_SERVICE, context)
    }else{
        sendServiceCommand(Constants.ACTION_STOP_SERVICE, context)
    }

    if(isTracking == true){
       LaunchedEffect(locations){
           locations.collect {
               if(it.isNotEmpty() && it.last().isNotEmpty()){
                   viewModel.setAttendance(it.last().last())
               }
           }
       }
    }

    if(classOngoingMessage.value){
        LaunchedEffect(snackbarHostState){
            snackbarHostState.showSnackbar(
                message = "class currently ongoing",
            )
            classOngoingMessage.value = !classOngoingMessage.value
        }
    }

      val laucher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()) {
          if(it.resultCode < 0){
              viewModel.resetLocationState(state = true)
          }

    }


    
    if(locationState.request) {
        LaunchedEffect(snackbarHostState) {
            val result = snackbarHostState.showSnackbar(
                message = "Turn on your location",
                actionLabel = "Turn on",
                duration = SnackbarDuration.Indefinite
            )
            when(result){
                SnackbarResult.ActionPerformed ->{
                    viewModel.resetLocationState()
                    checkLocationSetting(context = context, laucher)
                }
                SnackbarResult.Dismissed ->{}
            }
        }
    }

    Attendance(
        viewState,
        classes,
        viewModel,
        snackbarHostState,
        courseTitle,
        verify,
        classOngoingMessage,
        map
    )


}


@Composable
internal fun Attendance(
    viewState: User,
    classes: List<AttendanceModel>,
    viewModel: AttendanceViewModel,
    snackbarHostState: SnackbarHostState,
    courseTitle: MutableState<String>,
    verify: () -> Unit,
    classOngoingMsg: MutableState<Boolean>,
    map: (String) -> Unit
){
    val courses = viewModel.courses.collectAsState(initial = emptyList())


    Scaffold(
        scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState),
        topBar = {
            if(viewState.imageUri != ""){
                AttendanceTopBar(modifier = Modifier.fillMaxWidth())
            }
        },
        floatingActionButton = {
            if(viewState.imageUri != "" && viewState.userType == "Lecturer"){
                val rippleExplode = remember{ mutableStateOf(false)}
                FloatingActionButton(rippleExplode = rippleExplode)
                if(rippleExplode.value){
                    AttendanceDialog(
                        onConfirm = {
                            if(courseTitle.value != ""){
                                viewModel.insertClass(courseTitle.value)
                            }
                            rippleExplode.value = false
                        },
                        onDismiss = {
                            rippleExplode.value = false
                        },
                        value = courseTitle
                    )
                }
            }
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            if(viewState.imageUri == ""){
                VerifyAccount(verify, viewState.name)
            }
            if(viewState.imageUri != "" && viewState.userType == "Lecturer"){
                LecturerContent(classModels = courses.value, viewModel = viewModel, classOngoingMsg)
            }
            if(viewState.imageUri != "" && viewState.userType == "Student"){
                StudentContent(classes, map)
            }
            Spacer(modifier = Modifier.height(50.dp))
            NoOngoingClassMessage(classModels = courses.value, classes)
        }
    }
}



@Composable
fun LecturerContent(
    classModels: List<ClassModel>,
    viewModel: AttendanceViewModel,
    classOngoingMsg: MutableState<Boolean>
){
    LazyColumn {
      items(items = classModels){classModel ->
          ClassListRow(
              classModel = classModel,
              onClassChange = {state ->
                  if (viewModel.classState.value > 0 && state){
                      classOngoingMsg.value = true
                  }else{
                      viewModel.classState.value = 0
                      viewModel.updateClassState(state = state, classModel = classModel)
                  }
              }
          )
      }
    }
}



@Composable
fun StudentContent(classes: List<AttendanceModel>, map: (String) -> Unit) {

    LazyColumn {
        items(items = classes) { classData ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp).clickable { map(classData.userId) },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = classData.courseTitle,
                    style = typography.h6.copy(fontSize = 18.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
                Text(
                    text = "2, Aug 2021",
                    style = typography.h6.copy(fontSize = 14.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }
    }
}








@Composable
fun ClassListRow(classModel : ClassModel, onClassChange : (Boolean) -> Unit){

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = classModel.courseTitle,
            style = typography.h6.copy(fontSize = 14.sp),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
        Switch(
            checked = classModel.classState,
            colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colors.primary),
            onCheckedChange = onClassChange
        )
    }
}





@Composable
fun FloatingActionButton(rippleExplode : MutableState<Boolean>){
    ExtendedFloatingActionButton(
        text = { Text(text = "Attendance")},
        icon ={
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null
            )
        },
        onClick ={rippleExplode.value = !rippleExplode.value},
        modifier = Modifier.padding(bottom = 50.dp),
        backgroundColor = MaterialTheme.colors.primary
    )
}




@Composable
private fun VerifyAccount(verify: () -> Unit, name: String){
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            .horizontalGradientBackground(gradientGreen)
            .padding(top = 30.dp, bottom = 8.dp, start = 8.dp, end = 8.dp)
    ) {
        val (title, btn) = createRefs()

        Text(
            text = name,
            style = typography.body1,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(4.dp)
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Button(
            onClick = verify,
            shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.elevation(),
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .constrainAs(btn) {
                    top.linkTo(title.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
          Text(text = "Verify Account")
        }

    }
}



@Composable
fun NoOngoingClassMessage(classModels: List<ClassModel>, classes: List<AttendanceModel>){
    if(classModels.isEmpty() && classes.isEmpty()){
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "No Ongoing class at the moment",
                style = MaterialTheme.typography.h6,
                color = Color.LightGray
            )
        }
    }
    
}

@Composable
private fun AttendanceTopBar(modifier: Modifier){

    TopAppBar(
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onSurface,
        title = {
            Text(
                text = "Attendance",
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        },
        elevation = 0.dp
    )
}




@Composable
fun AttendanceDialog(
    onConfirm : () -> Unit,
    onDismiss : () -> Unit,
    value : MutableState<String>
){
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {Text(text = "Add a course")},
        text = {
            OutlinedTextField(
                value = value.value,
                onValueChange = {value.value = it},
                placeholder = {Text(text = "course title")}
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm
            ) {
                Text(text = "add")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.White,
                    contentColor = Color.Gray
                )
            ) {
                Text(text = "cancel")
            }
        }
    )
}

fun checkLocationSetting(
    context: Context,
    laucher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>,
){
    val locationRequest = LocationRequest.create().apply {
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
    val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
    val settingClient = LocationServices.getSettingsClient(context)
    val locationSettingsResponseTask = settingClient.checkLocationSettings(builder.build())

    locationSettingsResponseTask.addOnFailureListener{exception ->
        if(exception is ResolvableApiException){
            try {
                val intentSenderRequest = IntentSenderRequest.Builder(exception.resolution).build()
                laucher.launch(intentSenderRequest)
            }catch (sendEx : IntentSender.SendIntentException){
                Timber.i("Error : $sendEx")
            }
        }
    }

    locationSettingsResponseTask.addOnCompleteListener {
        if(it.isSuccessful){
            Timber.i("Location on successfully.....")
        }
    }
}

 fun sendServiceCommand(action : String, context: Context) =
    Intent(context, TrackingService::class.java).also {
        it.action = action
        context.startService(it)
    }
