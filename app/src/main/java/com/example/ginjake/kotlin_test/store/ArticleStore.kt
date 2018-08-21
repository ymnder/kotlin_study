package com.example.ginjake.kotlin_test.store

import com.example.ginjake.kotlin_test.model.Article
import io.realm.RealmResults

interface ArticleStore {
    fun getAllArticles(): RealmResults<Article>
    fun create(title: String, url: String, thumbnail: String, star: Boolean): Article
    fun update(id: String, title: String, url: String)
    fun delete(id: String)
    fun delete_all()
}