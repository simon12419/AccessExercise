package com.example.swipertest.ui.base

import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.example.accesstest.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.reflect.KClass
import kotlinx.android.synthetic.main.layout_loading.view.*

abstract class BaseActivity<T : ViewModel>(clazz: KClass<T>) : AppCompatActivity() {
    companion object {
        private const val TAG = "BaseActivity"
    }

    val viewModel: T by viewModel(clazz = clazz)

    private var loadingView: View? = null

    open fun loading() {
        loading(null)
    }

    open fun loading(message: String?) {
        if (loadingView == null) {
            loadingView = layoutInflater.inflate(R.layout.layout_loading, null)
            val params = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
            )
            addContentView(loadingView, params)
        } else {
            loadingView?.rl_loading?.visibility = View.VISIBLE
            loadingView?.rl_loading?.isClickable = true
        }

        loadingView?.pb_message?.text = message ?: getString(R.string.loading)
    }

    open fun hideLoading() {
        if (loadingView == null) {
            Log.d(TAG, "loadingView不存在")
        } else {
            loadingView?.rl_loading?.visibility = View.GONE
        }
    }

}
