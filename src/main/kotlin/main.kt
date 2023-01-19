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

data class Post(
    val id: Int = 0,
    val ownerID: Int, //Идентификатор владельца стены, на которой размещена запись
    val fromID: Int, //Идентификатор автора записи
    val friendsOnly: Boolean = false, //если запись была создана с опцией «Только для друзей
    val text: String,
    val likes: Like = Like(0),
    val comments: CommentPost = CommentPost(0),
    val views: Int = 0,
    val reposts: Repost = Repost(0),
    val canDelete: Boolean = true,
    val canEdit: Boolean = true

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
    WallService.addPost(Post(ownerID = 111, fromID = 222, text = "text1"))
    WallService.addPost(Post(ownerID = 333, fromID = 444, text = "text2"))
    WallService.updatePost(Post(id = 1, friendsOnly = true, text = "text3", ownerID = 0, fromID = 0))
    print(WallService.returnListPost())
}