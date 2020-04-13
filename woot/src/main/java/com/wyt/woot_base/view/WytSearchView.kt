package com.wyt.woot_base.view

import android.content.Context
import android.graphics.Color
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.wyt.woot_base.R


class WytSearchView : ConstraintLayout {

    private lateinit var editText: EditText //搜索输入框
    private lateinit var searchBtn: ImageView //搜索按钮
    private lateinit var hintView: Button  //提示栏
    private lateinit var rvSearch: RecyclerView
    private lateinit var closeBtn:ImageView
    private lateinit var adapter: SearchAdapter

    /**
     * 0->初始化状态
     * 1->搜索状态
     */
    private var states = 0

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        LayoutInflater.from(context).inflate(R.layout.search_view_layout, this, true)
        editText = findViewById(R.id.edit_search)
        searchBtn = findViewById(R.id.btn_search_submit)
        hintView = findViewById(R.id.btn_search)
        rvSearch = findViewById(R.id.rv_search)
        closeBtn = findViewById(R.id.btn_search_close)
        searchBtn.setColorFilter(Color.parseColor("#FFFFFF"))
        adapter = SearchAdapter()
        rvSearch.layoutManager = LinearLayoutManager(context)
        rvSearch.adapter = adapter
        val listen = OnClickListener {
            states = 1
            checkStates()
        }
        this.setOnClickListener(listen)
        hintView.setOnClickListener(listen)
        checkStates()
        closeBtn.setOnClickListener {
            states = 0
            editText.setText("")
            checkStates()
        }
    }


    private fun checkStates() {
        when (states) {
            0 -> {
                hideView(editText,searchBtn,rvSearch,closeBtn)
                showView(hintView)
            }
            1 -> {
                showView(editText,searchBtn,rvSearch,closeBtn)
                showSoftInputFromWindow()
                hideView(hintView)
            }
        }
    }

    private fun showSoftInputFromWindow(){
        editText.isFocusable = true
        editText.isFocusableInTouchMode = true
        editText.requestFocus()
        val inputManager =
            editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.showSoftInput(editText, 0)
    }

    private fun hideView(vararg views: View) {
        for (v in views) {
            v.visibility = View.GONE
        }
    }

    private fun showView(vararg views: View) {
        for(v in views){
            v.visibility = View.VISIBLE
        }
    }

    fun setData(list:List<String>?){
        adapter.setNewData(list)
    }

    fun addTextChangeListener(watch: TextWatcher){
        editText.addTextChangedListener(watch)
    }

    fun addSubmitListener(listener: OnClickListener){
        searchBtn.setOnClickListener(listener)
    }

    fun getSearchText():String{
        return editText.text.toString()
    }

    fun addAdapterItemClickListener(listener: BaseQuickAdapter.OnItemClickListener){
        adapter.onItemClickListener = listener
    }

    fun setText(txt: String){
        editText.setText(txt)
    }

    fun finishSearch(){
        editText.clearFocus()
    }



    class SearchAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_search) {
        override fun convert(helper: BaseViewHolder, item: String) {
            helper.setText(R.id.search_name,item)
        }
    }



}
