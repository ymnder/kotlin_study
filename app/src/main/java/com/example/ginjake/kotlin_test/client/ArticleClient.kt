package com.example.ginjake.kotlin_test.client

/**
 * Created by ginjake on 2018/01/19.
 */
import com.example.ginjake.kotlin_test.model.Article
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

interface ArticleClient {

    @GET("/api/v2/items")
    fun search(@Query("query") query: String): Observable<List<Article>>
}