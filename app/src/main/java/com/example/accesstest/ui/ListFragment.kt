package com.example.accesstest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.accesstest.R
import com.example.accesstest.network.json.User
import com.example.swipertest.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_list.*
import org.cxct.sportlottery.interfaces.OnSelectItemListener

class ListFragment : BaseFragment<MainViewModel>(MainViewModel::class) {

    private var mNeedMoreLoading = true //判斷滑到底是否需要繼續加載
    private val mAdapter = RvUserAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initButton()
        initRecycleView()
        initObserve()
        resetList()
    }

    private fun initButton() {
        refreshLayout.setOnRefreshListener { resetList() }
    }

    private fun initRecycleView() {
        list.adapter = mAdapter
        mAdapter.setOnSelectItemListener(object : OnSelectItemListener<User> {
            override fun onClick(select: User) {
                goToUserDetailFragment(select.login?: "")
            }
        })

        //滑動至底監聽，判斷是否要再 query 資料
        list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                // RecyclerView.canScrollVertically(1) 的值表示是否能向上滚动，false表示已经滚动到底部
                // RecyclerView.canScrollVertically(-1) 的值表示是否能向下滚动，false表示已经滚动到顶部
                if (mNeedMoreLoading && !recyclerView.canScrollVertically(1))
                    getMoreUserList()
            }
        })
    }

    private fun initObserve() {
        viewModel.userListResult.observe(viewLifecycleOwner, {
            refreshLayout.isRefreshing = false
            mNeedMoreLoading = it?.size ?: 0 < 100
            mAdapter.setData(it?.toMutableList(), mNeedMoreLoading)
        })
    }

    private fun resetList() {
        mAdapter.setData(null, false) //清空資料
        viewModel.getFirstUserList()
    }

    private fun getMoreUserList() {
        viewModel.getMoreUserList()
    }

    private fun goToUserDetailFragment(login: String) {
        val action = ListFragmentDirections.actionListFragmentToUserDetailFragment(login)
        findNavController().navigate(action)
    }
}