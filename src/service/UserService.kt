package ro.razvan.service

import org.jetbrains.exposed.sql.*
import ro.razvan.data.user.NewUser
import ro.razvan.data.user.User
import ro.razvan.data.user.Users
import ro.razvan.service.DatabaseFactory.dbQuery

class UserService {

    suspend fun getAll(): List<User> = dbQuery {
        Users.selectAll()
            .map { it.toUser() }
    }

    suspend fun findById(id: Int): User? = dbQuery {
        Users.select { Users.id eq id }
            .mapNotNull { it.toUser() }
            .singleOrNull()
    }

    suspend fun insert(user: NewUser): User {
        var key = 0
        dbQuery {
            key = (Users.insert {
                it[name] = user.name
                it[surname] = user.surname
                it[birthDate] = user.birthDate
            } get Users.id)
        }

        return findById(key)!!
    }

    suspend fun deleteUser(id: Int): Boolean =
        dbQuery { Users.deleteWhere { Users.id eq id } > 0 }

    suspend fun updateUser(id: Int?, user: NewUser): User? {
        return if (id == null)
            insert(user)
        else {
            dbQuery {
                Users.update({ Users.id eq id }) {
                    it[name] = user.name
                    it[surname] = user.surname
                    it[birthDate] = user.birthDate
                }
            }

            findById(id)
        }
    }

    private fun ResultRow.toUser() = User(
        id = this[Users.id],
        name = this[Users.name],
        surname = this[Users.surname],
        birthDate = this[Users.birthDate]
    )

}