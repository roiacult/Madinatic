package com.roacult.madinatic

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val nextPage = "http://157.230.19.233/api/declarations/?page=2".getNext()
        assertEquals(nextPage, 2)
    }
}


fun String.getNext() : Int {
//    http://157.230.19.233/api/declarations/?page=2
    val index = this.indexOf("page=")+5
    val subString = this.substring(index)
    val lastIndex = if(subString.contains("&"))
        subString.indexOf("&")
    else subString.length

    return subString.substring(0,lastIndex).toInt()
}