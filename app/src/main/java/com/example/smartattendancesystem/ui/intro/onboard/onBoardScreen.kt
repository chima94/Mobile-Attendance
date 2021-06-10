package com.example.smartattendancesystem.ui.intro.onboard

import android.animation.ValueAnimator
import android.content.Context
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.airbnb.lottie.LottieAnimationView
import com.example.smartattendancesystem.util.Pager
import com.example.smartattendancesystem.util.PagerState


enum class Navigation{
    LOGIN, REGISTER
}

@Composable
fun OnBoardScreen(onClick : (Navigation) -> Unit){
    val pagerState : PagerState = run{
        remember{
            PagerState(0, 0, onboardingList.size -1)
        }
    }

    Scaffold(
        content = { innerPadding ->
            Box(modifier = Modifier.fillMaxSize()){
                Pager(
                    state = pagerState,
                    orientation = Orientation.Horizontal,
                    modifier = Modifier.fillMaxSize()
                ){
                    OnboardingPagerItem(page = page)
                }
                
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 120.dp)
                ) {
                    onboardingList.forEachIndexed{ index, _ ->
                        OnBoardingPagerSlide(
                            selected = index == pagerState.currentPage,
                            color = MaterialTheme.colors.primary,
                            icon = Icons.Filled.Album
                        )
                    }
                }
            }
        },
        bottomBar = {
            BottomButtons(onClick)
        }
    )
}



@Composable
fun BottomButtons(onClick: (Navigation) -> Unit){
    Surface(
        elevation = 3.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ){
            Button(
                modifier = Modifier.weight(2f),
                onClick = {onClick(Navigation.LOGIN)},
                enabled = true
            ) {
                Text(text = "Login")
            }
            
            Spacer(modifier = Modifier.width(16.dp))

            OutlinedButton(
                modifier = Modifier.weight(2f),
                onClick = {onClick(Navigation.REGISTER)}
            ) {
                Text(text = "Register")
            }
        }
    }
}


@Composable
fun OnboardingPagerItem(page : Int){
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        LottieLoadingView(LocalContext.current, onboardingList[page].third)
        Text(
            text = onboardingList[page].first,
            style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.ExtraBold),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
        Text(
            text = onboardingList[page].second,
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun LottieLoadingView(context : Context, file : String){
    val lottieView = remember{
        LottieAnimationView(context).apply {
            setAnimation(file)
            repeatCount = ValueAnimator.INFINITE
        }
    }

    AndroidView(
        {lottieView}, modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {
        it.playAnimation()
    }
}


@Composable
fun OnBoardingPagerSlide(selected : Boolean, color : Color, icon : ImageVector){
    Icon(imageVector = icon,
        modifier = Modifier.padding(4.dp),
        contentDescription = null,
        tint = if(selected) color else Color.Gray
    )
}


val onboardingList = listOf(
    Triple(
        "Attendance Made Easy",
        "Our application help students record their attendance as easy as possible.",
        "profile.json"
    ),
    Triple(
        "Reliable",
        "Our application is designed to improve efficiency by automating attendance recording.",
        "attendance-manager.json"
    ),
    Triple(
        "Attendance Tracking",
        "We provide dashboard for lecturers/instructors to view the statistics of student attendance.",
        "instructor.json"
    )
)
