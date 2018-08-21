package com.example.ginjake.kotlin_test.presenter

import com.example.ginjake.kotlin_test.client.UpdateClient
import com.example.ginjake.kotlin_test.repository.CacheCleanerRepository
import com.example.ginjake.kotlin_test.repository.CacheCleanerRepositoryImpl
import com.example.ginjake.kotlin_test.repository.UpdateRepository
import com.example.ginjake.kotlin_test.repository.UpdateRepositoryImpl
import com.example.ginjake.kotlin_test.store.*
import io.realm.Realm
import kotlinx.coroutines.experimental.launch

class ArticlePresenter(

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

    fun deleteAll() {
        launch {
            cacheCleanerRepository.deleteAll()
        }
    }
}