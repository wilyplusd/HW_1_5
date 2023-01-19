package ru.netology

import Post
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class WallServiceTest {

    @Before
    fun clearBeforeTest() {
        WallService.clear()
    }

    private annotation class Before

    @Test
    fun addPost() {
        val post = WallService.addPost(Post(ownerID = 123, fromID = 222, text = "text1"))
        val referenceRes = Post(id = 1, ownerID = 123, fromID = 222, text = "text1")
        assertEquals(referenceRes, post)
    }

    @Test
    fun updatePost() {
        val result = WallService.updatePost(Post(id = 1, friendsOnly = true, text = "text3", ownerID = 0, fromID = 0))
        assertEquals(true, result)
    }
}
