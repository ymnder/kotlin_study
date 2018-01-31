package com.example.ginjake.kotlin_test

/**
 * Created by ginjake on 2018/01/19.
 */
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.ginjake.kotlin_test.model.Article
import com.example.ginjake.kotlin_test.view.ArticleView
import java.nio.file.Files.size
import android.view.LayoutInflater
import android.widget.TextView
import android.R.attr.onClick
import android.view.MotionEvent


class ArticleListAdapter(public val context: Context): RecyclerView.Adapter<ArticleListAdapter.ViewHolder>()  {


    var articles: MutableList<Article> = arrayListOf()
    private lateinit var listener: View.OnTouchListener


    class ViewHolder(itemView: ArticleView): RecyclerView.ViewHolder(itemView) {
        var set_article_item: ArticleView? = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v:ArticleView = ArticleView(context)
        val vh: ViewHolder = ViewHolder(v)
        return vh
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int): Unit {
        holder?.set_article_item?.setArticle(articles[position])
        holder?.set_article_item?.view?.setId(holder?.getAdapterPosition());
    }

    override fun getItemCount(): Int = articles.size
    fun getCount(): Int = articles.size
    fun getItem(position : Int): Any? = articles[position]
    override fun getItemId(position :Int) : Long = 0

}
