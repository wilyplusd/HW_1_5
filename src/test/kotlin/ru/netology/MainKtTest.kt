package ru.netology

import Attachment
import Audio
import AudioAttachment
import Comment
import NotFoundComment
import Post
import PostNotFoundException
import Video
import VideoAttachment
import WallService
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class WallServiceTest {
    @BeforeTest
    fun clearBeforeTest() {
        WallService.clear()
    }

    @Test
    fun addPost() {
        val post = WallService.addPost(
            ownerID = 222,
            fromID = 222,
            date = "01.02.2023",
            replyOwnerId = 13333,
            text = "text1"
        )
        val referenceRes = Post(
            id = 1, ownerID = 222,
            fromID = 222,
            date = "01.02.2023",
            replyOwnerId = 13333,
            text = "text1"
        )
        assertEquals(referenceRes, post)
    }

    @Test
    fun updatePost() {
        WallService.addPost(
            ownerID = 322,
            fromID = 222,
            date = "01.02.2023",
            replyOwnerId = 13333,
            text = "text1"
        )
        val result = WallService.updatePost(
            id = 1,
            ownerID = 322,
            fromID = 222,
            date = "01.02.2023",
            replyOwnerId = 13333,
            text = "text1"
        )
        assertEquals(true, result, "Error update post!")
    }

    @Test
    fun createComment(){
        WallService.addPost(
            ownerID = 322,
            fromID = 222,
            date = "01.02.2023",
            replyOwnerId = 13333,
            text = "text1"
        )
        val result =  WallService.createComment(
            idPost = 1,
            from_id = 222,
            date = "01.02.2023",
            text = "comment")
        val reference = Comment(
            id = 1,
            idPost = 1,
            from_id = 222,
            date = "01.02.2023",
            text = "comment"
        )
        assertEquals(reference, result, "Error!")
    }

    @Test
    fun shouldThrowPostNotFound() {
        WallService.clear()
        assertFailsWith(
            exceptionClass = PostNotFoundException::class,
            block = {
                WallService.addPost(
                    ownerID = 322,
                    fromID = 222,
                    date = "01.02.2023",
                    replyOwnerId = 13333,
                    text = "text1"
                )
                WallService.updatePost(
                    id = 511,
                    ownerID = 322,
                    fromID = 222,
                    date = "01.01.2021",
                    replyOwnerId = 13333,
                    text = "text1"
                )
            }
        )

        assertFailsWith(
            exceptionClass = PostNotFoundException::class,
            block = {

                WallService.createComment(
                    idPost = 12,
                    from_id = 222,
                    date = "01.02.2023",
                    text = "comment"
                )

            }
        )
        assertFailsWith(
            exceptionClass = PostNotFoundException::class,
            block = {
                WallService.clear()
                WallService.getAllPost()

            }
        )
    }

    @Test
    fun shouldThrowCommentNotFound() {
        WallService.clear()
        assertFailsWith(
            exceptionClass = NotFoundComment::class,
            block = {
                WallService.clear()
                WallService.getAllComments()
            }
        )
    }
}


