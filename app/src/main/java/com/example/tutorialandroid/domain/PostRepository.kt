package com.example.tutorialandroid.domain

interface PostRepository {
    suspend fun fetchPosts(): List<PostDomain>
}

class FakePostRepository : PostRepository {
    override suspend fun fetchPosts(): List<PostDomain> {
        // Données fake, simulant une réponse API
        return listOf(
            PostDomain(id = 1, userId = 1, title = "Fake post 1", body = "Description 1"),
            PostDomain(id = 2, userId = 1, title = "Fake post 2", body = "Description 2")
        )
    }
}