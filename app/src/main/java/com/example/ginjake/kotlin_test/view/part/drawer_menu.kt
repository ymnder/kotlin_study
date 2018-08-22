package com.example.ginjake.kotlin_test.view.part

import android.app.Activity
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.widget.Toolbar
import android.view.View
import com.example.ginjake.kotlin_test.R

class DrawerMenu()  {

    fun create_menu(activity: Activity, drawer: DrawerLayout, toolbar:Toolbar){
        /* メニュー*/
        var toggle = object : ActionBarDrawerToggle(
                activity, /* host Activity */
                drawer, /* DrawerLayout object */
                toolbar, /* nav drawer icon to replace 'Up' caret */
                R.string.open, /* "open drawer" description */
                R.string.close  /* "close drawer" description */
        ) {
            /** Called when a drawer has settled in a completely closed state.  */
            override fun onDrawerClosed(view: View?) {
                super.onDrawerClosed(view);
            }
            /** Called when a drawer has settled in a completely open state.  */
            override fun onDrawerOpened(view: View?) {
                super.onDrawerOpened(view);
            }
        }

        toggle.syncState()
    }

}