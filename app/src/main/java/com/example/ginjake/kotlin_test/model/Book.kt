package com.example.ginjake.kotlin_test.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*
/**
 * Created by ginjake on 2018/01/25.
 */
open class Book(
        @PrimaryKey open var id : String = UUID.randomUUID().toString(),
        @Required open var name : String = "",
        open var price : Long = 0
) : RealmObject()