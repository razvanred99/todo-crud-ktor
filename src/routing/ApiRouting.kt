package ro.razvan.routing

import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.NotFoundException
import io.ktor.features.StatusPages
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.route
import io.ktor.server.engine.defaultExceptionStatusCode
import io.ktor.util.KtorExperimentalAPI
import ro.razvan.data.response.ErrorResponse
import ro.razvan.service.TodoService
import ro.razvan.service.UserService

@KtorExperimentalAPI
fun Route.api(userService: UserService, todoService: TodoService) {

    route("/api") {
        user(userService)
        todo(todoService)
    }

}