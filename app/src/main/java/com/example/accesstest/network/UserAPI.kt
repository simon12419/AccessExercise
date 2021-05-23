package com.example.accesstest.network

import com.android.volley.Request
import java.util.*

class UserAPI : BaseAPI() {

    fun getUserDetail(username: String, resultListener: ResultListener) {
        val params = HashMap<String, String>()
        params["username"] = username
        val url = ApiUrl.BASE_URL + ApiUrl.renderString(ApiUrl.QUERY_USER_DETAIL, params)
        val stringRequest = createStringRequest(Request.Method.GET, url, null, null, resultListener)
        requestQueue.add(stringRequest)
    }

    fun getUserList(since: Int, per_page: Int, resultListener: ResultListener) {
        val url = ApiUrl.BASE_URL + ApiUrl.QUERY_USER_LIST +"?since=$since&per_page=$per_page"
        val stringRequest = createStringRequest(Request.Method.GET, url, null, null, resultListener)
        requestQueue.add(stringRequest)
    }

}