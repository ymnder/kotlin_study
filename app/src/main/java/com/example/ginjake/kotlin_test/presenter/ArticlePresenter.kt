package com.example.ginjake.kotlin_test.presenter

import com.example.ginjake.kotlin_test.client.UpdateClient
import com.example.ginjake.kotlin_test.model.Article
import com.example.ginjake.kotlin_test.repository.CacheCleanerRepository
import com.example.ginjake.kotlin_test.repository.CacheCleanerRepositoryImpl
import com.example.ginjake.kotlin_test.repository.UpdateRepository
import com.example.ginjake.kotlin_test.repository.UpdateRepositoryImpl
import com.example.ginjake.kotlin_test.store.*
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.coroutines.experimental.launch

class ArticlePresenter(
        private val view: ArticlePresenter.View
) {
    private val updateRepository: UpdateRepository
    private val cacheCleanerRepository: CacheCleanerRepository

    init {
        //このあたりの処理は他のActivityでも使う可能性があるためこのActivity内でインスタンス化すべきではない
        //APIとStore処理はRepository経由でしか触らせない
        val mRealm = Realm.getDefaultInstance()
        val updateClient = UpdateClient()
        val articleStore: ArticleStore = ArticleStoreImpl(mRealm)
        val versionStore: VersionStore = VersionStoreImpl(mRealm)
        updateRepository = UpdateRepositoryImpl(updateClient, articleStore, versionStore)
        val allStore: AllStore = AllStoreImpl(mRealm)
        cacheCleanerRepository = CacheCleanerRepositoryImpl(allStore)
    }

    fun onCreate() {
        checkUpdateVersion()
    }

    private fun checkUpdateVersion() {
        updateRepository.getVersion()
    }

    private fun createTestData() {
        //テストデータを追加する
        launch {
            updateRepository.createArticle(title = "ことりん",
                    url = "https://www.google.co.jp/search?q=%E3%81%93%E3%81%A8%E3%82%8A%E3%82%93&source=lnms&tbm=isch&sa=X&ved=0ahUKEwiQ1YTaofXYAhURhbwKHYwABZkQ_AUICigB&biw=2133&bih=1054",
                    thumbnail = "http://daybydaypg.com/wp-content/uploads/2017/10/f3759c4c-8e84-74f6-8a4e-b2ec82c7347b.png",
                    star = false)
            updateRepository.createArticle(
                    title = "honoka",
                    url = "http://honokak.osaka/",
                    thumbnail = "http://honokak.osaka/assets/img/honoka.png",
                    star = false
            )
            updateRepository.createArticle(
                    title = "るびぃ",
                    url = "https://ja.wikipedia.org/wiki/Ruby_on_Rails",
                    thumbnail = "https://dyama.org/wp-content/uploads/2016/04/ruby.jpg",
                    star = false
            )
        }
    }

    fun getAll() {
        launch {
            val articles = updateRepository.getAllArticles()
            view.updateArticles(articles)
        }
    }

    fun deleteAll() {
        launch {
            cacheCleanerRepository.deleteAll()
        }
    }

    interface View {
        fun updateArticles(articles: RealmResults<Article>)
    }
}