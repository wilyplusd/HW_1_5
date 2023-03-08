import PostNotFoundException
import PostNotFoundException as PostNotFoundException1
public class NotFoundComment : Exception("Not found comment!") {}
public class PostNotFoundException : Exception("Post not found") {}
enum class ListPostType {
    post, copy, reply, postpone, suggest
}

enum class TypeAttachment {
    photo, video, file, link, audio
}

interface Attachment {
    val type: TypeAttachment
}

open class AudioAttachment(open val audio: Audio) : Attachment {
    override val type: TypeAttachment = TypeAttachment.audio
}

open class VideoAttachment(open val video: Video) : Attachment {
    override val type: TypeAttachment = TypeAttachment.video
}

open class FileAttachment(open val file: File) : Attachment {
    override val type: TypeAttachment = TypeAttachment.file
}

open class LinkAttachment(open val link: Link) : Attachment {
    override val type: TypeAttachment = TypeAttachment.link
}

open class PhotoAttachment(open val photo: Photo) : Attachment {
    override val type: TypeAttachment = TypeAttachment.photo
}

data class Audio(
    val id: Int,
    val idPost:Int,
    val duration: Int,
    val artist: String,
)

data class Photo(
    val id: Int,
    val idPost:Int,
    val ownerId: Int,
    val title: String,
    val albumId: Int? = null
)

data class Video(
    val id: Int,
    val idPost:Int,
    val ownerId: Int,
    val title: String,
    val albumId: Int? = null
)

data class File(
    val id: Int,
    val idPost:Int,
    val ownerId: Int,
    val title: String,
    val url: String
)

data class Link(
    val id: Int,
    val idPost:Int,
    val ownerId: Int,
    val title: String,
    val url: String
)

//data class CommentPost(
//    val count: Long = 0,
//    val canPost: Boolean = true, //информация о том, может ли текущий пользователь комментировать запись
//    val groupCanPost: Boolean = true, //информация о том, могут ли сообщества комментировать запись
//    val canClose: Boolean = true, //может ли текущий пользователь закрыть комментарии к записи
//    val canOpen: Boolean = true // может ли текущий пользователь открыть комментарии к записи
//)

data class Comment(
    val id: Int = 0,
    val idPost:Int,
    val from_id: Int,
    val date: String, // Время публикации записи в формате unixtime
    val text: String,
)

data class Like(
    val count: Long = 0,
    val idPost:Int,
    val userLikes: Boolean = true,  //наличие отметки «Мне нравится» от текущего пользователя
    val canLike: Boolean = true // информация о том, может ли текущий пользователь поставить отметку «Мне нравится»
)

data class Repost(
    val count: Long = 0,
    val idPost:Int,
    val userRepost: Boolean = true //наличие репоста от текущего пользователя
)



data class Post(
    val id: Int = 0,
    val ownerID: Int, //Идентификатор владельца стены, на которой размещена запись
    val fromID: Int, //Идентификатор автора записи
    val date: String, // Время публикации записи в формате unixtime
    val friendsOnly: Boolean = false, //если запись была создана с опцией «Только для друзей
    val replyOwnerId: Int? = null, //Идентификатор владельца записи, в ответ на которую была оставлена текущая.
    val replyPostId: Int? = null, //Идентификатор записи, в ответ на которую была оставлена текущая
    val text: String,
    val likes: Like? = null,
    val views: Int? = null,
    val reposts: Repost? = null,
    val canDelete: Boolean = true,
    val canEdit: Boolean = true,
    val copyright: String? = null, //Источник материала, объект с полями
    val postType: ListPostType = ListPostType.post,
    val geo: String? = null,
    val signerI: Int? = null, //Идентификатор автора, если запись была опубликована от имени сообщества и подписана пользователем
    val copyHistory: List<Int>? = null, //Массив, содержащий историю репостов для записи.
    val canPin: Boolean = false, //Информация о том, может ли текущий пользователь закрепить запись
    val isPinned: Boolean = false, //Информация о том, что запись закреплена.
    val markedAsAds: Boolean = false, //Информация о том, содержит ли запись отметку «реклама
    val postponedId: Int? = null, // Идентификатор отложенной записи. Это поле возвращается тогда, когда запись стояла на таймере.
    val isFavorite: Boolean = false,
    // Поля post_source, donut не добавляла

) {}

object WallService {
    private var posts = mutableListOf<Post>()
    // private var posts = emptyArray<Post>()
    private var comments = mutableListOf<Comment>()


    fun addPost(ownerID: Int, //Идентификатор владельца стены, на которой размещена запись
                fromID: Int, //Идентификатор автора записи
                date: String, // Время публикации записи в формате unixtime
                friendsOnly: Boolean = false, //если запись была создана с опцией «Только для друзей
                replyOwnerId: Int? = null, //Идентификатор владельца записи, в ответ на которую была оставлена текущая.
                replyPostId: Int? = null, //Идентификатор записи, в ответ на которую была оставлена текущая
                text: String,
                likes: Like? = null,
                comments: List<Int>? = null,
                views: Int? = null,
                reposts: Repost? = null,
                canDelete: Boolean = true,
                canEdit: Boolean = true,
                copyright: String? = null, //Источник материала, объект с полями
                postType: ListPostType = ListPostType.post,
                geo: String? = null,
                signerI: Int? = null, //Идентификатор автора, если запись была опубликована от имени сообщества и подписана пользователем
                copyHistory: List<Int>? = null, //Массив, содержащий историю репостов для записи.
                canPin: Boolean = false, //Информация о том, может ли текущий пользователь закрепить запись
                isPinned: Boolean = false, //Информация о том, что запись закреплена.
                markedAsAds: Boolean = false, //Информация о том, содержит ли запись отметку «реклама
                postponedId: Int? = null, // Идентификатор отложенной записи. Это поле возвращается тогда, когда запись стояла на таймере.
                isFavorite: Boolean = false): Post {

        var id = if (posts.isEmpty()) {
            1
        } else {
            (posts.last().id + 1).toInt()
        }
        posts.add(Post(
            id = id,
            friendsOnly = friendsOnly, //если запись была создана с опцией «Только для друзей
            text = text,
            likes = likes,
            views = views,
            reposts = reposts,
            fromID = fromID,
            ownerID = ownerID,
            canDelete = canDelete,
            canEdit = canEdit,
            canPin = canPin,
            copyHistory = copyHistory,
            copyright = copyright,
            date = date,
            geo = geo,
            postType = postType,
            replyPostId = replyPostId,
            isFavorite = isFavorite,
            signerI = signerI,
            isPinned = isPinned,
            markedAsAds = markedAsAds,
            postponedId = postponedId,
            replyOwnerId = replyOwnerId))
        return posts.last()
    }

