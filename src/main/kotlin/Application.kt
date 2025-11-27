import arrow.continuations.SuspendApp
import arrow.fx.coroutines.ResourceScope
import arrow.fx.coroutines.resourceScope
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.time.Duration

fun application(
  timeout: Duration = Duration.INFINITE,
  action: suspend context(CoroutineScope, ResourceScope) () -> Unit,
) = SuspendApp(EmptyCoroutineContext, timeout) {
  logger.debug { "Hello, world!" }
  try {
    resourceScope {
      action()
    }
  } finally {
    logger.debug { "Goodbye!" }
  }
}

private val logger = KotlinLogging.logger("Main")
