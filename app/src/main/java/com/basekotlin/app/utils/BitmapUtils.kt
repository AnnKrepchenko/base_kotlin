package com.basekotlin.app.utils


import android.graphics.Bitmap
import android.graphics.BitmapFactory

import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object BitmapUtils {

    @Throws(IOException::class)
    fun squareBitmapFile(file: File): File {
        val bmOptions = BitmapFactory.Options()
        bmOptions.inSampleSize = 4
        var bitmap = BitmapFactory.decodeFile(file.absolutePath, bmOptions)

        bitmap = cropToSquare(bitmap)

        val fOut = FileOutputStream(file, false)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut)

        fOut.flush()
        fOut.close()

        return file
    }

    fun cropToSquare(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val newWidth = if (height > width) width else height
        val newHeight = if (height > width) height - (height - width) else height
        var cropW = (width - height) / 2
        cropW = if (cropW < 0) 0 else cropW
        var cropH = (height - width) / 2
        cropH = if (cropH < 0) 0 else cropH

        return Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight)
    }
}
