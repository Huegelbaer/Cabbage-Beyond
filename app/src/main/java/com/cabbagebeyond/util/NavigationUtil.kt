package com.cabbagebeyond.util

import android.app.Activity
import android.content.Intent
import com.cabbagebeyond.MainActivity
import com.cabbagebeyond.ui.auth.AuthenticationActivity

fun navigateIntoApp(activity: Activity) {
    val intent = Intent(activity, MainActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_TASK_ON_HOME
    activity.startActivity(intent)
    activity.finish()
}

fun navigateToAuthentication(activity: Activity) {
    val intent = Intent(activity, AuthenticationActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_TASK_ON_HOME
    activity.startActivity(intent)
    activity.finish()
}