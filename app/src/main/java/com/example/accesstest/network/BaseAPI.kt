package com.example.accesstest.network

import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.accesstest.MyApplication
import com.example.accesstest.util.L
import org.json.JSONObject
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

open class BaseAPI {
    companion object {
        private const val TAG = "BaseAPI"
        private const val SOCKET_TIMEOUT_MS = 30000 // Http Request Timeout
        val requestQueue: RequestQueue by lazy { Volley.newRequestQueue(MyApplication.appContext) }
    }

    interface ResultListener {
        fun onResult(response: String?)
        fun onError(error: VolleyError)
    }

    /**
     * Creates a new request.
     *
     * @param method         the HTTP method to use
     * @param url            URL to fetch the JSON from
     * @param headerParams   A [JSONObject] to post with the request. Null is allowed and
     * @param bodyParams     A [JSONObject] to post with the request. Null is allowed and
     * indicates no parameters will be posted along with request.
     * @param resultListener Listener to receive the JSON response (Custom object: BaseJsonOutput)
     */
    protected fun createStringRequest(
        method: Int, url: String?,
        headerParams: Map<String, String>?,
        bodyParams: Map<String, String>?,
        resultListener: ResultListener?
    ): StringRequest {
        val stringRequest: StringRequest = object : StringRequest(method, url,
            Response.Listener { response: String ->
                L.d(TAG, "Response: ($url) ==> $response")
                resultListener?.onResult(response)
            },
            Response.ErrorListener { error: VolleyError ->
                Log.e(TAG, error.message, error)
                resultListener?.onError(error)
            }) {
            override fun getParams(): Map<String, String>? {
                return bodyParams
            }

            override fun getHeaders(): Map<String, String> {
                return headerParams ?: emptyMap()
            }
        }

        // default timeout : 2,500 ms
        // Set Request Timeout : 10,000 ms
        stringRequest.retryPolicy = DefaultRetryPolicy(
            SOCKET_TIMEOUT_MS,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        val body: String = try {
            URLDecoder.decode(String(stringRequest.body, StandardCharsets.UTF_8), "UTF-8")
        } catch (e: Exception) {
            "null"
        }
        L.d(TAG, "Send request: ($url) ==> Body: $body")
        return stringRequest
    }

}