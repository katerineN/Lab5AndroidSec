package com.example.lab5androidsec.data

import android.content.Context

interface AppContainer {
    val tagsRepository: TagsRepository
}

class AppDataContainer(private val context: Context) : AppContainer{

    override val tagsRepository: TagsRepository by lazy{
        TagsDataRepository(context)
    }
}