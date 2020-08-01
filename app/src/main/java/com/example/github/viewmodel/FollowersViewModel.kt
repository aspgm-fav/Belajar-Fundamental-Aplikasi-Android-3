package com.example.github.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.github.data.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowersViewModel: ViewModel() {

    val listFollowers = MutableLiveData<ArrayList<User>>()

    fun setFollowers(username: String) {
        val listItems = ArrayList<User>()
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username/followers"

        client.addHeader("Authorization", "token d7da4ed6f31910f564aa19222c858083aecf5f01")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?,
                                   responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val responsArray = JSONArray(result)

                    for (i in 0 until responsArray.length()){
                        val responObject = responsArray.getJSONObject(i)
                        val userItem = User()

                        userItem.login = responObject.getString("login")
                        userItem.avatar = responObject.getString("avatar_url")

                        listItems.add(userItem)
                    }
                    listFollowers.postValue(listItems)
                } catch (e: Exception){
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?,
                                   responseBody: ByteArray?, error: Throwable) {
                Log.d("onFailur", error.message.toString())
            }
        })
    }

    fun getFollowers(): LiveData<ArrayList<User>> {
        return listFollowers
    }
}