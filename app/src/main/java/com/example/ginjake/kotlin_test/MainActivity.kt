package com.example.ginjake.kotlin_test

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import com.example.ginjake.kotlin_test.client.ArticleClient
import com.example.ginjake.kotlin_test.model.Article
import com.example.ginjake.kotlin_test.model.User
import com.example.ginjake.kotlin_test.view.ArticleView
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.trello.rxlifecycle.kotlin.bindToLifecycle
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import android.support.annotation.NonNull
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.LinearLayout
import com.example.ginjake.kotlin_test.R.layout.activity_sub


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var cardCount = 0

    private val drawer: DrawerLayout by lazy{
        findViewById<View>(R.id.drawer_layout) as DrawerLayout
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* メニュー 闇の魔術で動いてる*/
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        //setSupportActionBar(toolbar)

        //drawer = findViewById(R.id.drawer_layout)
        var toggle = object : ActionBarDrawerToggle(
                this, /* host Activity */
                drawer, /* DrawerLayout object */
                toolbar, /* nav drawer icon to replace 'Up' caret */
                R.string.app_name, /* "open drawer" description */
                R.string.app_name  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state.  */
            override fun onDrawerClosed(view: View?) {
                setTitle("開く")
            }

            /** Called when a drawer has settled in a completely open state.  */
            override fun onDrawerOpened(drawerView: View?) {
                setTitle("閉じる")
            }
        }
        drawer.setDrawerListener(toggle)
        toggle.syncState()

        val navigationView:NavigationView = findViewById(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener(this)
        /* メニューここまで */

        //ArticleViewオブジェクトを生成
        val articleView = ArticleView(applicationContext)

        val listAdapter = ArticleListAdapter(applicationContext)
        listAdapter.articles = listOf(dummyArticle("Kotlin入門","太郎"),dummyArticle("Java入門","じろう"))
        //Articleオブジェクトを生成して、AirticleViewオブジェクトにセット
        val listView: ListView = findViewById(R.id.list_view)
        listView.adapter = listAdapter
        listView.setOnItemClickListener { adapterView, view, position, id ->
            val article = listAdapter.articles[position]
            ArticleActivity.intent(this, article).let { startActivity(it) }
        }

        val gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
        val retrofit = Retrofit.Builder()
                .baseUrl("https://qita.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
        val articleClient = retrofit.create(ArticleClient::class.java)

        val queryEditText: EditText = findViewById(R.id.query_edit_text)
        val searchButton: Button = findViewById(R.id.search_button)
        searchButton.setOnClickListener {

            articleClient.search(queryEditText.text.toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    //.bindToLifecycle(this)
                    .subscribe({
                        queryEditText.text.clear()
                        listAdapter.articles = it
                        listAdapter.notifyDataSetChanged()
                    }, {
                        toast("エラー: $it")
                    })
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_version -> {
            }
            R.id.nav_gallery -> {
                // 変更したいレイアウトを取得する
                val layout:LinearLayout  = findViewById<View>(R.id.content_main) as LinearLayout
                // レイアウトのビューをすべて削除する
                layout.removeAllViews();
                // レイアウトをR.layout.sampleに変更する
                getLayoutInflater().inflate(R.layout.activity_sub, layout);
                drawer.closeDrawers();
            }
            R.id.nav_slideshow -> {
            }
            R.id.nav_billed -> {
                // 変更したいレイアウトを取得する
                val layout:LinearLayout  = findViewById<View>(R.id.content_main) as LinearLayout
                // レイアウトのビューをすべて削除する
                layout.removeAllViews();
                // レイアウトをR.layout.sampleに変更する
                getLayoutInflater().inflate(R.layout.activity_billed, layout);
                drawer.closeDrawers();
            }
        }
        return true
    }
    override fun onBackPressed() {
        val drawer: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
    private fun dummyArticle(title: String, userName: String): Article =
            Article(id = "",
                    title = title,
                    url = "https://kotlinklang.org",
                    user = User(id = "", name = userName, profileImageUrl =""))




}
