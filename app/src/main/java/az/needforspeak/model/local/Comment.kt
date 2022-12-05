package az.needforspeak.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.needforspeak.ui.network.model.PostBody

@Entity(
    tableName = "comments",
    foreignKeys = [ForeignKey(
        entity = PostBody::class,
        parentColumns = arrayOf("postId"),
        childColumns = arrayOf("postId"),
        onDelete = ForeignKey.CASCADE
    )]
)
class Comment(

    @PrimaryKey
    @ColumnInfo(name = "commentId")
    var commentId: String,

    @ColumnInfo(name = "postId")
    var postId: String = "q",

    @ColumnInfo(name = "commentUser")
    var commentUser: String? = null,

    @ColumnInfo(name = "body")
    var body: String? = null,

    @ColumnInfo(name = "date")
    var date: Long? = null
) : PostMain