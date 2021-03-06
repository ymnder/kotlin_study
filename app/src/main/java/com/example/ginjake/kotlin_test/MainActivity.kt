package com.example.ginjake.kotlin_test

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.view.MenuItem
import com.example.ginjake.kotlin_test.model.Article


import android.support.v7.widget.Toolbar
import android.text.Editable
import android.view.LayoutInflater
import android.util.Log
import android.view.View
import com.example.ginjake.kotlin_test.client.UpdateClient
import com.example.ginjake.kotlin_test.viewmodel.ArticleViewModel
import com.example.ginjake.kotlin_test.view.part.DrawerMenu
import com.example.ginjake.kotlin_test.view.part.TaskAddButton
import io.realm.Realm
import io.realm.RealmConfiguration
import com.facebook.stetho.Stetho
import com.uphyca.stetho_realm.RealmInspectorModulesProvider
import com.google.firebase.iid.FirebaseInstanceId
import android.content.ClipDescription
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.*
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


//DB
var mRealm : Realm? = null

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    //左メニュー
    private val drawer: DrawerLayout by lazy{
        findViewById<View>(R.id.drawer_layout) as DrawerLayout
    }
    //リストビュー
    private val article_list: ArticleViewModel by lazy{
        ArticleViewModel(this)
    }
    //タスク追加ボタン
    private val task_add_button: TaskAddButton by lazy{
        TaskAddButton(this)
    }

    //コンテンツ書き換え部分
    val layout:FrameLayout by lazy {
        findViewById<View>(R.id.content_main) as FrameLayout
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //firebaseの設定
        val mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        FirebaseMessaging.getInstance().subscribeToTopic("kotori")

        /* debug用 */
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());
        //DBの設定
        Realm.init(this)
        val realmConfig = RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build()
        mRealm = Realm.getInstance(realmConfig)

        //アップデート確認
        UpdateClient.getVersion();

        /* メニュー開閉ボタン*/
        var Toolbar:DrawerMenu = DrawerMenu()
        Toolbar.create_menu(this,drawer,
                findViewById<View>(R.id.toolbar) as Toolbar)

        /* ナビゲーション */
        val navigationView:NavigationView = findViewById(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener(this)

        /* リストビュー*/
        val layout: FrameLayout = findViewById<View>(R.id.content_main) as FrameLayout
        article_list.create_list_view(layout)

        /* タスク追加ボタン */
        task_add_button.create_task_add_button(layout)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_top -> {
                change_view(layout,R.layout.activity_list)

                /* タスク追加ボタン */
                task_add_button.create_task_add_button(layout)

                /* 一覧 */
                article_list.create_list_view(layout)

            }
            R.id.nav_delete_all -> {
                mRealm?.executeTransaction(Realm.Transaction { realm -> realm.deleteAll() })
                article_list.listAdapter.articles = arrayListOf()
                article_list.listAdapter.notifyDataSetChanged()
                drawer.closeDrawers();
            }
            R.id.nav_test_data -> {

                //テストデータを追加する
                article_list.listAdapter.articles.add(
                    Article.create(
                            title = "ことりん",
                            url = "https://www.google.co.jp/search?q=%E3%81%93%E3%81%A8%E3%82%8A%E3%82%93&source=lnms&tbm=isch&sa=X&ved=0ahUKEwiQ1YTaofXYAhURhbwKHYwABZkQ_AUICigB&biw=2133&bih=1054",
                            thumbnail = "http://daybydaypg.com/wp-content/uploads/2017/10/f3759c4c-8e84-74f6-8a4e-b2ec82c7347b.png",
                            star = false
                    )
                )
                article_list.listAdapter.articles.add(
                        Article.create(
                                title = "honoka",
                                url = "http://honokak.osaka/",
                                thumbnail = "http://honokak.osaka/assets/img/honoka.png",
                                star = false
                        )
                )
                article_list.listAdapter.articles.add(
                        Article.create(
                                title = "るびぃ",
                                url = "https://ja.wikipedia.org/wiki/Ruby_on_Rails",
                                thumbnail = "https://dyama.org/wp-content/uploads/2016/04/ruby.jpg",
                                star = false
                        )
                )

                article_list.listAdapter.notifyDataSetChanged()
                drawer.closeDrawers();
            }
            R.id.nav_kotori -> {
                change_view(layout,R.layout.activity_sub)
            }
            R.id.nav_billed -> {
                change_view(layout,R.layout.activity_billed)
            }
            R.id.nav_test_data_api -> {
                UpdateClient.getDataFromTestApi();

                change_view(layout,R.layout.activity_list)

                /* タスク追加ボタン */
                task_add_button.create_task_add_button(layout)

                /* 一覧 */
                article_list.create_list_view(layout)
            }

        }
        return true
    }


    fun change_view(layout:FrameLayout,activity_name: Int){
        layout.removeAllViews(); // レイアウトのビューをすべて削除する
        getLayoutInflater().inflate(activity_name, layout)
        drawer.closeDrawers(); //メニューを閉じる
    }
    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mRealm?.close()
    }
}

