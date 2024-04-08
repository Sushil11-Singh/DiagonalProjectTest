package com.diagnal.diagnalprject.repository

import android.content.Context
import com.diagnal.diagnalprject.model.ContentListDTO
import com.google.gson.Gson
import java.io.IOException

class ContentListRepo(private val context: Context) {
    fun loadDataFromJson(pageNumber: Int): ContentListDTO? {
        try {
            // Assuming your JSON files are named like "CONTENTLISTINGPAGE-PAGE11.json".
            val fileName = "CONTENTLISTINGPAGE-PAGE$pageNumber.json"
            val json: String = context.assets.open(fileName).bufferedReader().use {
                it.readText()
            }
            return Gson().fromJson(json, ContentListDTO::class.java)
        } catch (ioException: IOException) {
            // Handle IO errors (e.g., file not found, unable to read file)
            ioException.printStackTrace()
        } catch (exception: Exception) {
            // Handle other exceptions (e.g., JSON parsing error)
            exception.printStackTrace()
        }
        return null
    }
}


