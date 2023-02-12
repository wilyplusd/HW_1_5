enum class ListPostType {
    post, copy, reply, postpone, suggest
}

enum class TypeAttachment {
    photo, video, file, link, audio
}

interface Attachment {
    val type: TypeAttachment
    val id: Int
    val ownerId: Int
    val title: String
}

class Photo(
    override val type: TypeAttachment = TypeAttachment.photo,
    override val id: Int,
    override val ownerId: Int,
    override val title: String,
    val albumId: Int? = null
) : Attachment {}

class Video(
    override val type: TypeAttachment,
    override val id: Int,
    override val ownerId: Int,
    override val title: String,
    val albumId: Int? = null
) : Attachment {}

class Audio(
    override val type: TypeAttachment,
    override val id: Int,
    override val ownerId: Int,
    override val title: String,
    val albumId: Int? = null
) : Attachment {}

class File(
    override val type: TypeAttachment,
    override val id: Int,
    override val ownerId: Int,
    override val title: String,
    val url: String
) : Attachment {}

class Link(
    override val type: TypeAttachment,
    override val id: Int,
    override val ownerId: Int,
    override val title: String,
    val url: String
) : Attachment {}

data class CommentPost(
    val count: Long = 0,
    val canPost: Boolean = true, //информация о том, может ли текущий пользователь комментировать запись
    val groupCanPost: Boolean = true, //информация о том, могут ли сообщества комментировать запись
    val canClose: Boolean = true, //может ли текущий пользователь закрыть комментарии к записи
    val canOpen: Boolean = true // может ли текущий пользователь открыть комментарии к записи
)

data class Like(
    val count: Long = 0,
    val userLikes: Boolean = true,  //наличие отметки «Мне нравится» от текущего пользователя
    val canLike: Boolean = true // информация о том, может ли текущий пользователь поставить отметку «Мне нравится»
)

data class Repost(
    val count: Long = 0,
    val userRepost: Boolean = true //наличие репоста от текущего пользователя
)

data class Copyright(
    val id: Int,
    val link: String,
    val name: String,
    val type: String
)

data class Geo(
    val type: String,
    val coordinates: String,
    val place: String
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
    val comments: CommentPost? = null,
    val views: Int? = null,
    val reposts: Repost? = null,
    val canDelete: Boolean = true,
    val canEdit: Boolean = true,
    val copyright: Copyright? = null, //Источник материала, объект с полями
    val postType: ListPostType = ListPostType.post,
    val geo: Geo? = null,
    val attachment: Array<Video>? = null,
    val signerI: Int? = null, //Идентификатор автора, если запись была опубликована от имени сообщества и подписана пользователем
    val copyHistory: Array<Repost>? = null, //Массив, содержащий историю репостов для записи.
    val canPin: Boolean = false, //Информация о том, может ли текущий пользователь закрепить запись
    val isPinned: Boolean = false, //Информация о том, что запись закреплена.
    val markedAsAds: Boolean = false, //Информация о том, содержит ли запись отметку «реклама
    val postponedId: Int? = null, // Идентификатор отложенной записи. Это поле возвращается тогда, когда запись стояла на таймере.
    val isFavorite: Boolean = false,
    // Поля post_source, donut не добавляла

)

object WallService {
    private var posts = emptyArray<Post>()
    private var idPost: Int = 0

    fun addPost(post: Post): Post {
        idPost = if (posts.isEmpty()) {
            1
        } else {
            (posts.last().id + 1).toInt()
        }
        posts += post.copy(id = idPost)
        return posts.last()
    }

    fun updatePost(updatedPost: Post): Boolean {
        var status: Boolean = false
        for ((index, post) in posts.withIndex()) {
            if (post.id == updatedPost.id) {
                posts[index] = post.copy(
                    friendsOnly = updatedPost.friendsOnly, //если запись была создана с опцией «Только для друзей
                    text = updatedPost.text,
                    likes = updatedPost.likes,
                    comments = updatedPost.comments,
                    views = updatedPost.views,
                    reposts = updatedPost.reposts,
                )
                status = true
            }
        }
        return status
    }

    fun returnListPost(): StringBuilder {
        var listPost = StringBuilder()
        for (post in posts) {
            listPost.append(post)
            listPost.append("\n")
        }
        return listPost
    }

    fun clear() {
        posts = emptyArray()
    }
}

fun main() {
    WallService.addPost(Post(ownerID = 111, fromID = 222, date = "12.02.2020", replyOwnerId = 133, text = "text1"))
    val video = Video(type = TypeAttachment.video, id = 1231, ownerId = 222, title = "audio")
    WallService.addPost(
        Post(
            ownerID = 222,
            fromID = 222,
            date = "01.02.2023",
            replyOwnerId = 13333,
            text = "text1",
            attachment = arrayOf(video)
        )
    )
    WallService.updatePost(
        Post(
            id = 1,
            ownerID = 0,
            fromID = 0,
            date = "14.02.2020",
            friendsOnly = true,
            text = "text3"
        )
    )
    print(WallService.returnListPost())
}