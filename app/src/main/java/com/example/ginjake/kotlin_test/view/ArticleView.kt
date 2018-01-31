package com.example.ginjake.kotlin_test.view

/**
 * Created by ginjake on 2018/01/19.
 */
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.ginjake.kotlin_test.R
import com.example.ginjake.kotlin_test.model.Article

class ArticleView : FrameLayout {

    constructor(context: Context?) : super(context)

    constructor(context: Context?,
                attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?,
                attrs: AttributeSet?,
                defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(context: Context?,
                attrs: AttributeSet?,
                defStyleAttr: Int,
                defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    var profileImageView: ImageView? = null
    var titleTextView: TextView? = null
    var userNameTextView: TextView? = null

    var view:View
    init {
        view = LayoutInflater.from(context).inflate(R.layout.view_article, this)
        profileImageView = findViewById(R.id.profile_image_view)
        titleTextView = findViewById(R.id.title_text_view)
        userNameTextView = findViewById(R.id.user_name_text_view)

        view.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(view: View, event: MotionEvent): Boolean {
                if (event.action == MotionEvent.ACTION_UP) {
                    if(event.eventTime - event.downTime < getResources().getInteger(R.integer.longTapTime) ) { //ロングタップと判定し、タップアクションを行いたくない場合がある
                        Toast.makeText(context, "タップ判定:"+titleTextView?.text.toString(), Toast.LENGTH_LONG).show();
                    }
                }
                return true
            }
        })
    }
    fun aiueo(){
        true
    }
    fun setArticle(article: Article) {
        titleTextView?.text = article.title
        //userNameTextView?.text = article.user.name

        // TODO プロフィール画像をセットする
        //Glide.with(context).load(article.user.profileImageUrl).into(profileImageView)
    }


}