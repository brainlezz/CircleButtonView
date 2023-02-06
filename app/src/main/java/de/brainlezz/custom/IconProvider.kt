package de.brainlezz.custom

import android.graphics.Bitmap

interface IconProvider {
    fun getIconForIndex(index : Int) : Bitmap?
}