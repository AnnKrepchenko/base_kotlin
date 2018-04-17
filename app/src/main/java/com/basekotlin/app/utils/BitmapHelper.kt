package com.basekotlin.app.utils

import android.content.Context
import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

/**
 * A helper class to conveniently alter Bitmap data.
 *
 * @author Ralf Gehrer <ralf></ralf>@ecotastic.de>
 */
object BitmapHelper {

    /**
     * Converts a Bitmap to a byteArray.
     *
     * @return byteArray
     */
    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    /**
     * Converts a byteArray to a Bitmap object
     *
     * @param byteArray
     * @return Bitmap
     */
    fun byteArrayToBitmap(byteArray: ByteArray?): Bitmap? {
        return if (byteArray == null) {
            null
        } else {
            BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        }
    }

    /**
     * Shrinks and rotates (if necessary) a passed Bitmap.
     *
     * @param bm
     * @param maxLengthOfEdge
     * @param rotateXDegree
     * @return Bitmap
     */
    @JvmOverloads
    fun shrinkBitmap(bm: Bitmap, maxLengthOfEdge: Int, rotateXDegree: Int = 0): Bitmap {
        var bm = bm
        if (maxLengthOfEdge > bm.width && maxLengthOfEdge > bm.height) {
            return bm
        } else {
            // shrink image
            var scale = 1.0.toFloat()
            if (bm.height > bm.width) {
                scale = maxLengthOfEdge.toFloat() / bm.height
            } else {
                scale = maxLengthOfEdge.toFloat() / bm.width
            }
            // CREATE A MATRIX FOR THE MANIPULATION
            var matrix: Matrix? = Matrix()
            // RESIZE THE BIT MAP
            matrix!!.postScale(scale, scale)
            matrix.postRotate(rotateXDegree.toFloat())

            // RECREATE THE NEW BITMAP
            bm = Bitmap.createBitmap(bm, 0, 0, bm.width, bm.height,
                    matrix, false)

            matrix = null
            System.gc()

            return bm
        }
    }

    /**
     * Reads a Bitmap from an Uri.
     *
     * @param context
     * @param selectedImage
     * @return Bitmap
     */
    fun readBitmap(context: Context, selectedImage: Uri): Bitmap? {
        var bm: Bitmap? = null
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.RGB_565
        options.inScaled = false
        //      options.inSampleSize = 3;
        var fileDescriptor: AssetFileDescriptor? = null
        try {
            fileDescriptor = context.contentResolver.openAssetFileDescriptor(selectedImage, "r")
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return null
        } finally {
            try {
                bm = BitmapFactory.decodeFileDescriptor(
                        fileDescriptor!!.fileDescriptor, null, options)
                fileDescriptor.close()
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }

        }
        return bm
    }

    /**
     * Clears all Bitmap data, that is, recycles the Bitmap and
     * triggers the garbage collection.
     *
     * @param bm
     */
    fun clearBitmap(bm: Bitmap) {
        bm.recycle()
        System.gc()
    }


    /**
     * Deletes an image given its Uri and refreshes the gallery thumbnails.
     *
     * @param cameraPicUri
     * @param context
     * @return true if it was deleted successfully, false otherwise.
     */
    fun deleteImageWithUriIfExists(cameraPicUri: Uri?, context: Context): Boolean {
        try {
            if (cameraPicUri != null) {
                val fdelete = File(cameraPicUri.path)
                if (fdelete.exists()) {
                    if (fdelete.delete()) {
                        refreshGalleryImages(context, fdelete)
                        return true
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    /**
     * Forces the Android gallery to  refresh its thumbnail images.
     *
     * @param context
     * @param fdelete
     */
    private fun refreshGalleryImages(context: Context, fdelete: File) {
        try {
            context.sendBroadcast(Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())))
        } catch (e1: Exception) {
            e1.printStackTrace()
            try {
                val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                val contentUri = Uri.fromFile(fdelete)
                mediaScanIntent.data = contentUri
                context.sendBroadcast(mediaScanIntent)
            } catch (e2: Exception) {
                e2.printStackTrace()
            }

        }

    }
}
/**
 * Shrinks and a passed Bitmap.
 *
 * @param bm
 * @param maxLengthOfEdge
 * @return Bitmap
 */