package com.example.tutorialandroid.domain
import kotlinx.coroutines.delay
import com.example.tutorialandroid.network.PostAPI

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

class NetworkPostRepository(private val api: PostAPI) : PostRepository {
    override suspend fun fetchPosts(): List<PostDomain> {
        val postsDto = api.fetchPosts()
        val postsDomain = postsDto.map { it.toDomain() }
        return postsDomain
    }
}
