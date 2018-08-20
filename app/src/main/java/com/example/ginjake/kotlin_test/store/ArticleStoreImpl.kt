package com.example.ginjake.kotlin_test.store

import com.example.ginjake.kotlin_test.model.Article
import io.realm.Realm
import io.realm.RealmResults
import java.util.*

class ArticleStoreImpl(
        private val realm: Realm
): ArticleStore{
    override fun read(): RealmResults<Article> {
        return realm.where(Article::class.java).findAll()
    }

    override fun create(title: String, url: String, thumbnail: String, star: Boolean): Article {
        var random_id:String = ""
        realm.executeTransaction {
            random_id = UUID.randomUUID().toString()
            val article = realm.createObject(Article::class.java , random_id)
            article.title = title
            article.url = url
            article.thumbnail = thumbnail
            article.star = star
            realm.copyToRealm(article)
        }
        return Article(random_id,title,url,thumbnail,star)
    }

    override fun update(id: String, title: String, url: String) {
        realm.executeTransaction {
            val article = realm.where(Article::class.java).equalTo("id",id).findFirst()
            article.title = title
            article.url = url
        }
    }

    override fun delete(id: String) {
        realm.executeTransaction {
            val database = realm.where(Article::class.java).equalTo("id",id).findAll()
            database.deleteFromRealm(0)
        }
    }

    override fun delete_all() {
        realm.executeTransaction {
            val database = realm.where(Article::class.java).findAll()
            database.deleteFromRealm(100)
        }
    }

}