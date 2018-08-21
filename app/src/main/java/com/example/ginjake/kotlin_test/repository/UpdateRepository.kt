package com.example.ginjake.kotlin_test.repository

import com.example.ginjake.kotlin_test.model.Article
import io.realm.RealmResults

interface UpdateRepository {
    fun getVersion()
    fun createArticle(title: String, url: String, thumbnail: String, star: Boolean)
    fun getAllArticles(): RealmResults<Article>
}