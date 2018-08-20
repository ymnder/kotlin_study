package com.example.ginjake.kotlin_test.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

/**
 * Created by ginjake on 2018/01/25.
 */


open class Version(
        @PrimaryKey open var id: String = UUID.randomUUID().toString(),
        open var version: Double = 0.0
) : RealmObject()