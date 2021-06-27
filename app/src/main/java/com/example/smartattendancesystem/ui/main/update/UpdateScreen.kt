package com.example.smartattendancesystem.ui.main.update

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.smartattendancesystem.ui.intro.EditTextState
import com.example.smartattendancesystem.ui.intro.TextFieldState
import com.example.smartattendancesystem.ui.intro.isNameValid
import com.example.smartattendancesystem.ui.intro.nameValidationError
import com.example.smartattendancesystem.util.SignInSignUpScreen
import com.example.smartattendancesystem.util.SignInSignUpTopAppBar
import com.example.smartattendancesystem.util.TextFieldError


@Composable
fun UpdateScreen(onNavigateBack : () -> Unit){
    val school = remember{ mutableStateOf("")}
    val isOpen = remember{ mutableStateOf(false)}
    val userId = remember{ EditTextState(
        validator = { isNameValid(it) },
        errorFor = { nameValidationError(it) }
    )
    }

    Scaffold(
        topBar = {
             SignInSignUpTopAppBar(
                 topAppBarText = "Verify Account",
                 onBackPressed = onNavigateBack
             )
        },
        content = {
            SignInSignUpScreen(modifier = Modifier.fillMaxWidth()) {
                Column {
                    UpdateScreenContent(
                        userId,
                        school,
                        isOpen
                    )
                }
            }
        }
    )
}


@Composable
private fun UpdateScreenContent(
    userId: EditTextState,
    school: MutableState<String>,
    isOpen: MutableState<Boolean>
) {


    UserIdEditText(
        UserIdState = userId,
        imeAction = ImeAction.Next,
        onImeAction = {}
    )

    Spacer(modifier = Modifier.height(16.dp))

    SchoolSelection(
        school,
        isOpen
    )

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.elevation(),
        enabled = userId.isValid && school.value != ""

    ){
        Text(text = "Take Photo")
    }
}




@Composable
private fun DropDownList(
    requestToOpen : Boolean = false,
    list : List<String>,
    request : (Boolean) -> Unit,
    selectedString : (String) -> Unit
){
    DropdownMenu(
        expanded = requestToOpen,
        modifier= Modifier.fillMaxWidth(),
        onDismissRequest = { request(false) }
    ) {
        list.forEach{
            DropdownMenuItem(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    request(false)
                    selectedString(it)
                }
            ) {
                Text(
                    it,
                    modifier = Modifier
                        .wrapContentWidth()
                )
            }
        }
    }
}


@Composable
private fun SchoolSelection(school: MutableState<String>, isOpen: MutableState<Boolean>) {
    val schools = listOf(
        "Fupre",
        "Delsu",
        "PTI"
    )

    val openCloseOfDropDownList : (Boolean) -> Unit = {
        isOpen.value = it
    }
    val userSelection : (String) -> Unit = {
        school.value = it
    }

    Box{
        Column{
            OutlinedTextField(
                value = school.value,
                onValueChange = {school.value = it},
                label = {Text(text = "School")},
                modifier = Modifier.fillMaxWidth()
            )
            DropDownList(
                list = schools,
                requestToOpen = isOpen.value,
                request = openCloseOfDropDownList,
                selectedString = userSelection
            )
        }
        Spacer(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Transparent)
                .padding(10.dp)
                .clickable(
                    onClick = { isOpen.value = true }
                )
        )
    }
}


@Composable
fun UserIdEditText(
    UserIdState : TextFieldState,
    modifier: Modifier = Modifier,
    imeAction : ImeAction = ImeAction.Next,
    onImeAction : () -> Unit = {}
){

    OutlinedTextField(
        value = UserIdState.text,
        onValueChange = {UserIdState.text = it},
        label = {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium){
                Text(
                    text = "User Id",
                    style = MaterialTheme.typography.body2
                )
            }
        },
        placeholder = { Text(text = "User Id") },
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                UserIdState.onFocusedChange(focusState.isFocused)
                if (!focusState.isFocused) {
                    UserIdState.enableShowError()
                }
            },

        textStyle = MaterialTheme.typography.body2,
        isError = UserIdState.showError(),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = imeAction),
        keyboardActions = KeyboardActions(
            onDone = {
                onImeAction()
            }
        )
    )
    UserIdState.getError()?.let { error -> TextFieldError(errorText = error) }
}