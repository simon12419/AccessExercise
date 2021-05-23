package com.example.accesstest.network

import java.util.HashMap
import java.util.regex.Pattern

object ApiUrl {
    const val BASE_URL = "https://api.github.com"

    const val QUERY_USER_DETAIL = "/users/{username}"
    const val QUERY_USER_LIST = "/users"

    /**
     * 根據鍵值對填充字符串
     * @param content: 要填充字符串
     * @param map: 填充參數
     * @return:
     */
    fun renderString(content: String?, map: HashMap<String, String>): String? {
        var str = content
        val sets = map.entries
        for ((key, value) in sets) {
            val regex = "\\{$key\\}"
            val pattern = Pattern.compile(regex)
            val matcher = pattern.matcher(str)
            str = matcher.replaceAll(value)
        }
        return str
    }
}