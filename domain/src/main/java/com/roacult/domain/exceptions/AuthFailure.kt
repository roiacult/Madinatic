package com.roacult.domain.exceptions

import com.roacult.kero.team7.jstarter_domain.exception.Failure

sealed class AuthFailure : Failure() {
    object InternetConnection : AuthFailure()
    object AuthFailed : AuthFailure()
    object InvalidEmail : AuthFailure()
    object AlredySignedIn :AuthFailure()
    object InvalidPassword : AuthFailure()
}


sealed class ProfileFailures : Failure() {
    object UserNotStored : ProfileFailures()
    object InternetConnection : ProfileFailures()
    object AuthFailed : ProfileFailures()
}