package com.example.favoritelist.data.database

import android.net.Uri
import android.provider.BaseColumns
import com.example.favoritelist.data.database.DatabaseContract.ColumnFavorite.Companion.TABLE_NAME

object DatabaseContract {
    private const val AUTHORITY = "com.example.github"
    private const val SCHEME = "content"

    class ColumnFavorite : BaseColumns {
        companion object {
            const val TABLE_NAME = "github_user"
            const val ID = "_id"
            const val LOGIN_NAME = "login_name"
            const val AVATAR = "avatar"
        }
    }

    val CONTENT_URI : Uri = Uri.Builder().scheme(SCHEME)
        .authority(AUTHORITY)
        .appendPath(TABLE_NAME)
        .build()
}