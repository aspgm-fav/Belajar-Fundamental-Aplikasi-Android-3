package com.example.github.data.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.github.data.database.DatabaseContract.ColumnFavorite.Companion.AVATAR
import com.example.github.data.database.DatabaseContract.ColumnFavorite.Companion.LOGIN_NAME
import com.example.github.data.database.DatabaseContract.ColumnFavorite.Companion.TABLE_NAME
import com.example.github.data.database.DatabaseContract.ColumnFavorite.Companion.ID

internal class DatabaseHelper(context: Context): SQLiteOpenHelper(context,
    DATABASE_NAME, null,
    DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "db_fav_user"
        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_FAV = "CREATE TABLE $TABLE_NAME" +
                "($ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$LOGIN_NAME TEXT NOT NULL UNIQUE," +
                "$AVATAR TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_FAV)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

}