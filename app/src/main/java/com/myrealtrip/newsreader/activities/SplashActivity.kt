package com.myrealtrip.newsreader.activities

import android.content.Intent
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.myrealtrip.newsreader.R
import com.myrealtrip.newsreader.common.SPLASH_TIME_OUT
import kotlinx.android.synthetic.main.activity_splash.*

/*
 * Created by seongjinkim on 2020-03-28
 */
class SplashActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        maskCircle()
        Handler().postDelayed({
                                  startActivity(Intent(this, MainActivity::class.java))
                              }, SPLASH_TIME_OUT)
    }

    private fun maskCircle() {
        try {
            var origin = BitmapFactory.decodeResource(resources, R.drawable.img_news_main)
            if (origin == null) {
                return
            }
            iv_news.setImageBitmap(getMaskImage(origin, R.drawable.img_circle_mask))
        } catch (me: OutOfMemoryError) {
            me.printStackTrace()
        }
    }

    private fun getMaskImage(origin: Bitmap, maskSrc: Int): Bitmap {
        var result = Bitmap.createBitmap(origin.width, origin.height, Bitmap.Config.ARGB_8888)
        var maskMap = BitmapFactory.decodeResource(resources, maskSrc)
        var mask = Bitmap.createScaledBitmap(maskMap, origin.width, origin.height, true)
        var paint = getMaskPaint()
        drawMask(result, origin, mask, paint)
        return result
    }

    private fun getMaskPaint(): Paint {
        var paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.DST_IN))
        return paint
    }

    private fun drawMask(target: Bitmap, origin: Bitmap, mask: Bitmap, paint: Paint) {
        var canvas = Canvas(target)
        canvas.drawBitmap(origin, 0f, 0f, null)
        canvas.drawBitmap(mask, 0f, 0f, paint)
        paint.setXfermode(null)
        paint.style = Paint.Style.STROKE
    }
}
