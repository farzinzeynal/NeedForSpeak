package com.needforspeak.ui.network.model

import androidx.room.Embedded
import androidx.room.Relation
import az.needforspeak.model.local.Comment


class Post(
    @Embedded
    var postBody: PostBody? = null,

    @Relation(parentColumn = "postId", entityColumn = "postId", entity = Comment::class)
    var comments: List<Comment>? = null
)