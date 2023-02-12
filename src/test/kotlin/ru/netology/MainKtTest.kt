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
        val post = WallService.addPost(Post(ownerID = 123, fromID = 222, text = "text1", date = "12.12.2022"))
        val referenceRes = Post(id = 1, ownerID = 123, fromID = 222, text = "text1", date = "12.12.2022")
        assertEquals(referenceRes, post)
    }

    @Test
    fun updatePost() {
        val result = WallService.updatePost(
            Post(
                id = 1,
                friendsOnly = true,
                text = "text3",
                ownerID = 0,
                fromID = 0,
                date = "12.12.2022"
            )
        )
        assertEquals(true, result)
    }
}
