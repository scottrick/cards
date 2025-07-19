package com.hatfat.cards.util

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import org.intellij.lang.annotations.Language
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt

class CardView : AppCompatImageView {
    private val camera: Camera = Camera()
    private val matrix: Matrix = Matrix()

    private val unsetValue = -49f
    private var rotationX = 0f
    private var rotationY = 0f
    private var lastX = unsetValue
    private var lastY = unsetValue

    private var animator: ObjectAnimator? = null
    private var shader: RuntimeShader? = null

    private var touchHandlingIsEnabled = false

    private var maxRotation = 2f

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
    uniform float3 normalVec;
    uniform shader inputShader;
    
    uniform float2 size; 
    uniform vec3 lightPos; // Position of the light source
    uniform vec3 viewPos;  // Position of the camera/viewer
    uniform vec3 ambientColor;
    uniform vec3 diffuseColor;
    uniform vec3 specularColor;
    uniform float shininess;
    
    half4 main(float2 coords) {
        vec4 sourceColor = inputShader.eval(coords);
        float xPos = (coords.x / size.x - 0.5) * 6.3;
        float yPos = (coords.y / size.x - (size.y / size.x * 0.5)) * 6.3;
        vec3 vertPos = vec3(xPos, yPos, 0.0);
        
        // Already normalized
        vec3 normal = normalVec;
        
        vec3 lightDir = normalize(lightPos - vertPos);
        float lambertian = max(dot(lightDir, normal), 0.0);
        float specular = 0.0;
        
        if (lambertian > 0.0) {
            vec3 viewDir = normalize(viewPos - vertPos);
                
            // blinn phong
            vec3 halfDir = normalize(lightDir + viewDir);
            float specAngle = max(dot(halfDir, normal), 0.0);
            specular = pow(specAngle, shininess);
        }
          
        vec3 colorLinear = 
            ambientColor 
            + diffuseColor * lambertian 
            + specularColor * specular
            ;
            
        return vec4(colorLinear, 1.0) * sourceColor;
    }
""".trimIndent()

    private fun init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val newShader = RuntimeShader(cardLightingShader)
            newShader.setFloatUniform("normalVec", 0.0f, 0.0f, 1.0f)
            newShader.setFloatUniform("lightPos", 10.0f, 10.0f, 200.0f)
            newShader.setFloatUniform("viewPos", 0.0f, 0.0f, 20.0f)
            newShader.setFloatUniform("ambientColor", 0.2f, 0.2f, 0.2f)
            newShader.setFloatUniform("diffuseColor", 0.8f, 0.8f, 0.8f)
            newShader.setFloatUniform("specularColor", 0.5f, 0.5f, 0.5f)
            newShader.setFloatUniform("shininess", 2000.0f)
            shader = newShader

            val effect = RenderEffect.createRuntimeShaderEffect(newShader, "inputShader")
            this.setRenderEffect(effect)
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

    fun setMaxRotation(maxRotation: Float) {
        this.maxRotation = maxRotation
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

    fun setTouchHandlingIsEnabled(value: Boolean) {
        this.touchHandlingIsEnabled = value
    }

    fun rotateForMotionEvent(event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                stopRotationBack()
                lastX = event.x
                lastY = event.y
            }

            MotionEvent.ACTION_UP -> {
                startRotationBack()
                lastX = unsetValue
                lastY = unsetValue
            }

            MotionEvent.ACTION_MOVE -> {
                if (lastX == unsetValue && lastY == unsetValue) {
                    // When the CardView is inside the carousel, it doesn't always get ACTION_DOWN, so we are manually detecting it here.
                    stopRotationBack()
                    lastX = event.x
                    lastY = event.y
                    return
                }

                val changeX = event.x - lastX
                val changeY = event.y - lastY
                lastX = event.x
                lastY = event.y

                val scale = min(width, height) / 25f
                val rotationChangeX = changeY / -scale
                val rotationChangeY = changeX / scale
                rotationX += rotationChangeX
                rotationY += rotationChangeY

                rotationX = min(maxRotation, rotationX)
                rotationX = max(-maxRotation, rotationX)
                rotationY = min(maxRotation, rotationY)
                rotationY = max(-maxRotation, rotationY)

                invalidate()
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (!touchHandlingIsEnabled) {
            return super.onTouchEvent(event)
        }

        event?.let {
            rotateForMotionEvent(it)
        }

        return true
    }

    override fun onDraw(canvas: Canvas) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            shader?.let { shader ->
                shader.setFloatUniform("size", width.toFloat(), height.toFloat())

                val sinX = sin(rotationX * Math.PI / 180.0);
                val cosX = cos(rotationX * Math.PI / 180.0);
                val sinY = sin(rotationY * Math.PI / 180.0);
                val cosY = cos(rotationY * Math.PI / 180.0);

                val x = sinY * cosX
                val y = -sinX
                val z = cosY * cosX

                val magnitude = sqrt(x * x + y * y + z * z).toFloat()
                val normalizedX = x.toFloat() / magnitude
                val normalizedY = y.toFloat() / magnitude
                val normalizedZ = z.toFloat() / magnitude

                shader.setFloatUniform("normalVec", normalizedX, normalizedY, normalizedZ)

                val effect = RenderEffect.createRuntimeShaderEffect(shader, "inputShader")
                this.setRenderEffect(effect)
            }
        }

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
