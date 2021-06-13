package com.example.smartattendancesystem.ui.intro.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartattendancesystem.ui.*
import com.example.smartattendancesystem.ui.intro.*
import com.example.smartattendancesystem.util.*


@Composable
internal fun LoginScreen(loginAction : (LoginAction) -> Unit){
    val viewModel : LoginViewModel = viewModel()
    val viewState by rememberFlowWithLifecycle(flow = viewModel.state)
        .collectAsState(initial = LoginState.Nothing)
    var loading = false
    when(viewState){
        LoginState.Loading -> loading = true
        is LoginState.Success ->{loginAction(LoginAction.SignIn)}
        is LoginState.Error -> {
            loginAction(LoginAction.DisplayError((viewState as LoginState.Error).message))
        }
    }

    LoginScreenScaffold(loading){action ->
        when(action){
            LoginAction.NavigateBack ->{ loginAction(LoginAction.NavigateBack)}
            is LoginAction.Login ->{
                viewModel.login(action.email, action.password)
            }
        }

    }
}


@Composable
internal fun LoginScreenScaffold(loading : Boolean, action : (LoginAction) -> Unit){

    Scaffold(
        topBar = {
            SignInSignUpTopAppBar(
                topAppBarText = "Sign In",
                onBackPressed = {action(LoginAction.NavigateBack)}
            )
        },

        content = {
            SignInSignUpScreen(modifier = Modifier.fillMaxWidth()) {
                Column {
                    LoginContent(loading, action)
                }
            }
        }
    )

}




@Composable
private fun LoginContent(
    loading: Boolean = false,
    action : (LoginAction) -> Unit
){

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
            onClick = {action(LoginAction.Login(
                email = emailState.text,
                password = passwordState.text
            ))},
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.elevation(),
            enabled = emailState.text.isNotBlank() &&  passwordState.text.isNotBlank() && !loading

        ){
            Text(text = "Login")
        }
        if(loading){
            ProgressBar()
        }
    }
}