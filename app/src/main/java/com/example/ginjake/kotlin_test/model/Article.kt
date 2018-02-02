package com.example.ginjake.kotlin_test.model

import android.os.Parcel
import android.os.Parcelable
import com.example.ginjake.kotlin_test.mRealm
import io.realm.Realm

import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*


open class Article(
        @PrimaryKey open var id : String = UUID.randomUUID().toString(),
        @Required open var title : String = "",
        open var url :String = "" ,
        open var thumbnail :String = "",
        open var star :Boolean = false
):RealmObject(){

    companion object {

        fun read(database :Realm = mRealm!!) : RealmResults<Article> {
            return database.where(Article::class.java).findAll()
        }

        fun create(database : Realm = mRealm!!, title:String, url:String, thumbnail:String, star:Boolean):Article{
            var random_id:String = ""
            database.executeTransaction {
                random_id = UUID.randomUUID().toString()
                var article = database.createObject(Article::class.java , random_id)
                article.title = title
                article.url = url
                article.thumbnail = thumbnail
                article.star = star
                database.copyToRealm(article)
            }
            return Article(random_id,title,url,thumbnail,star)
        }

        fun update(database :Realm = mRealm!!, id:String, title:String, url:String){
            database.executeTransaction {
                var article = database.where(Article::class.java).equalTo("id",id).findFirst()
                article.title = title
                article.url = url
            }
        }

        fun delete(database :Realm = mRealm!!, id:String){
            database.executeTransaction {
                var database = database.where(Article::class.java).equalTo("id",id).findAll()
                database.deleteFromRealm(0)
            }
        }

        fun delete_all(database :Realm = mRealm!!){
            database.executeTransaction {
                var database = database.where(Article::class.java).findAll()
                database.deleteFromRealm(100)
            }
        }
    }
}