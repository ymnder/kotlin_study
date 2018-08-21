package com.example.ginjake.kotlin_test


import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import com.example.ginjake.kotlin_test.client.UpdateClient
import com.example.ginjake.kotlin_test.model.Article
import com.example.ginjake.kotlin_test.presenter.ArticlePresenter
import com.example.ginjake.kotlin_test.view.part.DrawerMenu
import com.example.ginjake.kotlin_test.view.part.TaskAddButton
import com.example.ginjake.kotlin_test.viewmodel.ArticleViewModel
import io.realm.RealmResults


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, ArticlePresenter.View {

    private val presenter = ArticlePresenter(this)

    //左メニュー
    private val drawer: DrawerLayout by lazy {
        findViewById<View>(R.id.drawer_layout) as DrawerLayout
    }
    //リストビュー
    private val article_list: ArticleViewModel by lazy {
        ArticleViewModel(this)
    }
    //タスク追加ボタン
    private val task_add_button: TaskAddButton by lazy {
        TaskAddButton(this)
    }

    //コンテンツ書き換え部分
    val layout: FrameLayout by lazy {
        findViewById<View>(R.id.content_main) as FrameLayout
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.onCreate()

        /* メニュー開閉ボタン*/
        val toolbar: DrawerMenu = DrawerMenu()
        toolbar.create_menu(this, drawer,
                findViewById<View>(R.id.toolbar) as Toolbar)

        /* ナビゲーション */
        val navigationView: NavigationView = findViewById(R.id.navigation_view)
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
                change_view(layout, R.layout.activity_list)

                /* タスク追加ボタン */
                task_add_button.create_task_add_button(layout)

                /* 一覧 */
                article_list.create_list_view(layout)

            }
            R.id.nav_delete_all -> {
                presenter.deleteAll()
                article_list.listAdapter.articles = arrayListOf()
                article_list.listAdapter.notifyDataSetChanged()
                drawer.closeDrawers();
            }
            R.id.nav_test_data -> {
                presenter.getAll()
            }
            R.id.nav_kotori -> {
                change_view(layout, R.layout.activity_sub)
            }
            R.id.nav_billed -> {
                change_view(layout, R.layout.activity_billed)
            }
            R.id.nav_test_data_api -> {
                UpdateClient.getDataFromTestApi();

                change_view(layout, R.layout.activity_list)

                /* タスク追加ボタン */
                task_add_button.create_task_add_button(layout)

                /* 一覧 */
                article_list.create_list_view(layout)
            }

        }
        return true
    }


    fun change_view(layout: FrameLayout, activity_name: Int) {
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
        mRealm.close()
    }

    override fun updateArticles(articles: RealmResults<Article>) {
        article_list.listAdapter.articles.addAll(articles)
        article_list.listAdapter.notifyDataSetChanged()
        drawer.closeDrawers()
    }
}

