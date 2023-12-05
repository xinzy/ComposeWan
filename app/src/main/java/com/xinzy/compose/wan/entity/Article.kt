package com.xinzy.compose.wan.entity

import android.os.Parcel
import android.os.Parcelable
import android.text.Html
import android.text.TextUtils
import com.google.gson.annotations.SerializedName

data class Article(
    var id: Int = 0,
    var author: String = "",
    var chapterId: Int = 0,
    var chapterName: String = "",

    var desc: String = "",
    var link: String = "",
    var niceDate: String = "",

    var title: String = "",
    var projectLink: String? = null,

    var superChapterId: Int = 0,
    var superChapterName: String = "",
    @SerializedName("envelopePic")
    var cover: String? = null,

    var courseId: Int = 0,
    var collect: Boolean = false,
    var fresh: Boolean = false,
    var publishTime: Long = 0,
    var type: Int = 0,
    var userId: Int = 0,
    var visible: Int = 0,
    var zan: Int = 0,

    var tags: List<Tag>? = null
) : Parcelable {

    val displayAuthor: String get() = if (TextUtils.isEmpty(author)) "匿名" else author

    val displayTitle: String
        get() = Html.fromHtml(title, 0).toString()

    fun isTop() = type == 1

    fun getCategory() = "$superChapterName / $chapterName"

    fun getTagTexts(): List<String> {
        return mutableListOf<String>().apply {
            tags?.let { it.forEach { item -> add(item.name)} }
        }
    }

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        author = parcel.readString() ?: ""
        chapterId = parcel.readInt()
        chapterName = parcel.readString() ?: ""
        desc = parcel.readString() ?: ""
        link = parcel.readString() ?: ""
        niceDate = parcel.readString() ?: ""
        title = parcel.readString() ?: ""
        projectLink = parcel.readString()
        superChapterId = parcel.readInt()
        superChapterName = parcel.readString() ?: ""
        cover = parcel.readString()
        courseId = parcel.readInt()
        collect = parcel.readByte() != 0.toByte()
        fresh = parcel.readByte() != 0.toByte()
        publishTime = parcel.readLong()
        type = parcel.readInt()
        userId = parcel.readInt()
        visible = parcel.readInt()
        zan = parcel.readInt()
    }

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