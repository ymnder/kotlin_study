package com.example.ginjake.kotlin_test.store

import com.example.ginjake.kotlin_test.model.Version

interface VersionStore {
    fun update_check(version_num: Double): Boolean
    fun create(version_num: Double): Version
}