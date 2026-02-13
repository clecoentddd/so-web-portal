package boond.common

import java.time.Instant
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

private val logger = KotlinLogging.logger {}

data class ErrorResponse(
    val error: String,
    val message: String,
    val timestamp: Instant = Instant.now()
)

@RestControllerAdvice
class GlobalExceptionHandler {

  @ExceptionHandler(CommandException::class)
  fun handleCommandException(ex: CommandException): ResponseEntity<ErrorResponse> {
    val isAccessDenied = ex.message.contains("Access Denied", ignoreCase = true)

    logger.warn { "Command validation failed: ${ex.message}" }

    val status = if (isAccessDenied) HttpStatus.FORBIDDEN else HttpStatus.BAD_REQUEST
    val errorType = if (isAccessDenied) "ACCESS_DENIED" else "BAD_REQUEST"

    return ResponseEntity.status(status)
        .body(ErrorResponse(error = errorType, message = ex.message))
  }

  @ExceptionHandler(Exception::class)
  fun handleGenericException(ex: Exception): ResponseEntity<ErrorResponse> {
    // Find if there's a CommandException deep in the cause chain (common in
    // Axon/CompletableFuture)
    val commandEx = findCommandException(ex)
    if (commandEx != null) {
      return handleCommandException(commandEx)
    }

    logger.error(ex) { "Unexpected error occurred" }

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(
            ErrorResponse(
                error = "INTERNAL_ERROR",
                message = "An unexpected error occurred. Please contact support."))
  }

  private fun findCommandException(ex: Throwable?): CommandException? {
    if (ex == null) return null
    if (ex is CommandException) return ex
    return findCommandException(ex.cause)
  }
}
