package com.example.accesstest.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.volley.VolleyError
import com.example.accesstest.R
import com.example.accesstest.network.BaseAPI
import com.example.accesstest.network.UserAPI
import com.example.accesstest.network.json.User
import com.example.accesstest.network.json.UserDetail
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch

class MainViewModel(private val androidContext: Context) : ViewModel() {
    companion object {
        private const val PAGE_SIZE = 20
    }

    private val mUserAPI by lazy { UserAPI() }
    private val mGson by lazy { Gson() }
    private var mLastUserId: Int = 0
    private var mIsGettingData = false //判斷請求任務是否進行中

    val userDetailResult: LiveData<UserDetail?>
        get() = _userDetailResult
    private val _userDetailResult = MutableLiveData<UserDetail?>()

    val userListResult: LiveData<List<User>?>
        get() = _userListResult
    private val _userListResult = MutableLiveData<List<User>?>()

    private val mUserList = mutableListOf<User>()

    fun getUserDetail(userName: String) {
        viewModelScope.launch {
            _userDetailResult.value = null
            mUserAPI.getUserDetail(userName, object : BaseAPI.ResultListener {
                override fun onResult(response: String?) {
                    val result = try {
                        mGson.fromJson(response, UserDetail::class.java)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        null
                    }
                    _userDetailResult.postValue(result)
                }

                override fun onError(error: VolleyError) {
                    _userDetailResult.postValue(null)
                }
            })
        }
    }

    //reset userList
    fun getFirstUserList() {
        mLastUserId = 0
        mUserList.clear()
        getMoreUserList()
    }

    //query next page userList
    fun getMoreUserList() {
        if (mUserList.size >= 100 || mIsGettingData)
            return
        mIsGettingData = true

        viewModelScope.launch {
            mUserAPI.getUserList(mLastUserId, PAGE_SIZE, object : BaseAPI.ResultListener {
                override fun onResult(response: String?) {
                    mIsGettingData = false
                    try {
                        val type = object : TypeToken<List<User>>() {}.type
                        val result = mGson.fromJson<List<User>>(response, type)
                        result.lastOrNull()?.id?.let { id -> mLastUserId = id }

                        mUserList.addAll(result)
                        if (mUserList.size > 100)
                            _userListResult.postValue(mUserList.subList(0, 100))
                        else
                            _userListResult.postValue(mUserList)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        onError(VolleyError(e.message))
                    }
                }

                override fun onError(error: VolleyError) {
                    mIsGettingData = false
                }
            })
        }
    }
}