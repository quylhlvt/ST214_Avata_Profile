package com.avatar.ocmaker.profile.custom.listener.listenerdraw

import android.view.MotionEvent
import com.avatar.ocmaker.profile.custom.DrawView


interface DrawEvent {
    fun onActionDown(tattooView: DrawView?, event: MotionEvent?)
    fun onActionMove(tattooView: DrawView?, event: MotionEvent?)
    fun onActionUp(tattooView: DrawView?, event: MotionEvent?)
}