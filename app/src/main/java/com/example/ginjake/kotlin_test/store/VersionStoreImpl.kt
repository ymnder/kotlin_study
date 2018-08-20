package com.example.ginjake.kotlin_test.store

import com.example.ginjake.kotlin_test.model.Version
import io.realm.Realm
import java.util.*

class VersionStoreImpl(
        private val realm: Realm
): VersionStore {
    override fun update_check(version_num: Double): Boolean {
        return realm.where(Version::class.java).greaterThanOrEqualTo("version", version_num).findAll().size == 0
    }

    override fun create(version_num: Double): Version {
        val random_id = UUID.randomUUID().toString()
        realm.executeTransaction {
            val database_object = realm.createObject(Version::class.java, random_id)
            database_object.version = version_num
            realm.copyToRealm(database_object)
        }
        return Version(random_id, version_num)
    }
}