package com.example.accesstest.ui

import android.os.Bundle
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.accesstest.R
import com.example.accesstest.util.JumpUtil
import com.example.swipertest.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_user_detail.*

class UserDetailFragment : BaseFragment<MainViewModel>(MainViewModel::class) {
    private val args: UserDetailFragmentArgs by navArgs()

    private val mRequestOptions = RequestOptions()
        .centerCrop()
        .placeholder(R.drawable.circle)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .dontTransform()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initButton()
        getUserDetail()
        initObserve()
    }

    private fun initButton() {
        btn_close.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun getUserDetail() {
        viewModel.getUserDetail(args.login)
    }

    private fun initObserve() {
        viewModel.userDetailResult.observe(viewLifecycleOwner, {
            try {
                Glide.with(iv_header.context)
                    .load(it?.avatar_url)
                    .apply(mRequestOptions)
                    .into(iv_header)

                tv_name.text = it?.name
                tv_bio.text = it?.bio
                tv_login.text = it?.login
                tv_staff.visibility = if (it?.site_admin == true) View.VISIBLE else View.GONE
                tv_location.text = it?.location
                val link = it?.blog ?: ""
                tv_link.text = createLink(link)

                tv_link.setOnClickListener {
                    JumpUtil.toExternalWeb(tv_link.context, link)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })
    }

    private fun createLink(link: String): Spanned {
        val htmlString = "<a href='${link}'>${link}</a>"
        return HtmlCompat.fromHtml(htmlString, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}