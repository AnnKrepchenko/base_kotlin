package com.basekotlin.app.utils

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.util.Log


import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

import android.content.Intent.FLAG_GRANT_PREFIX_URI_PERMISSION
import android.os.Environment.getExternalStorageDirectory
import com.basekotlin.app.BuildConfig

object ImagePicker {

    val PICK_IMAGE_FROM_GALLERY_REQUEST = 101
    val CREATE_IMAGE_REQUEST = 102
    private val CONTENT_GOOGLE_PHOTO = "content://com.google.android.apps.photos.content"
    private val MAX_IMAGE_SIZE = 3000
    private var mImageFileLocation = ""
    private var photoFile: File? = null


    val galleryIntent: Intent
        get() {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryIntent.type = "image/*"
            return galleryIntent
        }

    fun getCameraIntent(context: Context): Intent {
        val cameraIntent = Intent()
        cameraIntent.action = MediaStore.ACTION_IMAGE_CAPTURE
        try {
            photoFile = createImageFile()
        } catch (e: Exception) {
            photoFile = null
            Log.e("TAG", "getCameraIntent: ")
        }

        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        cameraIntent.addFlags(FLAG_GRANT_PREFIX_URI_PERMISSION)
        cameraIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        context.revokeUriPermission(getCaptureImageOutputUri(context), Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.grantUriPermission(getExternalStorageDirectory().toString(), getCaptureImageOutputUri(context), Intent.FLAG_GRANT_PREFIX_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        context.grantUriPermission(BuildConfig.APPLICATION_ID + ".provider", getCaptureImageOutputUri(context), Intent.FLAG_GRANT_PREFIX_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", photoFile))
        return cameraIntent
    }

    private fun getCaptureImageOutputUri(context: Context): Uri? {
        photoFile = File(mImageFileLocation).absoluteFile
        return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", photoFile!!.absoluteFile)
    }

    private fun createImageFile(): File? {
        val timeStamp = System.currentTimeMillis()
        val imageFileName = "IMAGE_" + timeStamp + "_"
        val storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        var image: File? = null
        try {
            image = File.createTempFile(imageFileName, ".jpg", storageDirectory)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        if (image != null && mImageFileLocation.length == 0) {
            mImageFileLocation = image.absolutePath
        }
        return image
    }

    fun getResizedFileFromResult(context: Context, intent: Intent, isGallery: Boolean): File? {
        var file: File? = null
        try {
            file = getTempFile(context)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return if (file != null) getResizedFileFromResult(context, intent, isGallery, file) else null
    }

    fun getResizedFileFromResult(context: Context, intent: Intent, isGallery: Boolean, path: File): File? {
        var imageFile: File? = null
        var bitmap: Bitmap? = null
        var stream: FileOutputStream? = null
        val picturePath: String?
        try {
            //Get Image from provider or data(Intent)
            imageFile = path
            val picUri = ImagePicker.getPickImageResultUri(context, intent)

            if (picUri != null && picUri.toString().startsWith(CONTENT_GOOGLE_PHOTO)) {
                var `is`: InputStream? = null
                try {
                    `is` = context.contentResolver.openInputStream(picUri)
                    if (`is` != null) {
                        /* decrease image size */
                        val options = BitmapFactory.Options()
                        val absolutePath = getAbsolutePathGooglePhoto(context, picUri)
                        if (absolutePath != null) {
                            if (!isExtensionAllowed(absolutePath)) {
                                return null
                            }
                        }
                        if (absolutePath != null) {
                            options.inSampleSize = calculateSampleSize(absolutePath, MAX_IMAGE_SIZE, MAX_IMAGE_SIZE)
                        }
                        /* - */
                        bitmap = BitmapFactory.decodeStream(`is`, null, options)
                        /* rotate image */
                        if (absolutePath != null) {
                            bitmap = rotateImageIfRequired(bitmap, absolutePath)
                        }
                        /* - */
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    if (`is` != null) {
                        `is`.close()
                    }
                }
            } else {
                picturePath = getImagePath(context, isGallery, picUri)
                if (picturePath != null) {
                    if (!isExtensionAllowed(picturePath)) {
                        return null
                    }
                    //                    bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), picUri);
                    bitmap = decodeSampledBitmapFromFile(picturePath, MAX_IMAGE_SIZE, MAX_IMAGE_SIZE)
                    bitmap = rotateImageIfRequired(bitmap, picturePath)
                } else {
                    bitmap = BitmapFactory.decodeFile(mImageFileLocation)
                }
            }
            if (bitmap == null)
                return null

            //Write image to file
            stream = FileOutputStream(imageFile)
            bitmap = getResizedBitmap(bitmap, MAX_IMAGE_SIZE)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            bitmap.recycle()

        } catch (e: Exception) {
            Log.e(ImagePicker::class.java.simpleName, "getResizedFileFromResult: " + e.localizedMessage)
        } finally {
            if (stream != null) {
                try {
                    stream.flush()
                    stream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        photoFile = null
        mImageFileLocation = ""
        return imageFile
    }

    private fun isExtensionAllowed(path: String): Boolean {
        return path.toLowerCase().endsWith(".jpeg") || path.toLowerCase().endsWith(".png") || path.toLowerCase().endsWith(".jpg")
    }

    fun getAbsolutePathGooglePhoto(context: Context, uri: Uri): String? {
        var res: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            res = cursor.getString(column_index)
        }
        cursor?.close()
        return res
    }

    fun decodeSampledBitmapFromFile(res: String, reqWidth: Int, reqHeight: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(res, options)
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(res, options)
    }

    private fun calculateSampleSize(absolutePath: String, reqWidth: Int, reqHeight: Int): Int {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(absolutePath, options)
        return calculateInSampleSize(options, reqWidth, reqHeight)
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options,
                                      reqWidth: Int,
                                      reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            while (height / inSampleSize >= reqHeight && width / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    fun getResizedBitmap(bitmap: Bitmap, maxSize: Int): Bitmap {
        var width = bitmap.width
        var height = bitmap.height
        if (width <= maxSize && height <= maxSize) {
            return bitmap
        }
        val ratio = width.toFloat() / height.toFloat()
        if (ratio > 1) {
            width = maxSize
            height = (width / ratio).toInt()
        } else {
            height = maxSize
            width = (height * ratio).toInt()
        }
        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }

    private fun getImagePath(context: Context, isGallery: Boolean, picUri: Uri?): String? {
        var picture: String? = null
        if (isGallery) {
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = context.contentResolver.query(picUri!!,
                    filePathColumn, null, null, null)
            if (cursor != null) {
                cursor.moveToFirst()
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                picture = cursor.getString(columnIndex)
                cursor.close()
            } else {
                try {
                    picture = picUri.path
                } catch (e: Exception) {
                    picture = null
                }

            }
        } else {
            try {
                picture = picUri!!.path
            } catch (e: Exception) {
                picture = null
            }

        }
        return picture
    }

    private fun getPickImageResultUri(context: Context, data: Intent?): Uri? {
        var isCamera = true
        if (data != null) {
            val action = data.action
            isCamera = action != null && action == MediaStore.ACTION_IMAGE_CAPTURE
        }

        return if (isCamera) getCaptureImageOutputUri(context) else data!!.data
    }

    @Throws(IOException::class)
    fun getTempFile(context: Context): File {
        val outputDir = context.cacheDir // context being the Activity pointer
        return File.createTempFile("temp" + System.currentTimeMillis(), ".jpg", outputDir)
    }

    private fun rotateImageIfRequired(img: Bitmap?, selectedImage: String): Bitmap? {
        var ei: ExifInterface? = null
        try {
            ei = ExifInterface(selectedImage)
        } catch (e: IOException) {
            e.printStackTrace()
            return img
        }

        val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> return rotateImage(img, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> return rotateImage(img, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> return rotateImage(img, 270)
            else -> return img
        }
    }

    private fun rotateImage(img: Bitmap?, degree: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedImg = Bitmap.createBitmap(img!!, 0, 0, img.width, img.height, matrix, true)
        img.recycle()
        return rotatedImg
    }


}