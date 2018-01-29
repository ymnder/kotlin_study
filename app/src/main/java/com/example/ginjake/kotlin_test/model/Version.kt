package com.example.ginjake.kotlin_test.model

import com.example.ginjake.kotlin_test.mRealm
import io.realm.Realm
import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*
/**
 * Created by ginjake on 2018/01/25.
 */


open class Version(
        @PrimaryKey open var id : String = UUID.randomUUID().toString(),
         open var version : Double = 0.0
) : RealmObject(){

    companion object {


        fun update_check(database : Realm= mRealm!!, version_num: Double ) : Boolean {
            return if (database.where(Version::class.java).greaterThanOrEqualTo("version",version_num).findAll().size == 0)  true else false
        }

        fun create(database :Realm = mRealm!!, version_num:Double ):Version{
            var random_id:String = ""
            database.executeTransaction {
                random_id = UUID.randomUUID().toString()
                var database_object = database.createObject(Version::class.java , random_id)
                database_object.version = version_num
                database.copyToRealm(database_object)
            }
            return Version  (random_id,version_num)
        }
    }
}