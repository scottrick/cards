package com.hatfat.cards.util

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import android.graphics.Shader
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import org.intellij.lang.annotations.Language
import kotlin.math.max
import kotlin.math.min

class CardView : AppCompatImageView {
    private val camera: Camera = Camera()
    private val matrix: Matrix = Matrix()

    private var rotationX = 0f
    private var rotationY = 0f

    private var lastX = 0f
    private var lastY = 0f

    private var animator: ObjectAnimator? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    @Language("AGSL")
    val cardLightingShader = """
    uniform float3 normal;
    uniform shader inputShader;
    
    uniform vec3 lightPos; // Position of the light source
    uniform vec3 viewPos;  // Position of the camera/viewer
    uniform vec3 ambient;  // Ambient color
    uniform vec3 diffuse;  // Diffuse color
    uniform vec3 specular; // Specular color
    uniform float shininess; // Shininess factor
    
    half4 main(float2 coords) {
        vec4 currValue = inputShader.eval(coords);
        
        return currValue * half4(0.0, 0.0, 1.0, 1.0);
    }
""".trimIndent()

    private fun init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val shader = RuntimeShader(cardLightingShader)
            val effect = RenderEffect.createRuntimeShaderEffect(shader, "inputShader")
            this.setRenderEffect(effect)


//            shader.setFloatUniform("size", width.toFloat(), height.toFloat())
//            shader.setFloatUniform("time", 10f)

//            this.setRenderEffect(RenderEffect.createRuntimeShaderEffect(shader, "composable"))
//            this.setRenderEffect(RenderEffect.createBlurEffect(10f, 10f, Shader.TileMode.CLAMP))
//            this.setRenderEffect(RenderEffect.createShaderEffect(fixedColorShader))
//            paint.shader = fixedColorShader
        }
    }

    override fun setRotationY(rotationY: Float) {
        this.rotationY = rotationY
        invalidate()
    }

    override fun setRotationX(rotationX: Float) {
        this.rotationX = rotationX
        invalidate()
    }

    private fun startRotationBack() {
        stopRotationBack()

        val xPropertyHolder = PropertyValuesHolder.ofFloat("rotationX", rotationX, 0f)
        val yPropertyHolder = PropertyValuesHolder.ofFloat("rotationY", rotationY, 0f)

        val newAnimator =
            ObjectAnimator.ofPropertyValuesHolder(this, xPropertyHolder, yPropertyHolder)

        newAnimator.setDuration(300)
        newAnimator.start()

        animator = newAnimator
    }

    private fun stopRotationBack() {
        animator?.cancel()
        animator = null
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    stopRotationBack()
                    lastX = event.x
                    lastY = event.y
                }

                MotionEvent.ACTION_UP -> {
                    startRotationBack()
                }

                MotionEvent.ACTION_MOVE -> {
                    val changeX = event.x - lastX
                    val changeY = event.y - lastY
                    lastX = event.x
                    lastY = event.y

                    val scale = min(width, height) / 25f
                    val rotationChangeX = changeY / -scale
                    val rotationChangeY = changeX / scale
                    rotationX += rotationChangeX
                    rotationY += rotationChangeY

                    val maxRotation = 6f
                    rotationX = min(maxRotation, rotationX)
                    rotationX = max(-maxRotation, rotationX)
                    rotationY = min(maxRotation, rotationY)
                    rotationY = max(-maxRotation, rotationY)

                    invalidate()
                }
            }
        }

        return true
    }

    override fun onDraw(canvas: Canvas) {
        camera.save()
        camera.rotateX(rotationX)
        camera.rotateY(rotationY)
        camera.getMatrix(matrix)
        camera.restore()

        matrix.preTranslate(-width / 2f, -height / 2f)
        matrix.postTranslate(width / 2f, height / 2f)

        canvas.concat(matrix)

        super.onDraw(canvas)
    }
}
