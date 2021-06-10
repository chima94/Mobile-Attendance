package com.example.smartattendancesystem.ui.intro.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.smartattendancesystem.ui.*
import com.example.smartattendancesystem.ui.intro.*
import com.example.smartattendancesystem.util.EmailEditText
import com.example.smartattendancesystem.util.Password
import com.example.smartattendancesystem.util.SignInSignUpScreen
import com.example.smartattendancesystem.util.SignInSignUpTopAppBar


@Composable
fun LoginScreen(onBackPressed : (Boolean) -> Unit){
    val viewModel = LoginViewModel()

    LoginScreenScaffold(){action ->
        when(action){
            LoginAction.NavigateBack ->{ onBackPressed(true)}
            is LoginAction.Login ->{
                viewModel.login()
            }
        }

    }
}


@Composable
internal fun LoginScreenScaffold(onNavigation : (LoginAction) -> Unit){

    Scaffold(
        topBar = {
            SignInSignUpTopAppBar(
                topAppBarText = "Sign In",
                onBackPressed = {onNavigation(LoginAction.NavigateBack)}
            )
        },

        content = {
            SignInSignUpScreen(modifier = Modifier.fillMaxWidth()) {
                Column {
                    LoginContent(onNavigation)
                }
            }
        }
    )

}




@Composable
private fun LoginContent(onNavigation: (LoginAction) -> Unit){

    Column(modifier = Modifier.fillMaxWidth()) {

        val emailState = remember{ EditTextState(
            validator = { true } ,
            errorFor = { emailValidationError(it) }
        )
        }
        val passwordState = remember { LoginPassword() }
        val emailRequest = remember { FocusRequester() }

        EmailEditText(
            emailState = emailState,
            imeAction = ImeAction.Next,
            onImeAction = {emailRequest.requestFocus()}
        )

        Spacer(modifier = Modifier.height(16.dp))


        Password(
            label = "Password",
            passwordState = passwordState,
            onImeAction = {}
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {onNavigation(LoginAction.Login(
                email = emailState.text,
                password = passwordState.text
            ))},
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.elevation(),
            enabled = emailState.text.isNotBlank() &&  passwordState.text.isNotBlank()

        ){
            Text(text = "Login")
        }
    }
}