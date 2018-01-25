package com.example.ginjake.kotlin_test

import android.app.PendingIntent.getActivity
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
import android.view.View
import android.widget.LinearLayout
import io.realm.Realm
import io.realm.RealmConfiguration
import java.util.*
import com.example.ginjake.kotlin_test.model.Book
import io.realm.RealmResults

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    private val mRecyclerView: RecyclerView? = null
    private var mAdapter: ArticleListAdapter? = null
    private lateinit var mRealm : Realm

    private val mLayoutManager: RecyclerView.LayoutManager by lazy{
        LinearLayoutManager(this)
    }

    //左メニュー
    private val drawer: DrawerLayout by lazy{
        findViewById<View>(R.id.drawer_layout) as DrawerLayout
    }
    override fun onCreate(savedInstanceState: Bundle?) {
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

        val listAdapter = ArticleListAdapter(applicationContext)
        listAdapter.articles = mutableListOf(dummyArticle("Kotlin入門","1st"),dummyArticle("Java入門","2nd"),dummyArticle("Java入門","3rd"),dummyArticle("Java入門","4th"))
        mRecyclerView.setAdapter(listAdapter)

        val touchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN
                        or ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT, ItemTouchHelper.UP or ItemTouchHelper.DOWN
                or ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            // ここで指定した方向にのみドラッグ可能

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {

                val from = viewHolder.adapterPosition
                val to = target.adapterPosition
                listAdapter.notifyItemMoved(from, to)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // スワイプで削除する場合はここ
                val swipedPosition = viewHolder.getAdapterPosition()
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

        val queryEditText: EditText = findViewById(R.id.query_edit_text)
        val searchButton: Button = findViewById(R.id.search_button)
        /*
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
        */
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_version -> {
            }
            R.id.nav_gallery -> {
                change_view(R.layout.activity_sub)
            }
            R.id.nav_slideshow -> {
                // create test
                create_book("test1",1)
                create_book("test2")

                // read test
                val getData = read_book()
                getData.forEach {
                    Log.d("debug","name :" + it.name + "price : " + it.price.toString())
                }

                // update test
                update_book(getData.first()!!.id, "updated")

                val getUpdatedData = read_book()
                getUpdatedData.forEach {
                    Log.d("debug","name :" + it.name + "price : " + it.price.toString())
                }

                // delete test
                delete_book(getData.first()!!.id)

                val getDeletedData = read_book()
                getDeletedData.forEach {
                    Log.d("debug","name :" + it.name + "price : " + it.price.toString())
                }

            }
            R.id.nav_billed -> {
                change_view(R.layout.activity_billed)
            }
        }
        return true
    }
    fun create_book(name:String, price:Long = 0){
        mRealm.executeTransaction {
            var book = mRealm.createObject(Book::class.java , UUID.randomUUID().toString())
            book.name = name
            book.price = price
            mRealm.copyToRealm(book)
        }
    }
    fun read_book() : RealmResults<Book> {
        return mRealm.where(Book::class.java).findAll()
    }
    fun update_book(id:String, name:String, price:Long = 0){
        mRealm.executeTransaction {
            var book = mRealm.where(Book::class.java).equalTo("id",id).findFirst()
            book!!.name = name
            if(price != 0.toLong()) {
                book.price = price
            }
        }
    }
    fun delete_book(id:String){
        mRealm.executeTransaction {
            var book = mRealm.where(Book::class.java).equalTo("id",id).findAll()
            book.deleteFromRealm(0)
        }
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
                    url = "https://kotlinklang.org",
                    user = User(id = "", name = userName, profileImageUrl =""))




}
