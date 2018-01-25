package com.example.ginjake.kotlin_test.model

import com.example.ginjake.kotlin_test.model.User

import android.os.Parcel
import android.os.Parcelable

open class Article(val id: String,
                   val title: String,
                   val url: String,
                   val user: User) : Parcelable {

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Article> = object : Parcelable.Creator<Article> {
            override fun createFromParcel(source: Parcel): Article = source.run {
                Article(readString(), readString(), readString(), readParcelable(User::class.java.classLoader))
            }

            override fun newArray(size: Int): Array<Article?> = arrayOfNulls(size)
        }
    }

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.run {
            writeString(id)
            writeString(title)
            writeString(url)
            writeParcelable(user, flags)
        }
    }
}