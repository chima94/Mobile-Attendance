package com.example.smartattendancesystem.ui.intro.register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartattendancesystem.ui.intro.*
import com.example.smartattendancesystem.ui.theme.SmartAttendanceSystemTheme
import com.example.smartattendancesystem.util.*
import timber.log.Timber


@Composable
fun RegisterScreen(onNavigation: (RegisterAction) -> Unit){

    val viewModel : RegisterViewModel = viewModel()
    val viewState by rememberFlowWithLifecycle(flow = viewModel.state)
        .collectAsState(initial = RegisterDataState.Nothing)
    var loading = false

    when(viewState){
        RegisterDataState.Loading -> {
            loading = true
        }
        is RegisterDataState.Success -> {
            onNavigation(RegisterAction.SignIn)
        }
        is RegisterDataState.Error ->{
            onNavigation(RegisterAction.DisplayError((viewState as RegisterDataState.Error).message))
        }
    }

    RegisterScreenScaffold(loading = loading) { action ->
        when(action){
            RegisterAction.NavigateBack ->{ onNavigation(RegisterAction.NavigateBack)}
            is RegisterAction.Register ->{
                viewModel.register(action)
            }
        }
    }
}


@Composable
internal fun RegisterScreenScaffold(
    loading : Boolean,
    signUpAction : (RegisterAction) -> Unit
){
    var userSelected by remember{ mutableStateOf("Lecturer")}

    Scaffold(
        topBar = {
            SignInSignUpTopAppBar(
                topAppBarText = "Register",
                onBackPressed = {signUpAction(RegisterAction.NavigateBack)}
            )
        },

        content = {
            SignInSignUpScreen(modifier = Modifier.fillMaxWidth()) {
                Column {
                    UserChooser (userSelected = {
                        userSelected = it
                    })
                    SignUpContent(loading, userSelected, signUpAction)
                }
            }
        }
    )
}


@Composable
private fun UserChooser(
    userSelected: (String) -> Unit
) {

    var selected by remember{ mutableStateOf("Lecturer")}

    Row{
        RadioButton(selected = selected == "Lecturer", onClick = {
            selected = "Lecturer"
            userSelected(selected)
        })
            Text(
                text = "Lecturer",
                modifier = Modifier
                    .clickable(onClick = {
                        userSelected(selected)
                    })
                    .padding(start = 4.dp)
            )
        Spacer(modifier = Modifier.width(4.dp))

        RadioButton(selected = selected == "Student", onClick = {
            selected = "Student"
            userSelected(selected)
        })
        Text(
            text  = "Student",
            modifier = Modifier
                .clickable(onClick = {
                    userSelected(selected)
                })
                .padding(4.dp)
        )
    }
}




@Composable
private fun SignUpContent(
    loading : Boolean = false,
    userSelected: String,
    onNavigation: (RegisterAction) -> Unit
) {

    Column(modifier = Modifier.fillMaxWidth()) {

        val emailState = remember{ EditTextState(
            validator = {isEmailValid(it)} ,
            errorFor = { emailValidationError(it)}
        )}

        val nameState = remember{ EditTextState(
            validator = { isNameValid(it) },
            errorFor = { nameValidationError(it)}
        )}

        val passwordState = remember { PasswordState()}

        val confirmPasswordState = remember{ ConfirmPasswordState(passwordState)}

        val emailRequest = remember { FocusRequester() }



        NameEditText(
            nameState = nameState,
            imeAction = ImeAction.Next,
            onImeAction = {}
        )

        Spacer(modifier = Modifier.height(16.dp))

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

        Password(
            label = "Confirm password",
            passwordState = confirmPasswordState,
            onImeAction = {}
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {onNavigation(RegisterAction.Register(
                name = nameState.text,
                email = emailState.text,
                userSelected = userSelected,
                password = passwordState.text
            ))},
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.elevation(),
            enabled = emailState.isValid && nameState.isValid
                    && passwordState.isValid && confirmPasswordState.isValid && !loading

        ){
            Text(text = "Register")
        }

        if(loading){
            ProgressBar()
        }
    }


}

@Preview
@Composable
private fun RegisterScreen(){
    SmartAttendanceSystemTheme() {
        RegisterScreen{}
    }

}