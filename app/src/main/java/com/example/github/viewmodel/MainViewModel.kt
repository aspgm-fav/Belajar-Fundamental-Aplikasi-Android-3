package com.example.github.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.github.data.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MainViewModel: ViewModel() {

    val listGitSearch = MutableLiveData<ArrayList<User>>()

    fun setSearchUserGit(username: String){
        val listItems = ArrayList<User>()
        val client = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=$username"

        client.addHeader("Authorization", "token d7da4ed6f31910f564aa19222c858083aecf5f01")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?,
                                   responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val jsonObject = JSONObject(result)
                    val item = jsonObject.getJSONArray("items")
                    for (i in 0 until item.length()) {
                        val responObject = item.getJSONObject(i)
                        val userModel = User()

                        userModel.login = responObject.getString("login")
                        userModel.avatar = responObject.getString("avatar_url")

                        listItems.add(userModel)
                    }
                    listGitSearch.postValue(listItems)
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

    fun getGitSearch(): LiveData<ArrayList<User>> {
        return listGitSearch
    }
}