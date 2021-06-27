package com.example.smartattendancesystem.ui.main.attendance

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartattendancesystem.model.User
import com.example.smartattendancesystem.ui.theme.gradientGreen
import com.example.smartattendancesystem.ui.theme.typography
import com.example.smartattendancesystem.util.horizontalGradientBackground
import com.example.smartattendancesystem.util.rememberFlowWithLifecycle


@Composable
fun Attendance(verify : () -> Unit){
    val viewModel : AttendanceViewModel = hiltViewModel()
    val viewState by rememberFlowWithLifecycle(flow = viewModel.userData)
        .collectAsState(initial = User())

    Scaffold(
        topBar = {
            if(viewState.imageUri != ""){
                AttendanceTopBar(modifier = Modifier.fillMaxWidth())
            }
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            if(viewState.imageUri == ""){
                VerifyAccount(verify, viewState.name)
            }
        }
    }
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