package com.example.smartattendancesystem.ui.main.profile

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Update
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartattendancesystem.R
import com.example.smartattendancesystem.ui.theme.AppBarAlphas
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues


@Composable
fun Profile(){
    Scaffold(
        topBar = {ProfileTopAppBar(modifier = Modifier.fillMaxWidth())},
        modifier = Modifier.fillMaxSize(),
        content = {
            ProfileScreen(modifier = Modifier.fillMaxWidth()) {
                ProfileContent()
            }
        }
    )

}



@Composable
private fun ProfileContent(){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileImage()
        Spacer(modifier = Modifier.height(30.dp))
        ProfileInputText(label = "Email")
        Spacer(modifier = Modifier.height(16.dp))
        ProfileInputText(label = "Full Name")
        Spacer(modifier = Modifier.height(16.dp))
        ProfileInputText(label = "School")
    }

}




@Composable
private fun ProfileImage(){
    Image(
        painter = painterResource(id = R.drawable.p1),
        contentScale = ContentScale.Crop,
        contentDescription = null,
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape),
    )
}



@Composable
private fun ProfileInputText(label : String){
    TextField(
        value = "Name",
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        onValueChange = {},
        label = {Text(text = label)},

    )
}


@Composable
private fun ProfileScreen(
    modifier: Modifier,
    content : @Composable () -> Unit
){
    LazyColumn(modifier = modifier) {
       item {
           Spacer(modifier = Modifier.height(44.dp))
           Box(
             modifier = Modifier
                 .fillMaxSize()
                 .padding(horizontal = 20.dp)
           ){
               content()
           }
       }
    }
}



@Composable
private fun ProfileTopAppBar(modifier: Modifier){

    TopAppBar(
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.surface.copy(AppBarAlphas.translucentBarAlpha()),
        contentColor = MaterialTheme.colors.onSurface,
        title = {
            Text(
                text = "Edit Profile",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )

        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Filled.Done, contentDescription = "Done" )
            }

        },
        elevation = 0.dp,
    )

   
}

