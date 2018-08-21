package com.example.ginjake.kotlin_test.store

import io.realm.Realm

class AllStoreImpl(
        private val realm: Realm
): AllStore {
    override fun deleteAll() {
        realm.executeTransaction { Realm.Transaction { realm -> realm.deleteAll() } }
    }
}