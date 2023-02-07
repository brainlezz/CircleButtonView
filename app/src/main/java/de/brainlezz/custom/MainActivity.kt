package de.brainlezz.custom

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import com.larswerkman.lobsterpicker.OnColorListener
import com.larswerkman.lobsterpicker.sliders.LobsterShadeSlider

class MainActivity : AppCompatActivity(), CircleButtonView.OnElementClickedListener, CircleButtonView.IconProvider {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var circleButtonView = findViewById<CircleButtonView>(R.id.circleButtonView)

        findViewById<SeekBar>(R.id.seekBarBorderWidth).onProgressChanged{ circleButtonView.borderWidth = it }
        findViewById<SeekBar>(R.id.seekBarSpacingWidth).onProgressChanged{ circleButtonView.elementSpacing = it }
        findViewById<SeekBar>(R.id.seekBarElementCount).onProgressChanged{ circleButtonView.circleElementCount = it }
        findViewById<SeekBar>(R.id.seekBarOffset).onProgressChanged{ circleButtonView.angleOffset = it.toFloat() }
        findViewById<SeekBar>(R.id.seekBarInnerPortion).onProgressChanged{ circleButtonView.innerCirclePortion = it }

        findViewById<LobsterShadeSlider>(R.id.shadeInnerColor).onColorChanged { circleButtonView.innerColor = it }
        findViewById<LobsterShadeSlider>(R.id.shadeOuterColor).onColorChanged { circleButtonView.outerColor = it }
        findViewById<LobsterShadeSlider>(R.id.shadeBorderColor).onColorChanged { circleButtonView.borderColor = it }
        findViewById<LobsterShadeSlider>(R.id.shadehighlightColor).onColorChanged { circleButtonView.highlightColor = it }

        circleButtonView.addClickListener(this)
        circleButtonView.iconProvider = this
    }

    override fun onElementClicked(index: Int) {
        Toast.makeText(baseContext, "Clicked $index", Toast.LENGTH_SHORT).show()
    }

    override fun getIconForIndex(index: Int): Bitmap? {
        var resourceId = 0
        var dstHeight = 50
        var dstWith = 50
        var icons = listOf(R.drawable.arrow_right,R.drawable.arrow_bottom,R.drawable.arrow_left, R.drawable.arrow_top)

        when(index){
            0 -> {
                resourceId = R.drawable.play
                dstWith = 100
                dstHeight = 100
            }
            else -> resourceId = icons[Math.floorMod(index, 4)]
        }
        if(resourceId != 0) {
            var bitmap = BitmapFactory.decodeResource(baseContext.resources, resourceId)
            return Bitmap.createScaledBitmap(bitmap, dstWith, dstHeight, false)
        }

        return null
    }



    //region Extensions
    private fun Int.toDp(): Float =
        this * resources.displayMetrics.density

    private fun SeekBar.onProgressChanged(onProgressChanged: (Int) -> Unit) {
        setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                onProgressChanged(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Nothing
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Nothing
            }
        })
    }

    private fun LobsterShadeSlider.onColorChanged(onColorChanged: (Int) -> Unit) {
        addOnColorListener(object : OnColorListener {
            override fun onColorChanged(color: Int) {
                onColorChanged(color)
            }

            override fun onColorSelected(color: Int) {
                // Nothing
            }
        })
    }
    //endregion

}