package com.harsh.lineupvalorant.data.sync

import android.content.Context
import android.content.SharedPreferences

object LineupSharedPref {
    private const val NAME = "LINEUPVALORANT"
    private const val MODE = Context.MODE_PRIVATE
    private val FIRESTORE_KEY = Pair("firestore_key","mykey")

    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit){
        val editor = edit()
        operation(editor)
        editor.apply()
    }
    var setValue: String?
    get() = preferences.getString(FIRESTORE_KEY.first, FIRESTORE_KEY.second)
        set(value) = preferences.edit{
            it.putString(FIRESTORE_KEY.first,value)
        }

}