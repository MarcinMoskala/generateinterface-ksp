@file:Suppress("RedundantNullableReturnType")

package test.text

import academy.kt.GenerateInterface

class User(val id: String)

@GenerateInterface("UserRepository")
class MongoUserRepository: UserRepository {
    override fun findUser(userId: String): User? = TODO()
    override fun findUsers(): List<User> = TODO()
    override fun updateUser(user: User) { TODO() }
    override fun insertUser(user: User) { TODO() }
}

class FakeUserRepository: UserRepository {
    private var users = listOf<User>()

    override fun findUser(userId: String): User? =
        users.find { it.id == userId }

    override fun findUsers(): List<User> = users

    override fun updateUser(user: User) {
        val oldUsers = users.filter { it.id == user.id }
        users = users - oldUsers + user
    }

    override fun insertUser(user: User) {
        users = users + user
    }
}

fun main(args: Array<String>) {
    println("Hello World!")
}

