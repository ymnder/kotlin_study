package com.example.ginjake.kotlin_test.client

import com.github.kittinunf.fuel.android.core.Json
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import org.json.JSONArray
import org.json.JSONObject


/**
 * Created by ginjake on 2018/01/29.
 */
class UpdateClient {

    companion object {
        private const val VERSION = "http://www.ginjake.net/kotlinApi/version.php"
        private const val CREATE_JSON = "http://www.ginjake.net/kotlinApi/create_json.php"
    }

    fun getVersion(success: (version: Double) -> Unit, error: (error: Result<Json, FuelError>) -> Unit) {
        VERSION.httpGet().responseJson { request, response, result ->
            when (result) {
                // ステータスコード 2xx
                is Result.Success -> {
                    val json = result.value.obj()
                    val results = json.get("results") as JSONArray
                    val version: Double = ((results[0] as JSONObject)["version"] as String).toDouble()
                    success(version)
                }
                // ステータスコード 2xx以外
                is Result.Failure -> {
                    error(result)
                }
            }

        }
    }

    fun getDataFromTestApi(success: (results: JSONArray) -> Unit, error: (error: Result<Json, FuelError>) -> Unit) {
        CREATE_JSON.httpGet().responseJson { request, response, result ->
            when (result) {
                // ステータスコード 2xx
                is Result.Success -> {
                    //jsonはこうやって処理する API自体は前に遊びで作った、pinterestからスクレイピングした結果を返すしょーもないやつ
                    val json = result.value.obj()
                    val results = json.get("results") as JSONArray
                    success(results)
                }
                // ステータスコード 2xx以外
                is Result.Failure -> {
                    error(result)
                }
            }

        }
    }


}