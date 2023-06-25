package com.lxn.gdghanoicheckin.qr

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder


object BarcodeImageGenerator {
    private val encoder = BarcodeEncoder()
    private val writer = MultiFormatWriter()

    suspend fun generateBitmap(
        content : String,
        width: Int,
        height: Int,
        margin: Int = 0,
        codeColor: Int = Color.BLACK,
        backgroundColor: Int = Color.WHITE
    ): Bitmap {
        try {
            val matrix = encoder.encode(
                content,
                BarcodeFormat.QR_CODE,
                width,
                height,
                createHints(margin)
            )
            return createBitmap(matrix, codeColor, backgroundColor)
        } catch (ex: Exception) {
            throw Exception("Unable to generate barcode image", ex)
        }
    }

    private fun createHints(margin: Int): Map<EncodeHintType, Any> {
        return mapOf<EncodeHintType, Any>(
            EncodeHintType.CHARACTER_SET to "utf-8",
            EncodeHintType.MARGIN to margin
        )
    }


    private  fun createBitmap(matrix: BitMatrix, codeColor: Int, backgroundColor: Int): Bitmap {
        val width = matrix.width
        val height = matrix.height
        val pixels = IntArray(width * height)

        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                pixels[offset + x] = if (matrix[x, y]) codeColor else backgroundColor
            }
        }

        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
            setPixels(pixels, 0, width, 0, 0, width, height)
        }
    }

    fun scaleBitmap(bitmapToScale: Bitmap?, newWidth: Float, newHeight: Float): Bitmap? {
        if (bitmapToScale == null) return null
        //get the original width and height
        val width = bitmapToScale.width
        val height = bitmapToScale.height
        // create a matrix for the manipulation
        val matrix = Matrix()
        matrix.postScale(newWidth / width, newHeight / height)

        return Bitmap.createBitmap(
            bitmapToScale,
            0,
            0,
            bitmapToScale.width,
            bitmapToScale.height,
            matrix,
            true
        )
    }
    fun Bitmap.addOverlayToCenter(overlayBitmap: Bitmap): Bitmap {
        val bitmap2Width = overlayBitmap.width
        val bitmap2Height = overlayBitmap.height
        val marginLeft = (this.width * 0.5 - bitmap2Width * 0.5).toFloat()
        val marginTop = (this.height * 0.5 - bitmap2Height * 0.5).toFloat()
        val canvas = Canvas(this)
        canvas.drawBitmap(this, Matrix(), null)
        canvas.drawBitmap(overlayBitmap, marginLeft, marginTop, null)
        return this
    }
}