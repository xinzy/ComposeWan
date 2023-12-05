package com.xinzy.compose.wan.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep

@Keep
data class Chapter(
    var id: Int = 0,
    var name: String = "",
    var order: Int = 0,
    var parentChapterId: Int = 0,
    var visible: Int = 0,
    var userControlSetTop: Boolean = false,
    var children: List<Chapter>? = null
) : Parcelable {

    fun getDisplayName() = name.replace("&amp;", "")

    fun getChildrenNames(): List<String> {
        return mutableListOf<String>().apply {
            children?.forEach { add(it.getDisplayName()) }
        }
    }

    val hasChild get() = children?.isNotEmpty() ?: false

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        name = parcel.readString() ?: ""
        order = parcel.readInt()
        parentChapterId = parcel.readInt()
        visible = parcel.readInt()
        userControlSetTop = parcel.readByte() != 0.toByte()
        children = parcel.createTypedArrayList(CREATOR)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeInt(order)
        parcel.writeInt(parentChapterId)
        parcel.writeInt(visible)
        parcel.writeByte(if (userControlSetTop) 1 else 0)
        parcel.writeTypedList(children)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Chapter> {
        override fun createFromParcel(parcel: Parcel): Chapter {
            return Chapter(parcel)
        }

        override fun newArray(size: Int): Array<Chapter?> {
            return arrayOfNulls(size)
        }
    }
}