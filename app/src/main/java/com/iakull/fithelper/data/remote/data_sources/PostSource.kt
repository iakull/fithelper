package com.iakull.fithelper.data.remote.data_sources

import androidx.lifecycle.LiveData
import com.iakull.fithelper.data.model.Post
import com.iakull.fithelper.data.remote.firestore
import com.iakull.fithelper.data.remote.uid
import com.iakull.fithelper.util.live_data.liveData

class PostSource {

    private val postsCollection = firestore.collection("posts")

    fun postsLive() = postsCollection.liveData { it.toObject(Post::class.java)!! }

    fun postsLive(authorId: String): LiveData<List<Post>> {
        return postsCollection.whereEqualTo("authorId", authorId).liveData {
            it.toObject(Post::class.java)!!
        }
    }

    fun publishPost(post: Post) {
        postsCollection.document(post.id).set(post)
    }

    fun likePost(post: Post) {
        val likes = post.likes.toMutableList()
        if (likes.contains(uid)) likes.remove(uid) else likes.add(uid)
        postsCollection.document(post.id).update("likes", likes)
    }
}