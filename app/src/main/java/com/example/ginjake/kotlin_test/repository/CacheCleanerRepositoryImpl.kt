package com.example.ginjake.kotlin_test.repository

import com.example.ginjake.kotlin_test.store.AllStore

class CacheCleanerRepositoryImpl(
        private val allStore: AllStore
): CacheCleanerRepository {

    /**
     * DBを一括削除する
     *
     * 本来はDBを一括操作するUseCaseはないはずなので、削除したいStoreごとにdeleteを実装するべき
     */
    override fun deleteAll() {
        allStore.deleteAll()
    }
}