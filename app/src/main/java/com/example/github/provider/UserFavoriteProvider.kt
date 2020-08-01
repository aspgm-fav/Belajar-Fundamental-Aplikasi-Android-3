package com.example.github.provider


import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.github.data.database.DatabaseContract.AUTHORITY
import com.example.github.data.database.DatabaseContract.CONTENT_URI
import com.example.github.data.database.DatabaseContract.ColumnFavorite.Companion.TABLE_NAME
import com.example.github.data.database.UserFavoriteHelper

class UserFavoriteProvider : ContentProvider() {

    companion object {
        private const val userFavorite = 1
        private const val userId = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var userFavoriteHelper: UserFavoriteHelper

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, userFavorite)
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", userId)
        }
    }

    override fun onCreate(): Boolean {
        userFavoriteHelper = UserFavoriteHelper.getInstance(context as Context)
        userFavoriteHelper.open()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when (sUriMatcher.match(uri)) {
            userFavorite -> userFavoriteHelper.queryAll()
            userId -> userFavoriteHelper.queryById(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (userFavorite) {
            sUriMatcher.match(uri) -> userFavoriteHelper.insert(values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val updated: Int = when (userId) {
            sUriMatcher.match(uri) -> userFavoriteHelper.update(uri.lastPathSegment.toString(), values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return updated
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (userId) {
            sUriMatcher.match(uri) -> userFavoriteHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }
}
