package com.example.smartattendancesystem.ui.intro

class PasswordState : TextFieldState(
    validator = { isPasswordValid(it) },
    errorFor = { passwordValidationError()}
)

class LoginPassword : TextFieldState(
    validator = {true},
    errorFor = {""}
)

class ConfirmPasswordState(private val passwordState: PasswordState) : TextFieldState(){

    override val isValid: Boolean
        get() = passwordAndConfirmationValid(passwordState.text, text)


    override fun getError(): String? {
        return if(showError()){
            passwordConfirmationError()
        }else{
            null
        }
    }
}



private fun passwordAndConfirmationValid(password : String, confirmPassword : String) : Boolean{
    return  password == confirmPassword
}



private fun isPasswordValid(password : String) : Boolean{
    return password.length > 5
}


private fun passwordValidationError() : String{
    return "password must be greater than 5"
}

private fun passwordConfirmationError() : String{
    return "passwords don't match"
}