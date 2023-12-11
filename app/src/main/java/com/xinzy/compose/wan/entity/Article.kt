package com.xinzy.compose.wan.entity

import android.os.Parcel
import android.os.Parcelable
import android.text.Html
import android.text.TextUtils
import com.google.gson.annotations.SerializedName

data class Article(
    val id: Int = 0,
    val author: String = "",
    val chapterId: Int = 0,
    val chapterName: String = "",

    val desc: String = "",
    val link: String = "",
    val niceDate: String = "",

    val title: String = "",
    val projectLink: String? = null,

    val superChapterId: Int = 0,
    val superChapterName: String = "",
    @SerializedName("envelopePic")
    val cover: String = "",

    val courseId: Int = 0,
    val collect: Boolean = false,
    val fresh: Boolean = false,
    val publishTime: Long = 0,
    val type: Int = 0,
    val userId: Int = 0,
    val visible: Int = 0,
    val zan: Int = 0,
    val shareUser: String = "",

    val tags: List<Tag> = emptyList()
) : Parcelable {

    val displayAuthor: String get() = if (TextUtils.isEmpty(author)) shareUser else author

    val displayTitle: String
        get() = Html.fromHtml(title, 0).toString()

    val hasAuthor: Boolean get() = !TextUtils.isEmpty(author)

    fun isTop() = type == 1

    fun getCategory() = "$superChapterName / $chapterName"

    constructor(parcel: Parcel) : this(
        id = parcel.readInt(),
        author = parcel.readString() ?: "",
        chapterId = parcel.readInt(),
        chapterName = parcel.readString() ?: "",
        desc = parcel.readString() ?: "",
        link = parcel.readString() ?: "",
        niceDate = parcel.readString() ?: "",
        title = parcel.readString() ?: "",
        projectLink = parcel.readString(),
        superChapterId = parcel.readInt(),
        superChapterName = parcel.readString() ?: "",
        cover = parcel.readString() ?: "",
        courseId = parcel.readInt(),
        collect = parcel.readByte() != 0.toByte(),
        fresh = parcel.readByte() != 0.toByte(),
        publishTime = parcel.readLong(),
        type = parcel.readInt(),
        userId = parcel.readInt(),
        visible = parcel.readInt(),
        zan = parcel.readInt(),
        shareUser = parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(author)
        parcel.writeInt(chapterId)
        parcel.writeString(chapterName)
        parcel.writeString(desc)
        parcel.writeString(link)
        parcel.writeString(niceDate)
        parcel.writeString(title)
        parcel.writeString(projectLink)
        parcel.writeInt(superChapterId)
        parcel.writeString(superChapterName)
        parcel.writeString(cover)
        parcel.writeInt(courseId)
        parcel.writeByte(if (collect) 1 else 0)
        parcel.writeByte(if (fresh) 1 else 0)
        parcel.writeLong(publishTime)
        parcel.writeInt(type)
        parcel.writeInt(userId)
        parcel.writeInt(visible)
        parcel.writeInt(zan)
        parcel.writeString(shareUser)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Article> {
        override fun createFromParcel(parcel: Parcel): Article {
            return Article(parcel)
        }

        override fun newArray(size: Int): Array<Article?> {
            return arrayOfNulls(size)
        }
    }
}