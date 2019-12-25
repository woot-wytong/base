package com.wyt.demo.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.wyt.demo.R

class TitleBar: ConstraintLayout {

    private var mTvLeft: TextView? = null
    private var mImgLeft: ImageView? = null
    private var mTvTitle: TextView? = null
    private var mTvRight: TextView? = null
    private var mImgRight: ImageView? = null
    private var mTvRight2: TextView? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs){
        LayoutInflater.from(context).inflate(R.layout.titlebar, this, true)
        mTvLeft = findViewById(R.id.tv_title_left)
        mImgLeft = findViewById(R.id.iv_title_left)
        mTvTitle = findViewById(R.id.tv_title_title)
        mTvRight = findViewById(R.id.tv_title_right)
        mImgRight = findViewById(R.id.iv_title_right)
        mTvRight2 = findViewById(R.id.tv_title_right2)
    }

    fun setTitle(s: String): TitleBar {
        mTvTitle!!.text = s
        return this
    }

    fun setTitleClick(listener: OnClickListener): TitleBar {
        mTvTitle!!.setOnClickListener(listener)
        return this
    }

    fun setLeftText(s: String): TitleBar {
        mImgLeft!!.visibility = View.GONE
        mTvLeft!!.visibility = View.VISIBLE
        mTvLeft!!.text = s
        return this
    }

    fun setRightClick(listener: OnClickListener): TitleBar {
        if (mTvRight!!.visibility == View.VISIBLE) {
            mTvRight!!.setOnClickListener(listener)
        } else {
            mImgRight!!.setOnClickListener(listener)
        }
        return this
    }

    fun setLeftClick(listener: OnClickListener): TitleBar {
        if (mTvLeft!!.visibility == View.VISIBLE) {
            mTvLeft!!.setOnClickListener(listener)
        } else {
            mImgLeft!!.setOnClickListener(listener)
        }
        return this
    }

    fun setRight2Text(s:String): TitleBar {
        mTvRight2!!.visibility = View.VISIBLE
        mTvRight2!!.text = s
        return this
    }

    fun setRight2Click(listener: OnClickListener): TitleBar {
        mTvRight2!!.setOnClickListener(listener)
        return this
    }

    fun setRightText(s: String): TitleBar {
        mImgRight!!.visibility = View.GONE
        mTvRight!!.visibility = View.VISIBLE
        mTvRight!!.text = s
        return this
    }

    fun setLeftImg(resId: Int): TitleBar {
        mTvLeft!!.visibility = View.GONE
        mImgLeft!!.visibility = View.VISIBLE
        mImgLeft!!.setImageResource(resId)
        return this
    }



    fun setRightImg(resId: Int): TitleBar {
        mTvRight!!.visibility = View.GONE
        mImgRight!!.visibility = View.VISIBLE
        mImgRight!!.setImageResource(resId)
        return this
    }

    fun initTitleBar(): TitleBar {
        mTvLeft!!.visibility = View.GONE
        mImgLeft!!.visibility = View.GONE
        mTvRight!!.visibility = View.GONE
        mImgRight!!.visibility = View.GONE
        return this
    }
}