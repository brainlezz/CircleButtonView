package de.brainlezz.custom

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class MainActivity : AppCompatActivity(), CircleButtonView.OnElementClickedListener, IconProvider {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var circleButtonView = findViewById<CircleButtonView>(R.id.circleButtonView)

        circleButtonView.addClickListener(this)
        circleButtonView.iconProvider = this
    }

    override fun onElementClicked(index: Int) {
        Toast.makeText(baseContext, "Clicked $index", Toast.LENGTH_SHORT).show()
    }

    override fun getIconForIndex(index: Int): Bitmap? {
        var resourceId = 0
        var dstHeight = 100
        var dstWith = 100
        when(index){
            0 -> {
                resourceId = R.drawable.play
                dstWith = 200
                dstHeight = 200
            }
            1 -> resourceId = R.drawable.arrow_right
            2 -> resourceId = R.drawable.arrow_bottom
            3 -> resourceId = R.drawable.arrow_left
            4 -> resourceId = R.drawable.arrow_top

        }
        if(resourceId != 0) {
            var bitmap = BitmapFactory.decodeResource(baseContext.resources, resourceId)
            return Bitmap.createScaledBitmap(bitmap, dstWith, dstHeight, false)
        }

        return null
    }
}