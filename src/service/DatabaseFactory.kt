package ro.razvan.service

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.insert
import org.joda.time.DateTime
import org.joda.time.LocalDate
import ro.razvan.data.todo.Todos
import ro.razvan.data.user.Users
import java.util.*

object DatabaseFactory {

    fun init() {

        Database.connect(hikari())

        transaction {
            create(Users)
            create(Todos)

            Users.insert{
                it[name] = "Emma"
                it[surname] = "Rosu"
                it[birthDate] = DateTime.parse("1999-06-19")
            }
        }

    }

    private fun hikari(): HikariDataSource {
        return HikariDataSource(HikariConfig().apply {
            driverClassName = "org.h2.Driver"
            jdbcUrl = "jdbc:h2:mem:test"
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        })
    }

    suspend fun <T> dbQuery(block: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction { block() }
        }

}