    fun updatePost(
        id: Int,
        ownerID: Int, //Идентификатор владельца стены, на которой размещена запись
        fromID: Int, //Идентификатор автора записи
        date: String, // Время публикации записи в формате unixtime
        friendsOnly: Boolean = false, //если запись была создана с опцией «Только для друзей
        replyOwnerId: Int? = null, //Идентификатор владельца записи, в ответ на которую была оставлена текущая.
        replyPostId: Int? = null, //Идентификатор записи, в ответ на которую была оставлена текущая
        text: String,
        likes: Like? = null,
        views: Int? = null,
        reposts: Repost? = null,
        canDelete: Boolean = true,
        canEdit: Boolean = true,
        copyright: String? = null, //Источник материала, объект с полями
        postType: ListPostType = ListPostType.post,
        geo: String? = null,
        signerI: Int? = null, //Идентификатор автора, если запись была опубликована от имени сообщества и подписана пользователем
        copyHistory: List<Int>? = null, //Массив, содержащий историю репостов для записи.
        canPin: Boolean = false, //Информация о том, может ли текущий пользователь закрепить запись
        isPinned: Boolean = false, //Информация о том, что запись закреплена.
        markedAsAds: Boolean = false, //Информация о том, содержит ли запись отметку «реклама
        postponedId: Int? = null, // Идентификатор отложенной записи. Это поле возвращается тогда, когда запись стояла на таймере.
        isFavorite: Boolean = false): Boolean {
        for ((index, post) in posts.withIndex()) {
            if (post.id == id) {
                posts[index] = post.copy(
                    id = id,
                    friendsOnly = friendsOnly, //если запись была создана с опцией «Только для друзей
                    text = text,
                    likes = likes,
                    views = views,
                    reposts = reposts,
                    fromID = fromID,
                    ownerID = ownerID,
                    canDelete = canDelete,
                    canEdit = canEdit,
                    canPin = canPin,
                    copyHistory = copyHistory,
                    copyright = copyright,
                    date = date,
                    geo = geo,
                    postType = postType,
                    replyPostId = replyPostId,
                    isFavorite = isFavorite,
                    signerI = signerI,
                    isPinned = isPinned,
                    markedAsAds = markedAsAds,
                    postponedId = postponedId,
                    replyOwnerId = replyOwnerId)
                return true
            }
        }
        throw PostNotFoundException()
    }
    //********************************** Домашнее задание 7 *********************************
    fun createComment(id: Int = 0,
                      idPost:Int,
                      from_id: Int,
                      date: String, // Время публикации записи в формате unixtime
                      text: String,
    ): Comment {
        var id: Int = 0
        if (posts.isNotEmpty()) {
            for (post in posts) {
                if (post.id == idPost) {
                    id = if (comments.isEmpty()) {
                        1
                    } else {
                        (comments.last().id + 1).toInt()
                    }
                    comments.add(Comment(id = id, idPost = idPost, from_id = from_id, date = date, text = text))
                    return comments.last()
                }
            }
        }
        throw PostNotFoundException()

    }
    fun getAllPost(): List<Post> {
        val returnPost = mutableListOf<Post>()
        for (post in posts) {
                returnPost.add(post)
        }
        if (returnPost.isNotEmpty()) {
            return returnPost
        }
        throw PostNotFoundException()
    }
    fun getAllComments(): List<Comment> {
        val returnComments = mutableListOf<Comment>()
        for (comment in comments) {
                returnComments.add(comment)
        }
        if (returnComments.isNotEmpty()) {
            return returnComments
        }
        throw NotFoundComment()
    }


    fun clear() {
        posts.clear()
        comments.clear()
    }
}




fun main() {
//    WallService.addPost(
//        ownerID = 222,
//        fromID = 222,
//        date = "01.02.2023",
//        replyOwnerId = 13333,
//        text = "text1",
//
//    )
//    WallService.updatePost(
//            id = 1,
//            ownerID = 322,
//            fromID = 222,
//            date = "01.02.2023",
//            replyOwnerId = 13333,
//            text = "text1"
//    )
//
//
//    print(WallService.getAllPost())
//
//    //********************************** Домашнее задание 7 *********************************
//
//    try{
//        val comment = WallService.createComment(
//            idPost = 1,
//            from_id = 222,
//            date = "01.02.2023",
//            text = "comment")
//        println(comment)
//    } catch (e: PostNotFoundException1)
//    {
//        print("Пост не найден!")
//    }
//    print(WallService.getAllComments())
}

