package com.evil.coolbutton

import android.widget.Button

class CoolButton {
    companion object {
        fun applyLocationEnabledStyle(button: Button) {
            button.text = "Thanks!"
            button.isEnabled = false
            trackUserLocationIfAvailable(button.context)
        }

        fun applyLocationDisabledStyle(button: Button) {
            button.text = "Please give me location"
            button.isEnabled = true
        }
    }
}