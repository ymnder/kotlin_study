package com.example.ginjake.kotlin_test.view.part

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.example.ginjake.kotlin_test.R
import com.example.ginjake.kotlin_test.model.Article


class TaskAddButton : View {
    constructor(context: Context?) : super(context)

    constructor(context: Context?,
                attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?,
                attrs: AttributeSet?,
                defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun create_task_add_button(layout: FrameLayout){
        LayoutInflater.from(context).inflate(R.layout.task_add_button, layout)
        val taskAddFloatingButton: android.support.design.widget.FloatingActionButton = layout.findViewById(R.id.task_add_floating_button)

        taskAddFloatingButton.setOnClickListener {
            Toast.makeText(context, "なんかのアクション", Toast.LENGTH_LONG).show()
        }
        /*
        LayoutInflater.from(context).inflate(R.menu.task_add_button, layout)
        //タスク登録ボタンの処理
        val TaskText: EditText = layout.findViewById(R.id.task_text)
        val searchButton: Button = layout.findViewById(R.id.search_button)

        searchButton.setOnClickListener {
            // データ自体は入るが、画面は更新されない。この機能自体が仮なので。
            val new_article = Article.create(title = TaskText.text.toString(),url = "https://hoge.com/",thumbnail = "https://hoge.com/wp-content/uploads/hoge-lumber-company.png",star = false)
        }
        */
    }

}