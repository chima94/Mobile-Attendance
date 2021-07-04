package com.example.smartattendancesystem.ui.main.attendance

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartattendancesystem.model.User
import com.example.smartattendancesystem.model.local.ClassModel
import com.example.smartattendancesystem.ui.theme.gradientGreen
import com.example.smartattendancesystem.ui.theme.typography
import com.example.smartattendancesystem.util.horizontalGradientBackground
import com.example.smartattendancesystem.util.rememberFlowWithLifecycle
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import timber.log.Timber


@Composable
fun Attendance(verify : () -> Unit){

    val viewModel : AttendanceViewModel = hiltViewModel()
    val viewState by rememberFlowWithLifecycle(flow = viewModel.userData)
        .collectAsState(initial = User())
    val courseTitle = remember{ mutableStateOf("")}
    val classes = viewModel.classes.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            if(viewState.imageUri != ""){
                AttendanceTopBar(modifier = Modifier.fillMaxWidth())
            }
        },
        floatingActionButton = {
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
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            if(viewState.imageUri == ""){
                VerifyAccount(verify, viewState.name)
            }
            AttendanceContent(classModels = classes.value)
        }
    }
}




@Composable
fun AttendanceContent(classModels: List<ClassModel>){
    LazyColumn {
      items(items = classModels){classModel ->
          ClassListRow(classModel = classModel)
      }
    }
}



@Composable
fun ClassListRow(classModel : ClassModel){
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
            checked = false,
            colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colors.primary),
            onCheckedChange = {  }
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