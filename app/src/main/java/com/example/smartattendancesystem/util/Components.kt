package com.example.smartattendancesystem.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.smartattendancesystem.ui.intro.TextFieldState


@Composable
fun SignInSignUpTopAppBar(topAppBarText : String, onBackPressed : () -> Unit){
    TopAppBar(
        title = {
            Text(
                text = topAppBarText,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        },

        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(imageVector = Icons.Filled.ChevronLeft,
                    contentDescription = "back"
                )
            }
        },
        actions = {
            Spacer(modifier = Modifier.width(68.dp))
        },
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 0.dp
    )
}


@Composable
fun EmailEditText(
    emailState : TextFieldState,
    modifier: Modifier = Modifier,
    imeAction : ImeAction = ImeAction.Next,
    onImeAction : () -> Unit = {}
){

    OutlinedTextField(
        value = emailState.text,
        onValueChange = {emailState.text = it},
        leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = null) },
        label = {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium){
                Text(
                    text = "Email",
                    style = MaterialTheme.typography.body2
                )
            }
        },
        placeholder = { Text(text = "Your email") },
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                emailState.onFocusedChange(focusState.isFocused)
                if (!focusState.isFocused) {
                    emailState.enableShowError()
                }
            },

        textStyle = MaterialTheme.typography.body2,
        isError = emailState.showError(),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = imeAction),
        keyboardActions = KeyboardActions(
            onDone = {
                onImeAction()
            }
        )
    )
    emailState.getError()?.let { error -> TextFieldError(errorText = error) }
}


@Composable
fun NameEditText(
    nameState : TextFieldState,
    modifier: Modifier = Modifier,
    imeAction : ImeAction = ImeAction.Next,
    onImeAction : () -> Unit = {}
){

    OutlinedTextField(
        value = nameState.text,
        onValueChange = {nameState.text = it},
        leadingIcon = { Icon(imageVector = Icons.Filled.Person, contentDescription = null) },
        label = {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium){
                Text(
                    text = "Full name",
                    style = MaterialTheme.typography.body2
                )
            }
        },
        placeholder = { Text(text = "Your name") },
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                nameState.onFocusedChange(focusState.isFocused)
                if (!focusState.isFocused) {
                    nameState.enableShowError()
                }
            },

        textStyle = MaterialTheme.typography.body2,
        isError = nameState.showError(),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = imeAction),
        keyboardActions = KeyboardActions(
            onDone = {
                onImeAction()
            }
        )
    )
    nameState.getError()?.let { error -> TextFieldError(errorText = error) }
}

@Composable
fun SignInSignUpScreen(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
){
    LazyColumn(modifier = modifier) {
        item {
            Spacer(modifier = Modifier.height(44.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ){
                content()
            }
        }

    }
}


@Composable
fun Password(
    label : String,
    passwordState : TextFieldState,
    modifier: Modifier = Modifier,
    imeAction : ImeAction = ImeAction.Done,
    onImeAction: () -> Unit = {}
){
    val showPassword = remember{ mutableStateOf(false) }

    OutlinedTextField(
        value = passwordState.text,
        onValueChange = {passwordState.text = it},
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                passwordState.onFocusedChange(focusState.isFocused)
                if (!focusState.isFocused) {
                    passwordState.enableShowError()
                }
            },
        textStyle = MaterialTheme.typography.body2,
        label =  {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.body2
                )
            }
        },
        trailingIcon = {
            if(showPassword.value){
                IconButton(onClick = { showPassword.value = false}) {
                    Icon(
                        imageVector = Icons.Filled.Visibility,
                        contentDescription = "hide password"
                    )
                }
            }else{
                IconButton(onClick = { showPassword.value = true}) {
                    Icon(
                        imageVector = Icons.Filled.VisibilityOff,
                        contentDescription = "show password"
                    )
                }
            }
        },
        placeholder = { Text(text = "password") },
        visualTransformation = if(showPassword.value){
            VisualTransformation.None
        }else{
            PasswordVisualTransformation()
        },
        isError = passwordState.showError(),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = imeAction),
        keyboardActions = KeyboardActions(
            onDone = {
                onImeAction()
            }
        )
    )
    passwordState.getError()?.let { TextFieldError(errorText = it) }
}



@Composable
fun TextFieldError(errorText : String){
    Row(modifier = Modifier.fillMaxWidth()){
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = errorText,
            modifier = Modifier.fillMaxWidth(),
            style = LocalTextStyle.current.copy(color = MaterialTheme.colors.error)
        )
    }
}


@Composable
fun ProgressBar(){
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}


@Composable
fun DefaultSnackbar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier,
    onDismiss : () -> Unit
){
    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = {data ->
            Snackbar(
                modifier = Modifier.padding(16.dp),
                snackbarData = data,
            )
        },
        modifier = modifier
    )
}
