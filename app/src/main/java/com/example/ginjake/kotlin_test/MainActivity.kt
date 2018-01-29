package com.example.ginjake.kotlin_test

import android.app.PendingIntent.getActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.graphics.Color
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
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import android.support.annotation.NonNull
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.support.v7.widget.helper.ItemTouchHelper
import android.system.Os.read
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import io.realm.Realm
import io.realm.RealmConfiguration
import java.util.*
import com.example.ginjake.kotlin_test.model.Book
import com.facebook.stetho.Stetho
import com.uphyca.stetho_realm.RealmInspectorModulesProvider
import io.realm.RealmResults
import rx.Observable

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    private val mRecyclerView: RecyclerView? = null
    private var mAdapter: ArticleListAdapter? = null
    private lateinit var mRealm : Realm

    private val mLayoutManager: RecyclerView.LayoutManager by lazy{
        LinearLayoutManager(this)
    }
    private val listAdapter:ArticleListAdapter by lazy {
        ArticleListAdapter(applicationContext)
    }

    //左メニュー
    private val drawer: DrawerLayout by lazy{
        findViewById<View>(R.id.drawer_layout) as DrawerLayout
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Realm.init(this)
        val realmConfig = RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build()
        mRealm = Realm.getInstance(realmConfig)
        /* メニュー*/
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar

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


        val mRecyclerView : RecyclerView = findViewById(R.id.list_view)
        // use a linear layout manager
        mRecyclerView.setLayoutManager(mLayoutManager);

        listAdapter.articles = mutableListOf<Article>()//mutableListOf(dummyArticle("Kotlin入門","1st"),dummyArticle("Java入門","2nd"),dummyArticle("Java入門","3rd"),dummyArticle("Java入門","4th"))

        Article.read(mRealm).forEach {
            listAdapter.articles.add(Article(id = it.id,
                    title = it.title,
                    url = it.url))
        }

        mRecyclerView.setAdapter(listAdapter)

        mRecyclerView.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                Log.d("touch","これはとれる")
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                Log.d("touch","onTouchEvent")
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
                Log.d("touch","onRequestDisallowInterceptTouchEvent")
            }
        })

        val touchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            // ここで指定した方向にのみドラッグ可能

            //選択ステータスが変更された場合の処理を指定します
            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)
                if (actionState == ItemTouchHelper.ACTION_STATE_DRAG)
                    viewHolder?.itemView?.alpha = 0.5f
            }

            // アニメーションが終了する時に呼ばれる
            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                super.clearView(recyclerView, viewHolder)
                // e.g. 反透明にしていたのを元に戻す
                viewHolder?.itemView?.alpha = 1.0f
            }
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {

                val from = viewHolder.adapterPosition
                val to = target.adapterPosition
                listAdapter.notifyItemMoved(from, to)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // スワイプで削除する場合はここ
                val swipedPosition = viewHolder.getAdapterPosition()
                Log.d("swipe","ID:"+listAdapter.articles[swipedPosition].title)
                Article.delete(mRealm,listAdapter.articles[swipedPosition].id)
                listAdapter.articles.removeAt(swipedPosition);
                listAdapter.notifyItemRemoved(swipedPosition);
            }
        })
        mRecyclerView.setHasFixedSize(true)
        touchHelper.attachToRecyclerView(mRecyclerView)
        mRecyclerView.addItemDecoration(touchHelper)


        /*
        listView.setOnItemClickListener { adapterView, view, position, id ->
            val article = listAdapter.articles[position]
            ArticleActivity.intent(this, article).let { startActivity(it) }
        }*/

        val gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
        val retrofit = Retrofit.Builder()
                .baseUrl("https://qita.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
        val articleClient = retrofit.create(ArticleClient::class.java)

        val TaskText: EditText = findViewById(R.id.task_text)
        val searchButton: Button = findViewById(R.id.search_button)

        searchButton.setOnClickListener {
            // create test

            val new_article = Article.create(mRealm,TaskText.text.toString(),"http://www.ginjake.net/yagi/")
            listAdapter.articles.add(new_article)
            listAdapter.notifyDataSetChanged()
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_top -> {
                change_view(R.layout.activity_main)

            }
            R.id.nav_version -> {
                mRealm.executeTransaction(Realm.Transaction { realm -> realm.deleteAll() })
                listAdapter.articles = arrayListOf()
                listAdapter.notifyDataSetChanged()
            }
            R.id.nav_gallery -> {

                listAdapter.articles.add(
                    Article.create(mRealm,"ことりん","https://www.google.co.jp/search?q=%E3%81%93%E3%81%A8%E3%82%8A%E3%82%93&source=lnms&tbm=isch&sa=X&ved=0ahUKEwiQ1YTaofXYAhURhbwKHYwABZkQ_AUICigB&biw=2133&bih=1054")
                )
                listAdapter.articles.add(
                        Article.create(mRealm,"honoka","http://honokak.osaka/")
                )
                listAdapter.articles.add(
                        Article.create(mRealm,"るびぃ","https://ja.wikipedia.org/wiki/Ruby_on_Rails")
                )

                listAdapter.notifyDataSetChanged()

            }
            R.id.nav_slideshow -> {
                change_view(R.layout.activity_sub)
            }
            R.id.nav_billed -> {
                change_view(R.layout.activity_billed)
            }
        }
        return true
    }


    fun change_view(activity_name: Int){
        // 変更したいレイアウトを取得する
        val layout:LinearLayout  = findViewById<View>(R.id.content_main) as LinearLayout
        // レイアウトのビューをすべて削除する
        layout.removeAllViews();
        // レイアウトをR.layout.sampleに変更する
        getLayoutInflater().inflate(activity_name, layout);
        drawer.closeDrawers();
    }
    override fun onBackPressed() {
        val drawer: DrawerLayout = findViewById(R.id.drawer_layout)
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
    private fun dummyArticle(title: String, userName: String): Article =
            Article(id = "",
                    title = title,
                    url = "https://kotlinklang.org")
                   // user = User(id = "", name = userName, profileImageUrl =""))




}
