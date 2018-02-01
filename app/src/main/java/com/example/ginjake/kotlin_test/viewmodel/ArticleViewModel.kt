package com.example.ginjake.kotlin_test.viewmodel

/**
 * Created by ginjake on 2018/01/19.
 */
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.*
import com.example.ginjake.kotlin_test.ArticleListAdapter
import com.example.ginjake.kotlin_test.R
import com.example.ginjake.kotlin_test.model.Article
import android.widget.Toast
import android.view.animation.AnimationUtils
import android.view.animation.Animation
import com.example.ginjake.kotlin_test.view.ArticleSingleView


class ArticleViewModel : RecyclerView {
    constructor(context: Context?) : super(context)

    constructor(context: Context?,
                attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?,
                attrs: AttributeSet?,
                defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    public val listAdapter: ArticleListAdapter by lazy {
        ArticleListAdapter(context)
    }

    fun create_list_view(layout:LinearLayout){

        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        LayoutInflater.from(context).inflate(R.layout.activity_list, layout)
        val mRecyclerView: RecyclerView = layout.findViewById(R.id.list_view)

        mRecyclerView.setLayoutManager(mLayoutManager);

        //記事一覧の中身をセットするため初期化
        listAdapter.articles = mutableListOf<Article>()
        //DBからデータを読み込みアダプターにセットする
        Article.read().forEach()
        {
            listAdapter.articles.add(Article(id = it.id,
                    title = it.title,
                    url = it.url))
        }

        //アダプターをViewに適用
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

        //タップの判定と処理
        listAdapter.touchViewAction = {
            context:Context,event:MotionEvent,article:Article ->
            if (event.action == MotionEvent.ACTION_UP) {
                if(event.eventTime - event.downTime < getResources().getInteger(R.integer.longTapTime) ) { //ロングタップと判定し、タップアクションを行いたくない場合がある
                    Toast.makeText(context, "タップ", Toast.LENGTH_LONG).show()
                    ArticleSingleView(context,layout,article)
                }
            }
        }

        mRecyclerView.setHasFixedSize(true)
        touchHelper.attachToRecyclerView(mRecyclerView)
        mRecyclerView.addItemDecoration(touchHelper)
    }
}