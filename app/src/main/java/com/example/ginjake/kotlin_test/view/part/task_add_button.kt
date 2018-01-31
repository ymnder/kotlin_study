package com.example.ginjake.kotlin_test.view.part

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import com.example.ginjake.kotlin_test.ArticleListAdapter
import com.example.ginjake.kotlin_test.R
import com.example.ginjake.kotlin_test.model.Article


class TaskAddButton : View {
    constructor(context: Context?) : super(context)

    constructor(context: Context?,
                attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?,
                attrs: AttributeSet?,
                defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun create_task_add_button(layout: LinearLayout){
        LayoutInflater.from(context).inflate(R.menu.task_add_button, layout)
        //タスク登録ボタンの処理
        val TaskText: EditText = layout.findViewById(R.id.task_text)
        val searchButton: Button = layout.findViewById(R.id.search_button)

        searchButton.setOnClickListener {
            // データ自体は入るが、画面は更新されない。この機能自体が仮なので。
            val new_article = Article.create(title = TaskText.text.toString())
        }

}

}