package com.example.ginjake.kotlin_test.view

/**
 * Created by ginjake on 2018/01/19.
 */
import android.content.Context
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.example.ginjake.kotlin_test.ArticleListAdapter
import com.example.ginjake.kotlin_test.R
import com.example.ginjake.kotlin_test.mRealm
import com.example.ginjake.kotlin_test.model.Article

class ArticleListView : RecyclerView {
    constructor(context: Context?) : super(context)

    constructor(context: Context?,
                attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?,
                attrs: AttributeSet?,
                defStyleAttr: Int) : super(context, attrs, defStyleAttr)



    private lateinit var  mRecyclerView: RecyclerView


    public val listAdapter: ArticleListAdapter by lazy {
        ArticleListAdapter(context)
    }


    fun create_list_view(layout:LinearLayout){

        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)

        LayoutInflater.from(context).inflate(R.layout.activity_list, layout)
        mRecyclerView = layout.findViewById(R.id.list_view)
        mRecyclerView.setLayoutManager(mLayoutManager);

        listAdapter.articles = mutableListOf<Article>()

        //DBからデータを読み込みアダプターにセットする
        Article.read().forEach()
        {
            listAdapter.articles.add(Article(id = it.id,
                    title = it.title,
                    url = it.url))
        }
        //アダプターをViewnに適用
        mRecyclerView.setAdapter(listAdapter)

        //リストアイテムのイベント処理
        val touchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(// ここで指定した方向にのみドラッグ可能
                ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            //ロングタップの処理
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

            //ロングタップで移動したときの処理
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                val from = viewHolder.adapterPosition
                val to = target.adapterPosition
                listAdapter.notifyItemMoved(from, to)
                // TODO DBにも変更を適用する
                return true
            }

            //スワイプの処理
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // 削除する
                val swipedPosition = viewHolder.getAdapterPosition()
                Article.delete(id = listAdapter.articles[swipedPosition].id)
                listAdapter.articles.removeAt(swipedPosition);
                listAdapter.notifyItemRemoved(swipedPosition);
            }
        })



        mRecyclerView.setHasFixedSize(true)
        touchHelper.attachToRecyclerView(mRecyclerView)
        mRecyclerView.addItemDecoration(touchHelper)
    }
}