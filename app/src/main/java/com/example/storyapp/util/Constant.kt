package com.example.storyapp.util

import android.Manifest

object Constant {
    var REQUIRED_PERMISSIONS = arrayOf(
        android.Manifest.permission.CAMERA
    )
    const val REQUEST_CODE_PERMISSIONS = 10
    const val ACCESS_PERMISSION_DEFAULT = 101
}