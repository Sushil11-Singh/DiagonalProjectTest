package com.diagnal.diagnalprject.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class ContentListDTO(
    val page: Page
)

data class Page(
    @SerializedName("content-items")
    val contentItems: ContentItems,
    @SerializedName("page-num")
    val pageNum: String,
    @SerializedName("page-size")
    val pageSize: String,
    val title: String,
    @SerializedName("total-content-items")
    val totalContentItems: String
)

data class ContentItems(
    val content: List<Content>
)

data class Content(
    val name: String,
    @SerializedName("poster-image")
    val posterImage: String
)