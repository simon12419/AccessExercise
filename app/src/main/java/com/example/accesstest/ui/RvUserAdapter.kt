package com.example.accesstest.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.accesstest.R
import com.example.accesstest.network.json.User
import kotlinx.android.synthetic.main.content_list_footer.view.*
import kotlinx.android.synthetic.main.content_list_item.view.*
import org.cxct.sportlottery.interfaces.OnSelectItemListener

class RvUserAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    internal enum class ItemType { ITEM, FOOTER }

    private var mDataList: MutableList<User> = mutableListOf()
    private var mOnSelectItemListener: OnSelectItemListener<User>? = null

    private val mRequestOptions = RequestOptions()
        .centerCrop()
        .placeholder(R.drawable.circle)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .dontTransform()

    private var mNeedMoreLoading = false

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ItemType.ITEM.ordinal) {
            val layoutView = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.content_list_item, viewGroup, false)
            ItemViewHolder(layoutView)

        } else {
            val layoutView = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.content_list_footer, viewGroup, false)
            FooterViewHolder(layoutView)
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        try {
            when (viewHolder) {
                is ItemViewHolder -> viewHolder.bind(position)
                is FooterViewHolder -> viewHolder.bind()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            mDataList.lastIndex -> ItemType.FOOTER.ordinal
            else -> ItemType.ITEM.ordinal
        }
    }

    fun setData(newDataList: MutableList<User>?, needMoreLoading: Boolean) {
        mNeedMoreLoading = needMoreLoading
        mDataList = newDataList ?: mutableListOf()

        if (mDataList.size > 0)
            mDataList.add(User(null, null, null, null)) //在最尾端添加一筆空物件，做 footer view 顯示用
        notifyDataSetChanged()
    }

    fun setOnSelectItemListener(onSelectItemListener: OnSelectItemListener<User>?) {
        mOnSelectItemListener = onSelectItemListener
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            val data = mDataList[position]

            itemView.apply {
                Glide.with(context)
                    .load(data.avatar_url)
                    .apply(mRequestOptions)
                    .into(iv_header)

                tv_login.text = data.login
                tv_staff.visibility = if (data.site_admin == true) View.VISIBLE else View.GONE
                tv_number.text = (position + 1).toString()

                setOnClickListener { mOnSelectItemListener?.onClick(data) }
            }
        }
    }

    inner class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            itemView.apply {
                tv_footer.setText(if (mNeedMoreLoading) R.string.loading else R.string.footer)
            }
        }
    }

}