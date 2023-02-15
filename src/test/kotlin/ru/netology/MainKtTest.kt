package ru.netology

import Post
import Video
import VideoAttachment
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
        WallService.addPost(Post(ownerID = 123, fromID = 222, text = "text1", date = "12.12.2022"))
        val result = WallService.updatePost(
            Post(
                id = 1,
                friendsOnly = true,
                text = "text2",
                ownerID = 123,
                fromID = 222,
                date = "12.02.2022",
                attachment = listOf(VideoAttachment(Video(id = 1231, ownerId = 222, title = "video")))
            )
        )
        assertEquals(true, result, "Error update post!")
    }


}
