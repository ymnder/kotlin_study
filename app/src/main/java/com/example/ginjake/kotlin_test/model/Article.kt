package com.example.ginjake.kotlin_test.model

import com.example.ginjake.kotlin_test.model.User

import android.os.Parcel
import android.os.Parcelable
import io.realm.Realm

import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*
open class Articlehhh(
        @PrimaryKey open var id : String = UUID.randomUUID().toString(),
        @Required open var title : String = "",
        open var url :String = ""
        //private val user: User
) : RealmObject()



open class Article(
        @PrimaryKey open var id : String = UUID.randomUUID().toString(),
        @Required open var title : String = "",
        open var url :String = ""
) : Parcelable,RealmObject(){

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Article> = object : Parcelable.Creator<Article> {
            override fun createFromParcel(source: Parcel): Article = source.run {
                Article(readString(), readString(), readString())
            }

            override fun newArray(size: Int): Array<Article?> = arrayOfNulls(size)
        }


        fun read(database : Realm) : RealmResults<Article> {
            return database.where(Article::class.java).findAll()
        }

        fun create(database : Realm, title:String, url:String = "hoge"):Article{
            var random_id:String = ""
            database.executeTransaction {
                random_id = UUID.randomUUID().toString()
                var article = database.createObject(Article::class.java , random_id)
                article.title = title
                article.url = url
                database.copyToRealm(article)
            }
            return Article(random_id,title,url)
        }
        fun update(database :Realm, id:String, title:String, url:String){
            database.executeTransaction {
                var article = database.where(Article::class.java).equalTo("id",id).findFirst()
                article!!.title = title
                article!!.url = url
            }
        }
        fun delete(database : Realm, id:String){
            database.executeTransaction {
                var database = database.where(Article::class.java).equalTo("id",id).findAll()
                database.deleteFromRealm(0)
            }
        }
        fun delete_all(database : Realm){
            database.executeTransaction {
                var database = database.where(Article::class.java).findAll()
                database.deleteFromRealm(100)
            }
        }
    }

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.run {
            writeString(id)
            writeString(title)
            writeString(url)
           // writeParcelable(user, flags)
        }
    }


}