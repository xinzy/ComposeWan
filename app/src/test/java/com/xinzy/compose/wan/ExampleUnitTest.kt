package com.xinzy.compose.wan

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
        assertEquals(4, 2 + 2)
    }


    @Test
    fun test1() {
        println(Long.MAX_VALUE)

        val time = 1709113111807
        println(time)

        val delta = 28 * 86400 * 1000
        val time2 = time + delta
        println(time2)

        println(delta)
    }
}