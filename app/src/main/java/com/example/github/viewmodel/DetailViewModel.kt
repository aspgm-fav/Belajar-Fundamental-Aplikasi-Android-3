package com.example.github.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.github.data.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class DetailViewModel: ViewModel() {

    private val dataDetail = MutableLiveData<User>()

    fun setDetailUser(username: String){
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username"

        client.addHeader("Authorization", "token d7da4ed6f31910f564aa19222c858083aecf5f01")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?,
                                   responseBody: ByteArray) {
                val result = String(responseBody)
                try {
                    val responObject = JSONObject(result)
                    val userItem = User()

                    userItem.login = responObject.getString("login")
                    userItem.id = responObject.getString("id").toInt()
                    userItem.name = responObject.getString("name")
                    userItem.company = responObject.getString("company")
                    userItem.location = responObject.getString("location")
                    userItem.blog = responObject.getString("blog")
                    userItem.repository = responObject.getInt("public_repos")
                    userItem.follower = responObject.getInt("followers")
                    userItem.following = responObject.getInt("following")

                    dataDetail.postValue(userItem)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<out Header>?,
                                   responseBody: ByteArray?, error: Throwable) {
                when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
            }
        })
    }

    fun getDetailData(): LiveData<User>{
        return dataDetail
    }
}