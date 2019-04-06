package ro.razvan.data.todo

import kotlinx.serialization.Serializable
import org.joda.time.DateTime
import ro.razvan.util.serializer.DateTimeSerializer

@Serializable
data class NewTodo(
    val userId: Int,
    val title: String,
    val notes: String,
    @Serializable(with = DateTimeSerializer::class)
    val dateCreated: DateTime
)