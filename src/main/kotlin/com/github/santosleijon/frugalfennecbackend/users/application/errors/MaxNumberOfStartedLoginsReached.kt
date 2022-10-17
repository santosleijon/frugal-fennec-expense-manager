import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.http.HttpStatus

@ResponseStatus(code = HttpStatus.TOO_MANY_REQUESTS, reason = "Max number of allowed started logins has been reached")
class MaxNumberOfStartedLoginsReached() : RuntimeException("Max number of allowed started logins has been reached")
