package com.example.chatapplication.adapter

import android.view.View
import androidx.databinding.BindingAdapter

/**
 * Binding adapter used to hide the spinner once data is available.
 */
@BindingAdapter("isNetworkError")
fun hideIfNetworkError(view: View, eventNetworkError: Boolean) {
    if(eventNetworkError) {
        view.visibility = View.GONE
    }
}

@BindingAdapter("userList")
fun hideIfUserListDataUnavailable(view: View, userList: Any?) {
    view.visibility = if (userList != null) View.GONE else View.VISIBLE
}

@BindingAdapter("userLogin")
fun hideIfUserLoginDataUnavailable(view: View, userLoginData: Any?) {
    view.visibility = if (userLoginData != null) View.GONE else View.VISIBLE
}
