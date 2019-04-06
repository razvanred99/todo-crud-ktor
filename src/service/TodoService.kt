package ro.razvan.service

import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import ro.razvan.data.todo.NewTodo
import ro.razvan.data.todo.Todo
import ro.razvan.data.todo.Todos
import ro.razvan.service.DatabaseFactory.dbQuery

class TodoService {

    suspend fun getAll(): List<Todo> = dbQuery {
        Todos.selectAll()
            .mapNotNull { it.toTodo() }
    }

    suspend fun findByUserId(userId: Int): List<Todo> = dbQuery {
        Todos.select { Todos.userId eq userId }
            .mapNotNull { it.toTodo() }
    }

    suspend fun findById(id: Int): Todo? = dbQuery {
        Todos.select { Todos.id eq id }
            .mapNotNull { it.toTodo() }
            .singleOrNull()
    }

    @Throws(ExposedSQLException::class)
    suspend fun insert(newTodo: NewTodo): Todo? {

        val id = dbQuery {

            try {

                Todos.insert {
                    it[userId] = newTodo.userId
                    it[title] = newTodo.title
                    it[notes] = newTodo.notes
                    it[dateCreated] = newTodo.dateCreated
                } get Todos.id
            } catch (exc: ExposedSQLException) {

                null
            }

        }

        return findById(id ?: -1)
    }

    suspend fun deleteById(id: Int): Boolean {
        return dbQuery { Todos.deleteWhere { Todos.id eq id } > 0 }
    }

    suspend fun deleteByUserId(userId: Int): Boolean {
        return dbQuery { Todos.deleteWhere { Todos.userId eq userId } > 0 }
    }

    suspend fun update(id: Int, todo: NewTodo): Todo? {

        val update = dbQuery {

            try {

                Todos.update({ Todos.id eq id }) {
                    it[userId] = todo.userId
                    it[title] = todo.title
                    it[notes] = todo.notes
                    it[dateCreated] = todo.dateCreated
                }

            } catch (exc: ExposedSQLException) {
                0
            } > 0

        }

        return if(update) findById(id) else null
    }

    private fun ResultRow.toTodo() = Todo(
        id = this[Todos.id],
        title = this[Todos.title],
        notes = this[Todos.notes],
        userId = this[Todos.userId],
        dateCreated = this[Todos.dateCreated]
    )

}