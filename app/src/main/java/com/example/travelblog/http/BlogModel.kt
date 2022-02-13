package com.example.travelblog.http

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat

private val dateFormat = SimpleDateFormat("MMMM dd, yyyy")

data class BlogData(val data: List<Blog>)

@Entity
@Parcelize
data class Blog(
    @PrimaryKey
    val id: String,
    @Embedded
    var author: Author,
    val title: String,
    val date: String,
    val image: String,
    val description: String,
    val views: Int,
    val rating: Float
) :Parcelable{
    fun getImageUrl() = BlogHttpClient.BASE_URL + BlogHttpClient.PATH + image
    fun getDateMills() = dateFormat.parse(date).time
}

@Parcelize
data class Author(
    val name: String,
    val avatar: String
) :Parcelable{
    fun getAvatarUrl() = BlogHttpClient.BASE_URL + BlogHttpClient.PATH + avatar
}