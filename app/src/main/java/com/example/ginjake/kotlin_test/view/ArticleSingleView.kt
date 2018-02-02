package com.example.ginjake.kotlin_test.view

/**
 * Created by ginjake on 2018/01/19.
 */
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.ginjake.kotlin_test.R
import com.example.ginjake.kotlin_test.model.Article

class ArticleSingleView(context: Context?, layout: LinearLayout, article: Article) : FrameLayout(context) {




    init {

        val view:View
        val anim = AnimationUtils.loadAnimation(context, R.anim.article_single)

        layout.removeAllViews(); // レイアウトのビューをすべて削除する
        view = LayoutInflater.from(context).inflate(R.layout.view_article_single, layout)

        val titleTextView: TextView = layout.findViewById(R.id.single_title_text_view)
        titleTextView.text = article.title

        val userNameTextView: TextView = layout.findViewById(R.id.user_name_text_view)
        userNameTextView.text = "そのうち名前を表示。"

        val webView:WebView = layout.findViewById(R.id.web_view)
        webView.webViewClient = WebViewClient()
        webView.loadUrl(article.url)

        val profileImageView: ImageView = layout.findViewById(R.id.profile_image_view)
        Glide.with(context).load(article.thumbnail).into(profileImageView)
        view.startAnimation(anim)

    }


}