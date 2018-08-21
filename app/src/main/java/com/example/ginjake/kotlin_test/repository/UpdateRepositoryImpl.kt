package com.example.ginjake.kotlin_test.repository

import android.util.Log
import com.example.ginjake.kotlin_test.client.UpdateClient
import com.example.ginjake.kotlin_test.model.Article
import com.example.ginjake.kotlin_test.store.ArticleStore
import com.example.ginjake.kotlin_test.store.VersionStore
import io.realm.RealmResults

class UpdateRepositoryImpl(
        private val client: UpdateClient,
        private val articleStore: ArticleStore,
        private val versionStore: VersionStore
) : UpdateRepository {
    override fun getVersion() {
        client.getVersion({ version ->
            if (versionStore.update_check(version_num = version)) {
                versionStore.create(version_num = version)
                // TODO アップデート処理
                Log.d("json", "アップデート")
                client.getDataFromTestApi({ results ->
                    for (i in 0..(results.length() - 1)) {
                        val item = results.getJSONObject(i)
                        articleStore.create(
                                title = item["description"].toString(),
                                url = item["link"].toString(),
                                thumbnail = item["url"].toString(),
                                star = false
                        )
                    }

                }, {
                    // TODO エラー処理
                })
            } else {
                Log.d("json", "既に最新")
            }
        }, {
            // TODO エラー処理
        })
    }

    override fun createArticle(title: String, url: String, thumbnail: String, star: Boolean) {
        articleStore.create(title, url, thumbnail, star)
    }

    override fun getAllArticles(): RealmResults<Article> {
        return articleStore.getAllArticles()
    }
}