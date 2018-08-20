package com.example.ginjake.kotlin_test.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*


open class Article(
        @PrimaryKey open var id : String = UUID.randomUUID().toString(),
        @Required open var title : String = "",
        open var url :String = "" ,
        open var thumbnail :String = "",
        open var star :Boolean = false
):RealmObject()