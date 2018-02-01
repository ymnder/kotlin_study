package com.example.ginjake.kotlin_test

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.view.MenuItem
import com.example.ginjake.kotlin_test.model.Article


import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.example.ginjake.kotlin_test.client.UpdateClient
import com.example.ginjake.kotlin_test.viewmodel.ArticleViewModel
import com.example.ginjake.kotlin_test.view.part.DrawerMenu
import com.example.ginjake.kotlin_test.view.part.TaskAddButton
import io.realm.Realm
import io.realm.RealmConfiguration
import com.facebook.stetho.Stetho
import com.uphyca.stetho_realm.RealmInspectorModulesProvider

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
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        UpdateClient.get();

        /* メニュー開閉ボタン*/
        var Toolbar:DrawerMenu = DrawerMenu()
        Toolbar.create_menu(this,drawer,
                findViewById<View>(R.id.toolbar) as Toolbar)

        /* ナビゲーション */
        val navigationView:NavigationView = findViewById(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener(this)

        /* リストビュー*/
        val layout:LinearLayout  = findViewById<View>(R.id.content_main) as LinearLayout
        article_list.create_list_view(layout)

        /* タスク追加ボタン */
        task_add_button.create_task_add_button(layout)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_top -> {
                val layout:LinearLayout  = findViewById<View>(R.id.content_main) as LinearLayout// 変更したいレイアウト
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
                    Article.create(title="ことりん",url="https://www.google.co.jp/search?q=%E3%81%93%E3%81%A8%E3%82%8A%E3%82%93&source=lnms&tbm=isch&sa=X&ved=0ahUKEwiQ1YTaofXYAhURhbwKHYwABZkQ_AUICigB&biw=2133&bih=1054")
                )
                article_list.listAdapter.articles.add(
                        Article.create(title="honoka",url="http://honokak.osaka/")
                )
                article_list.listAdapter.articles.add(
                        Article.create(title="るびぃ",url="https://ja.wikipedia.org/wiki/Ruby_on_Rails")
                )

                article_list.listAdapter.notifyDataSetChanged()
                drawer.closeDrawers();
            }
            R.id.nav_kotori -> {
                val layout:LinearLayout  = findViewById<View>(R.id.content_main) as LinearLayout// 変更したいレイアウト
                change_view(layout,R.layout.activity_sub)
            }
            R.id.nav_billed -> {
                val layout:LinearLayout  = findViewById<View>(R.id.content_main) as LinearLayout// 変更したいレイアウト
                change_view(layout,R.layout.activity_billed)
            }
        }
        return true
    }


    fun change_view(layout:LinearLayout,activity_name: Int){
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
