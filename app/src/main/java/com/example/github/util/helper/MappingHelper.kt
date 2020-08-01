package com.example.github.util.helper

import android.database.Cursor
import com.example.github.data.database.DatabaseContract
import com.example.github.data.model.User

object MappingHelper {
    fun mapCursorToArrayList(gitCursor: Cursor?): ArrayList<User> {
        val userList = ArrayList<User>()
        gitCursor?.apply {
            while (moveToNext()) {
                val userItems = User()
                userItems.id = getInt(getColumnIndexOrThrow(DatabaseContract.ColumnFavorite.ID))
                userItems.login = getString(getColumnIndexOrThrow(DatabaseContract.ColumnFavorite.LOGIN_NAME))
                userItems.avatar = getString(getColumnIndexOrThrow(DatabaseContract.ColumnFavorite.AVATAR))
                userList.add(userItems)
            }
        }
        return userList
    }
}
