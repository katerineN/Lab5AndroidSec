package com.example.lab5androidsec.data

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.exifinterface.media.ExifInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TagsDataRepository(val applicationContext: Context) : TagsRepository {
    override suspend fun getEditableTags(uri: Uri): Map<String, String> {
        val tags = arrayOf(
            ExifInterface.TAG_DATETIME,
            ExifInterface.TAG_MAKE,
            ExifInterface.TAG_MODEL)
        val input = applicationContext.contentResolver.openInputStream(uri)
        val exifInterface = ExifInterface(input!!)
        val exifData = mutableMapOf<String, String>()
        exifInterface.latLong?.let {
            exifData["Latitude"] = it[0].toString()
            exifData["Longitude"] = it[1].toString()
        }
        tags.forEach { tag ->
            exifInterface.getAttribute(tag)?.let {
                exifData[tag] = it
            }
        }
        withContext(Dispatchers.IO) {
            input.close()
        }
        return exifData
    }

    override suspend fun getTagsData(uri: Uri): Map<String, String> {
        val input = applicationContext.contentResolver.openInputStream(uri)
        val exifInterface = ExifInterface(input!!)
        val exifData = mutableMapOf<String, String>()
        exifInterface.latLong?.let {
            exifData["Latitude"] = it[0].toString()
            exifData["Longitude"] = it[1].toString()
        }
        EXIF_TAGS.forEach { tag ->
            exifInterface.getAttribute(tag)?.let {
                exifData[tag] = it
            }
        }
        withContext(Dispatchers.IO) {
            input.close()
        }
        return exifData
    }

    override suspend fun saveTagsData(uri: Uri, tags: Map<String, String>) {
        val mediaUri = MediaStore.getMediaUri(applicationContext, uri)

        val tempInputOutput = applicationContext.contentResolver.openFileDescriptor(mediaUri!!, "rw", null)!!

        tempInputOutput.use {
            val exifInterface = ExifInterface(it.fileDescriptor)
            exifInterface.setAttribute(TagsRepository.TAG_MAKE, tags[TagsRepository.TAG_MAKE])
            exifInterface.setAttribute(TagsRepository.TAG_MODEL, tags[TagsRepository.TAG_MODEL])
            exifInterface.setAttribute(TagsRepository.TAG_DATETIME, tags[TagsRepository.TAG_DATETIME])
            tags[TagsRepository.TAG_LATITUDE]?.toDoubleOrNull()?.let { lat ->
                tags[TagsRepository.TAG_LONGITUDE]?.toDoubleOrNull()?.let { long ->
                    exifInterface.setLatLong(lat, long)
                }
            }

            exifInterface.saveAttributes()
        }
    }

    companion object {
        private val EXIF_TAGS = arrayOf(
            ExifInterface.TAG_DATETIME,
            ExifInterface.TAG_MAKE,
            ExifInterface.TAG_MODEL,
            ExifInterface.TAG_IMAGE_LENGTH,
            ExifInterface.TAG_IMAGE_WIDTH,
            ExifInterface.TAG_GPS_LATITUDE,
            ExifInterface.TAG_GPS_LONGITUDE
        )
    }
}