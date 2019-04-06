package ro.razvan.data.todo

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import ro.razvan.data.user.Users

object Todos : Table() {
    val id = integer("id").primaryKey().autoIncrement()
    val userId =
        integer("userId").references(Users.id, onDelete = ReferenceOption.CASCADE, onUpdate = ReferenceOption.CASCADE)
    val title = text("title")
    val notes = text("notes")
    val dateCreated = date("dateCreated")
}