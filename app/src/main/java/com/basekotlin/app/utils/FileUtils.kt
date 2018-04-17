package com.basekotlin.app.utils

import android.content.Context
import android.media.MediaMetadataRetriever
import android.util.Log
import java.io.*
import java.util.*


object FileUtils {

    fun loadAssetTextAsString(context: Context, name: String): String? {
        var bufferedReader: BufferedReader? = null
        try {
            val buf = StringBuilder()
            val inputStream = context.assets.open(name)
            bufferedReader = BufferedReader(InputStreamReader(inputStream))
            var str: String?
            var isFirst = true
            str = bufferedReader.readLine()
            while (str != null) {
                if (isFirst)
                    isFirst = false
                else
                    buf.append('\n')
                buf.append(str)
                str = bufferedReader.readLine()
            }
            return buf.toString()
        } catch (e: IOException) {
            Log.e("loadAssetTextAsString", "Error opening asset $name")
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close()
                } catch (e: IOException) {
                    Log.e("loadAssetTextAsString", "Error closing asset $name")
                }

            }
        }

        return null
    }

    @Throws(IOException::class)
    fun readFile(file: File): ByteArray {
        // Open file
        val f = RandomAccessFile(file, "r")
        try {
            // Get and check length
            val longlength = f.length()
            val length = longlength.toInt()
            if (length.toLong() != longlength)
                throw IOException("File size >= 2 GB")
            // Read file and return data
            val data = ByteArray(length)
            f.readFully(data)
            return data
        } finally {
            f.close()
        }
    }

    fun deleteRecordFolder(context: Context, recordId: String): Boolean {
        return deleteRecursive(File(getRootFolder(context, recordId)))
    }

    private fun deleteRecursive(fileOrDirectory: File): Boolean {
        if (fileOrDirectory.isDirectory) {
            for (child in fileOrDirectory.listFiles()) {
                deleteRecursive(child)
            }
        }
        return fileOrDirectory.delete()
    }

    private fun getRootFolder(context: Context, srVideoId: String): String {
        val dir = context.getExternalFilesDir(null)
        val path = StringBuilder()
                .append(dir!!.absolutePath).append("/")
                .append(srVideoId)
                .toString()
        val root = File(path)
        if (!root.exists()) {
            root.mkdirs()
        }
        return path
    }

    private fun getAudioFolderPath(context: Context, srVideoId: String): String {
        val path = StringBuilder()
                .append(getRootFolder(context, srVideoId)).append("/")
                .append("audio")
                .toString()
        val root = File(path)
        if (!root.exists()) {
            root.mkdirs()
        }
        return path
    }

    private fun getVideoFolderPath(context: Context, srVideoId: String): String {
        val path = StringBuilder()
                .append(getRootFolder(context, srVideoId)).append("/")
                .append("video")
                .toString()
        val root = File(path)
        if (!root.exists()) {
            root.mkdirs()
        }
        return path
    }

    fun getImageFolderPath(context: Context, srVideoId: String): String {
        val path = StringBuilder()
                .append(getRootFolder(context, srVideoId)).append("/")
                .append("image")
                .toString()
        val root = File(path)
        if (!root.exists()) {
            root.mkdirs()
        }
        return path
    }

    fun getAudioFilePath(context: Context, srVideoId: String, name: String): String {
        return StringBuilder()
                .append(getAudioFolderPath(context, srVideoId)).append("/")
                .append(name).append(".aac")
                .toString()
    }

    fun getAudioFilePath(context: Context, srVideoId: String): String {
        return StringBuilder()
                .append(getAudioFolderPath(context, srVideoId)).append("/")
                .append(System.currentTimeMillis()).append(".mp4")
                .toString()
    }

    fun getAudiosByRecordId(context: Context, srVideoId: String): List<File> {
        val files = LinkedList<File>()
        val path = getAudioFolderPath(context, srVideoId)
        val dir = File(path)
        if (dir.exists()) {
            files.addAll(Arrays.asList(*dir.listFiles()))
        }
        return files
    }

    fun getVideoFilePath(context: Context, srVideoId: String, name: String): String {
        return StringBuilder()
                .append(getVideoFolderPath(context, srVideoId)).append("/")
                .append(name).append(".mp4")
                .toString()
    }

    fun getVideoFilePath(context: Context, srVideoId: String): String {
        return StringBuilder()
                .append(getVideoFolderPath(context, srVideoId)).append("/")
                .append(System.currentTimeMillis()).append(".mp4")
                .toString()
    }

    fun getImageFilePath(context: Context, srVideoId: String): String {
        return StringBuilder()
                .append(getImageFolderPath(context, srVideoId)).append("/")
                .append(System.currentTimeMillis()).append(".jpg")
                .toString()
    }

    fun getVideosByRecordId(context: Context, srVideoId: String): List<File> {
        val files = LinkedList<File>()
        val path = getVideoFolderPath(context, srVideoId)
        val dir = File(path)
        if (dir.exists()) {
            files.addAll(Arrays.asList(*dir.listFiles()))
        }
        return files
    }

    fun getImagesByRecordId(context: Context, srVideoId: String): List<File> {
        val files = LinkedList<File>()
        val path = getImageFolderPath(context, srVideoId)
        val dir = File(path)
        if (dir.exists()) {
            files.addAll(Arrays.asList(*dir.listFiles()))
        }
        return files
    }

    fun readMetaData(file: File) {

        if (file.exists()) {
            Log.i("readMetaData", file.name + " file Exist")

            //Added in API level 10
            val retriever = MediaMetadataRetriever()
            try {
                retriever.setDataSource(file.absolutePath)
                for (i in 0..999) {
                    //only Metadata != null is printed!
                    if (retriever.extractMetadata(i) != null) {
                        Log.i("readMetaData", "Metadata + " + i + " :: " + retriever.extractMetadata(i))
                    }

                }
            } catch (e: Exception) {
                Log.e("readMetaData", "Exception : " + e.message)
            }

        } else {
            Log.e("readMetaData", ".mp4 file doesn´t exist.")
        }

    }


    //    JSON

    fun getJsonFolder(context: Context, srVideoId: String): String {
        val path = StringBuilder()
                .append(getRootFolder(context, srVideoId)).append("/")
                .append("json")
                .toString()
        val root = File(path)
        if (!root.exists()) {
            root.mkdirs()
        }
        return path
    }

    fun getJsonFilePath(context: Context, srVideoId: String, intitialStatePos: Int): String {
        val builder = StringBuilder()
                .append(getJsonFolder(context, srVideoId)).append("/")
                .append(srVideoId).append("-")
                .append(intitialStatePos)
                .append(".json")
        return builder.toString()
    }

    fun getJsonFilePath(context: Context, srVideoId: String, intitialStatePos: Int, keyFramePos: Int): String {
        val builder = StringBuilder()
                .append(getJsonFolder(context, srVideoId)).append("/")
                .append(srVideoId).append("-")
                .append(intitialStatePos).append("-")
                .append(keyFramePos)
                .append(".json")
        return builder.toString()
    }

    //    ZIP
    fun getZipFilePath(context: Context, srVideoId: String): String {
        val builder = StringBuilder()
                .append(getRootFolder(context, srVideoId)).append("/")
                .append("result")
                .append(".zip")
        return builder.toString()
    }

}
