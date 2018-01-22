package com.example.ginjake.kotlin_test

import android.content.Context
import android.widget.Toast

/**
 * Created by ginjake on 2018/01/19.
 */
fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}