package com.example.tutorialandroid.domain
import kotlinx.coroutines.delay

interface PostRepository {
    suspend fun fetchPosts(): List<PostDomain>
}

class FakePostRepository : PostRepository {
    override suspend fun fetchPosts(): List<PostDomain> {
        // Simule un délai réseau de 2 secondes
        delay(2000)
        // Données fake, simulant une réponse API
        return listOf(
            PostDomain(id = 1, userId = 1, title = "Fake post 1", body = "Description 1"),
            PostDomain(id = 2, userId = 1, title = "Fake post 2", body = "Description 2")
        )
    }
}