package chat.girlfriend.girlfriendchat.utils

import android.util.Log
import chat.girlfriend.girlfriendchat.MyApplication
import chat.girlfriend.girlfriendchat.models.Girl
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Modifier

const val GIRL_STORAGE = "girl_storage"
const val GIRL_STORAGE_KEY = "girl_key"
fun MyApplication.putLocalStorage(value: Girl): Unit {
    val gson = Gson()
    val cache = getLocalStorage()
    if (!cache.existGirl(value.id)) {
        cache.add(value)
        val jsonFavorites = gson.toJson(cache)
        this.sharedPreferences.edit().putString(GIRL_STORAGE, jsonFavorites).commit()
    }

}

fun MyApplication.getLocalStorage(): ArrayList<Girl> {
    val gson = Gson()
    val jsonFavorites = this.sharedPreferences.getString(GIRL_STORAGE, "")
    Log.d("local_storage", jsonFavorites)
    if (jsonFavorites != "") {
        val girlList = gson.fromJson<ArrayList<Girl>>(jsonFavorites, object : TypeToken<ArrayList<Girl>>() {}.type);
//        this.sharedPreferences.edit().putString("girl_storage", jsonFavorites)
        return girlList
    }
    return ArrayList()
}

fun MyApplication.removeLocalStorage(postition: Int): Unit {
    val gson = Gson()
    var jsonFavorites = this.sharedPreferences.getString(GIRL_STORAGE, "")
    if (jsonFavorites != "") {
        val girlList = gson.fromJson<ArrayList<Girl>>(jsonFavorites, object : TypeToken<ArrayList<Girl>>() {}.type);
//        this.sharedPreferences.edit().putString("girl_storage", jsonFavorites)
        girlList.removeAt(postition)
        jsonFavorites = gson.toJson(girlList)
        this.sharedPreferences.edit().putString(GIRL_STORAGE, jsonFavorites).commit()
    }
}

fun ArrayList<Girl>.existGirl(id: String): Boolean {
    this.forEach {
        if (it.id.equals(id)) {
            return true
        }
    }
    return false
}