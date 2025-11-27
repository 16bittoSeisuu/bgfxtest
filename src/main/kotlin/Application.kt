import arrow.continuations.SuspendApp
import arrow.fx.coroutines.ResourceScope
import arrow.fx.coroutines.resourceScope
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.time.Duration

fun application(
  timeout: Duration = Duration.INFINITE,
  action: suspend ResourceCoroutineScope.() -> Unit,
) = SuspendApp(EmptyCoroutineContext, timeout) {
  logger.debug { "Hello, world!" }
  try {
    resourceScope {
      val scope =
        object :
          ResourceCoroutineScope,
          CoroutineScope by this@SuspendApp,
          ResourceScope by this@resourceScope {}
      scope.action()
    }
  } finally {
    logger.debug { "Goodbye!" }
  }
}

interface ResourceCoroutineScope :
  CoroutineScope,
  ResourceScope

private val logger = KotlinLogging.logger("Main")
