package de.brainlezz.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.lang.Integer.min
import kotlin.math.*

class CircleButtonView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    companion object{
        const val DEFAULT_COLOR = Color.GRAY
        const val DEFAULT_HIGHLIGHT_COLOR = Color.LTGRAY
        const val DEFAULT_BORDER_WIDTH = 5
        const val DEFAULT_BORDER_COLOR = Color.BLACK
        const val DEFAULT_INNER_CIRCLE_PORTION = 50
        const val DEFAULT_ANGLE_OFFSET = 0f
        const val DEFAULT_CIRCLE_ELEMENT_COUNT = 4
        const val DEFAULT_ELEMENT_SPACING = 10
        const val DEFAULT_CENTER_ENABLED = true

        const val NOTHING_TOUCHED_ELEMENT = -1
        const val INNER_CIRCLE_ELEMENT = 0
    }

    private val outerPaint = Paint().apply { isAntiAlias = true }
    private val innerPaint = Paint().apply { isAntiAlias = true }
    private val borderPaint = Paint().apply { isAntiAlias = true }
    private val highlightPaint = Paint().apply { isAntiAlias = true }
    private val spacingPaint = Paint().apply { isAntiAlias = true }

    private var circleCenter = 0f
    private var circleDiameter = 0
    private var radiusInnerCircle = 0f
    private var touchedElement = NOTHING_TOUCHED_ELEMENT
    private var touchedAngle = 0f

    private var clickedListeners = ArrayList<OnElementClickedListener>()


    interface OnElementClickedListener{
        fun onElementClicked(index : Int)
    }

    interface IconProvider {
        fun getIconForIndex(index : Int) : Bitmap?
    }

    var angleOffset = DEFAULT_ANGLE_OFFSET
        set(value) {
            if(value >= 0 && value < 360) {
                field = value
                update()
            }else{
                throw java.lang.IllegalArgumentException("Angle Offset has to be between 0 and 360!")
            }
        }
    var circleElementCount = DEFAULT_CIRCLE_ELEMENT_COUNT
        set(value) {
            if(value >= 2) {
                field = value
                update()
            }else{
                throw java.lang.IllegalArgumentException("At least two elements are needed!")
            }
        }

    var elementSpacing = DEFAULT_ELEMENT_SPACING
        set(value) {
            field = value
            update()
        }

    var highlightColor: Int = DEFAULT_HIGHLIGHT_COLOR
        set(value) {
            field = value
            update()
        }

    var innerColor: Int = DEFAULT_COLOR
        set(value) {
            field = value
            update()
        }

    var outerColor: Int = DEFAULT_COLOR
        set(value) {
            field = value
            update()
        }

    var borderColor: Int = DEFAULT_BORDER_COLOR
        set(value) {
            field = value
            update()
        }

    var borderWidth: Int = DEFAULT_BORDER_WIDTH
        set(value) {
            field = value
            update()
        }

    var innerCirclePortion: Int = DEFAULT_INNER_CIRCLE_PORTION
        set(value) {
            if(value in 10..90) {
                field = value
                update()
            }else{
                throw java.lang.IllegalArgumentException("For a resonable result the inner circle portion should be between 10 and 90%!")
            }
        }

    var iconProvider: IconProvider? = null
        set(value) {
            field = value
            update()
        }

    var centerEnabled: Boolean = DEFAULT_CENTER_ENABLED
        set(value) {
            field = value
            update()
        }

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CircleButtonView)

        attributes.getColor(R.styleable.CircleButtonView_innerColor, DEFAULT_COLOR)
            .also {
                if (it != 0) innerColor = it
            }
        attributes.getColor(R.styleable.CircleButtonView_outerColor, DEFAULT_COLOR)
            .also { if (it != 0) outerColor = it }
        attributes.getColor(R.styleable.CircleButtonView_highlightColor, DEFAULT_HIGHLIGHT_COLOR)
            .also { if (it != 0) highlightColor = it }
        attributes.getColor(R.styleable.CircleButtonView_borderColor, DEFAULT_BORDER_COLOR)
            .also { if (it != 0) borderColor = it }

        attributes.getInteger(R.styleable.CircleButtonView_angleOffset, DEFAULT_ANGLE_OFFSET.toInt())
            .also { angleOffset = it.toFloat() }

        attributes.getInteger(R.styleable.CircleButtonView_elementCount, DEFAULT_CIRCLE_ELEMENT_COUNT)
            .also { if (it != 0) circleElementCount = it }
        attributes.getInteger(R.styleable.CircleButtonView_elementSpacing, DEFAULT_ELEMENT_SPACING)
            .also { elementSpacing = it }
        attributes.getInteger(R.styleable.CircleButtonView_customBorderWidth, DEFAULT_BORDER_WIDTH)
            .also { borderWidth = it }
        attributes.getInteger(R.styleable.CircleButtonView_innerCirclePortion, DEFAULT_INNER_CIRCLE_PORTION)
            .also { if (it != 0) innerCirclePortion = it }

        attributes.getBoolean(R.styleable.CircleButtonView_centerEnabled, DEFAULT_CENTER_ENABLED)
            .also { centerEnabled = it }

        attributes.recycle()
    }

    public fun addClickListener(listener : OnElementClickedListener) = clickedListeners.add(listener)

    public fun removeClickListener(listener : OnElementClickedListener){
        if (clickedListeners.contains(listener))
            clickedListeners.remove(listener)
    }

    public fun clearListeners() {
        clickedListeners = ArrayList<OnElementClickedListener>()
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        update()
    }



    private fun update(){
        val usableWidth = (width - 2*borderWidth) - (paddingLeft + paddingRight)
        val usableHeight = (height - 2* borderWidth) - (paddingTop + paddingBottom)
        circleDiameter = min(usableWidth, usableHeight)
        circleCenter = circleDiameter / 2f
        radiusInnerCircle = innerCirclePortion.toFloat() * circleCenter / 100
        updateColors()
        invalidate()
    }

    private fun updateColors(){
        outerPaint.color = outerColor//if(touchedElement > 0) Color.LTGRAY else outerColor
        innerPaint.color = innerColor
        highlightPaint.color = highlightColor
        borderPaint.color = borderColor
        borderPaint.strokeWidth = borderWidth.toFloat()
        spacingPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        spacingPaint.strokeWidth = elementSpacing.toFloat()
    }

    private fun getTouchedElementIndex(x : Float, y : Float) : Int {
        val centerDistance = sqrt(
            (x - circleCenter).toDouble().pow(2.0) +
            (y - circleCenter).toDouble().pow(2.0)
        )

        if(centerDistance < radiusInnerCircle) {
            return INNER_CIRCLE_ELEMENT
        }
        else if (centerDistance < circleDiameter/2){
            touchedAngle = Math.toDegrees(
                atan2(
                    (y - circleCenter).toDouble(),
                    (x - circleCenter).toDouble()
                )
            ).toFloat()


            var startAngle = if(touchedAngle > 0) touchedAngle else 180f + (180f + touchedAngle)
            // we need to add 90° here, because this is giving us a x-axis dependent angleR
            startAngle = (startAngle + 90f - angleOffset).mod(360f)

            // +1 because 0 is our inner circle
            touchedElement = 1 +  (startAngle / (360f/circleElementCount)).toInt()
            //Toast.makeText(context, "${touchedElement.toString()}", Toast.LENGTH_SHORT).show()
            return touchedElement
            }
        return NOTHING_TOUCHED_ELEMENT
    }

    private fun notifyListeners(){
        for(listener : OnElementClickedListener in clickedListeners) {
            if (touchedElement == INNER_CIRCLE_ELEMENT && !centerEnabled) return
            listener.onElementClicked(touchedElement)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y

        when(event.action ){
            MotionEvent.ACTION_DOWN -> {
                touchedElement = getTouchedElementIndex(touchX, touchY)
                if(touchedElement >= INNER_CIRCLE_ELEMENT)
                    notifyListeners()
                update()
            }
            MotionEvent.ACTION_UP -> {
                touchedElement = NOTHING_TOUCHED_ELEMENT
                update()
            }
        }

        postInvalidate()
        return true
    }

    private fun drawIcon(canvasBitmap: Canvas, index : Int) {
        //if(index > 1) return
        val elementAngle = 360f / circleElementCount

        var startX = circleCenter
        var startY = circleCenter
        var angle = (elementAngle * index + elementAngle/2 + angleOffset).toDouble() * PI / 180
        var endX  = startX + (radiusInnerCircle + (circleDiameter/2 - radiusInnerCircle) / 2f) * sin(angle)
        var endY  = startY - (radiusInnerCircle + (circleDiameter/2 - radiusInnerCircle) / 2f) * cos(angle)

        val bitmap = iconProvider?.getIconForIndex(index + 1)

        if(bitmap != null) {
            var bitmapX = endX.toFloat() - bitmap.width.toFloat() / 2f
            var bitmapY = endY.toFloat() - bitmap.height.toFloat() / 2f
            canvasBitmap.drawBitmap(bitmap, bitmapX, bitmapY, null)
        }
    }

    private fun drawCenterIcon(canvasBitmap: Canvas){
        val bitmap = iconProvider?.getIconForIndex(INNER_CIRCLE_ELEMENT)
        if(bitmap != null) {
            canvasBitmap.drawBitmap(bitmap,
                circleCenter - bitmap.width.toFloat() / 2f,
                circleCenter - bitmap.height.toFloat() / 2f, null)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var bitmap = Bitmap.createBitmap(circleDiameter, circleDiameter, Bitmap.Config.ARGB_8888)
        bitmap.eraseColor(Color.TRANSPARENT)

        var canvasBitmap = Canvas(bitmap)

        // draw border
        if(borderWidth > 0){
            canvasBitmap.drawCircle(
                circleCenter, circleCenter,
                circleCenter,
                borderPaint
            )
        }

        // draw outer circle
        canvasBitmap.drawCircle(
            circleCenter,
            circleCenter,
            circleCenter - borderWidth,
            outerPaint
        )

        // highlight circle element if touched
        if (touchedElement > INNER_CIRCLE_ELEMENT){
            var angleElement = 360f / circleElementCount
            // -1 because element 0 is the inner circle
            var angleStart = (touchedElement-1) * (360f/circleElementCount) - 90
            var borderSpacing = borderWidth.toFloat()
            canvasBitmap.drawArc(
                RectF(
                    borderSpacing, borderSpacing,
                    circleDiameter.toFloat() - borderWidth ,circleDiameter.toFloat()- borderWidth
                ),
                angleStart + angleOffset, angleElement, true, highlightPaint)


        }

        // draw outer circle inner border
        if(borderWidth > 0){
            canvasBitmap.drawCircle(
                circleCenter,
                circleCenter,
                radiusInnerCircle,
                borderPaint
            )
        }

        // draw lines
        if(borderWidth > 0 || elementSpacing > 0){
            canvasBitmap.save()
            canvasBitmap.rotate(angleOffset, circleCenter, circleCenter)
            val elementAngle = 360f / circleElementCount
            for(i in 0 until circleElementCount){
                var startX = circleCenter
                var startY = circleCenter
                var angle = (elementAngle * i).toDouble() * PI / 180
                var endX  = startX + circleDiameter/2f * sin(angle)
                var endY  = startY - circleDiameter/2f * cos(angle)

                if(elementSpacing > 0)
                    borderPaint.strokeWidth = 2f * borderWidth + elementSpacing
                // draw border
                canvasBitmap.drawLine(startX, startY, endX.toFloat(), endY.toFloat(), borderPaint)

                if(elementSpacing > 0) {
                    // draw spacing
                    // when border and spacing have the same length there is a tiny shadow of the border
                    // left. To avoid this we are adding 1 to the spacing beam length
                    endX  = startX + (circleDiameter/2f+1f) * sin(angle)
                    endY  = startY - (circleDiameter/2f+1f) * cos(angle)
                    canvasBitmap.drawLine(startX, startY, endX.toFloat(), endY.toFloat(), spacingPaint)
                }
            }
            canvasBitmap.restore()
        }

        // draw inner circle spacing and border
        if(elementSpacing > 0){
            // draw inner circle border
            canvasBitmap.drawCircle(
                circleCenter,
                circleCenter,
                radiusInnerCircle - borderWidth,
                spacingPaint
            )
            if(borderWidth > 0)
                canvasBitmap.drawCircle(
                    circleCenter,
                    circleCenter,
                    radiusInnerCircle - (borderWidth + elementSpacing),
                    borderPaint
                )
        }

        // draw inner circle and highlight if touched
        var innerCircleRadius =  radiusInnerCircle - borderWidth
        if(elementSpacing > 0)
            innerCircleRadius -= (elementSpacing + borderWidth)

        canvasBitmap.drawCircle(
            circleCenter,
            circleCenter,
            innerCircleRadius,
            if (touchedElement == INNER_CIRCLE_ELEMENT && centerEnabled) highlightPaint else innerPaint
        )

        if(iconProvider != null){
            drawCenterIcon(canvasBitmap)

            for(i in 0 until circleElementCount) {
                drawIcon(canvasBitmap, i)
            }
        }
        canvas.drawBitmap(bitmap, 0f, 0f, null)
    }

}