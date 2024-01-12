package com.example.lab5androidsec.data

import android.net.Uri
import androidx.exifinterface.media.ExifInterface

interface TagsRepository {
    suspend fun getTagsData(uri: Uri) : Map<String, String>

    suspend fun getEditableTags(uri: Uri) : Map<String, String>

    suspend fun saveTagsData(uri: Uri, tags: Map<String, String>)

    companion object {
        val TAG_DATETIME = ExifInterface.TAG_DATETIME
        val TAG_MAKE = ExifInterface.TAG_MAKE
        val TAG_MODEL = ExifInterface.TAG_MODEL
        val TAG_LATITUDE = "Latitude"
        val TAG_LONGITUDE = "Longitude"

    }
}