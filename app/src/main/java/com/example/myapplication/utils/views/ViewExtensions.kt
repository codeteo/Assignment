package com.example.myapplication.utils.views

import android.app.Activity
import com.google.android.material.snackbar.Snackbar

fun Activity.snackbar(message: String) =
    Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT)
