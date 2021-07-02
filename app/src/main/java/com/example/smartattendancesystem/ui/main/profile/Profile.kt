package com.example.smartattendancesystem.ui.main.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartattendancesystem.R
import com.example.smartattendancesystem.model.User
import com.example.smartattendancesystem.ui.main.update.UpdateState
import com.example.smartattendancesystem.ui.theme.AppBarAlphas
import com.example.smartattendancesystem.util.LoadImage
import com.example.smartattendancesystem.util.rememberFlowWithLifecycle
import timber.log.Timber


@Composable
fun Profile(){

    val viewModel : ProfileViewModel = hiltViewModel()
    val viewState by rememberFlowWithLifecycle(flow = viewModel.user)
        .collectAsState(initial = User())

    Scaffold(
        topBar = {ProfileTopAppBar(modifier = Modifier.fillMaxWidth())},
        modifier = Modifier.fillMaxSize(),
        content = {
            ProfileScreen(modifier = Modifier.fillMaxWidth()) {
                ProfileContent(viewState)
            }
        }
    )

}



@Composable
private fun ProfileContent(user: User) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileImage(user.imageUri)
        Spacer(modifier = Modifier.height(30.dp))
        ProfileInputText(label = "Email", value = user.email)
        Spacer(modifier = Modifier.height(16.dp))
        ProfileInputText(label = "Full Name", value = user.name)
        Spacer(modifier = Modifier.height(16.dp))
        ProfileInputText(label = "School", user.school)

    }

}




@Composable
private fun ProfileImage(imageUri : String){

    val image = LoadImage(url = imageUri)
    image.value?.let {
        Image(
        bitmap = it.asImageBitmap(),
        contentScale = ContentScale.Crop,
        contentDescription = null,
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape),
    )
    }
}



@Composable
private fun ProfileInputText(label : String, value : String){
    TextField(
        value = value,
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

