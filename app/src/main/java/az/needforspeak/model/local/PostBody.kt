package com.needforspeak.ui.network.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "post_bodies")
data class PostBody(
    @PrimaryKey
    @ColumnInfo(name = "postId")
    var postId: String = "",

    @ColumnInfo(name = "postUserId")
    var postUserId: String? = null,

    @ColumnInfo(name = "subject")
    val subject: String,

    @ColumnInfo(name = "body")
    val body: String,

    @ColumnInfo(name = "conferenceRoomName")
    var conferenceRoomName: String? = null,

    @ColumnInfo(name = "postDate")
    var date: Long? = null,

    @ColumnInfo(name = "postImage")
    var postImage: List<String?>? = null,

    @ColumnInfo(name = "isPostStarred")
    var isStarred: Boolean? = false

)
