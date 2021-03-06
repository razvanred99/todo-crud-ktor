package ro.razvan.data.user

import kotlinx.serialization.Serializable
import org.joda.time.DateTime
import ro.razvan.util.serializer.DateTimeSerializer

@Serializable
data class NewUser(
    val name: String,
    val surname: String,
    @Serializable(with = DateTimeSerializer::class)
    val birthDate: DateTime?
)