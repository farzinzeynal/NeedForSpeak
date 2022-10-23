package az.needforspeak.utils

import android.graphics.Bitmap
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import az.needforspeak.R
import coil.load
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

fun <T> MutableLiveData<ArrayList<T>>.add(item: T) {
    val updatedItems = this.value as ArrayList
    updatedItems.add(item)
    this.postValue(updatedItems)
}

fun <T> MutableLiveData<ArrayList<T>>.remove(item: T) {
    val updatedItems = this.value as ArrayList
    updatedItems.remove(item)
    this.postValue(updatedItems)
}

fun <T> MutableLiveData<ArrayList<T>>.removeAt(position: Int) {
    if (position >= 0 && position < this.value?.size ?: -1) {
        val updatedItems = this.value as ArrayList
        updatedItems.removeAt(position)
        this.postValue(updatedItems)
    }
}

/**
 * This Kotlin extension function is responsible to replace blank values
 * @return all replaces blank values string
 */
fun String.replaceBlank(): String {
    return replace(" ".toRegex(), "")
}


/**
 * This Kotlin extension function is responsible to replace blank values
 * @return all replaces blank values string
 */
fun String.replaceString(replaceText: String): String {
    return replace(replaceText.toRegex(), "")
}


/**
 * This extension function is responsible to check if list position is bigger than list size to
 * eliminate indexoutofbounds exception
 */
fun <E> List<E>.getIfExists(position: Int): E? {
    return if (position in 0 until size)
        get(position)
    else
        null
}

/**
 * This extension function is responsible to check if list position is bigger than list size to
 * eliminate indexoutofbounds exception
 */
fun <E> Array<E>.getIfExists(position: Int): E? {
    return if (position in 0 until size)
        get(position)
    else
        null
}

fun <E> ArrayList<E>.removeIfExists(position: Int): E? {
    return if (position in 0 until size)
        removeAt(position)
    else
        null
}


fun Any?.isNull(): Boolean {
    return this == null
}

fun Any?.isNotNull(): Boolean {
    return this != null
}

fun <T> merge(first: List<T>, second: List<T>): List<T> {
    return first + second
}

fun <T, K, R> LiveData<T>.combineWith(liveData: LiveData<K>, block: (T?, K?) -> R): LiveData<R> {
    val result = MediatorLiveData<R>()
    result.addSource(this) {
        result.value = block(this.value, liveData.value)
    }
    result.addSource(liveData) {
        result.value = block(this.value, liveData.value)
    }
    return result
}

/**
 * This method is responsible to hide view visibility
 */
fun View.hide() {
    visibility = View.GONE
}

/**
 * This method is responsible to show view visibility
 */
fun View.show() {
    visibility = View.VISIBLE
}


fun Bitmap.persistImage(file: File) {
    val os: OutputStream
    try {
        os = FileOutputStream(file)
        compress(Bitmap.CompressFormat.JPEG, 100, os)
        os.flush()
        os.close()
    } catch (e: java.lang.Exception) {
        Log.e(javaClass.simpleName, "Error writing bitmap", e)
    }
}

fun File.sizeInKB() = java.lang.String.valueOf(length() / 1024).toInt()



