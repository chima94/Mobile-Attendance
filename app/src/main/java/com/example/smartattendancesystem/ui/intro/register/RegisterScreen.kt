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
import com.example.smartattendancesystem.ui.*
import com.example.smartattendancesystem.ui.intro.*
import com.example.smartattendancesystem.util.*


@Composable
fun RegisterScreen(onBackPressed : (Boolean) -> Unit){
    val viewModel = RegisterViewModel()

    RegisterScreenScaffold() { action ->
        when(action){
            RegisterAction.NavigateBack ->{ onBackPressed(true)}
            is RegisterAction.Register ->{
                viewModel.register()
            }
        }
    }
}


@Composable
internal fun RegisterScreenScaffold(signUpAction : (RegisterAction) -> Unit){
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
                    SignUpContent(userSelected,signUpAction)
                }
            }
        }
    )
}


@Composable
private fun UserChooser( userSelected: (String) -> Unit) {

    var selected by remember { mutableStateOf("Lecturer")}

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
private fun SignUpContent(userSelected: String, onNavigation: (RegisterAction) -> Unit) {

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
            enabled = emailState.isValid && nameState.isValid && passwordState.isValid && confirmPasswordState.isValid

        ){
            Text(text = "Register")
        }
    }


}

@Preview
@Composable
private fun RegisterScreen(){
    RegisterScreen{}
}