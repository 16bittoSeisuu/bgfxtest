
import io.github.oshai.kotlinlogging.KotlinLogging
import net.japanesehunter.math.Color
import net.japanesehunter.math.cyan
import net.japanesehunter.math.gray80
import net.japanesehunter.math.magenta
import net.japanesehunter.math.toAbgr8888
import net.japanesehunter.math.toRgba8888
import net.japanesehunter.math.yellow
import org.lwjgl.BufferUtils
import org.lwjgl.bgfx.BGFX.BGFX_ATTRIB_COLOR0
import org.lwjgl.bgfx.BGFX.BGFX_ATTRIB_POSITION
import org.lwjgl.bgfx.BGFX.BGFX_ATTRIB_TYPE_FLOAT
import org.lwjgl.bgfx.BGFX.BGFX_ATTRIB_TYPE_UINT8
import org.lwjgl.bgfx.BGFX.BGFX_BUFFER_NONE
import org.lwjgl.bgfx.BGFX.BGFX_CLEAR_COLOR
import org.lwjgl.bgfx.BGFX.BGFX_CLEAR_DEPTH
import org.lwjgl.bgfx.BGFX.BGFX_DISCARD_NONE
import org.lwjgl.bgfx.BGFX.BGFX_NATIVE_WINDOW_HANDLE_TYPE_DEFAULT
import org.lwjgl.bgfx.BGFX.BGFX_NATIVE_WINDOW_HANDLE_TYPE_WAYLAND
import org.lwjgl.bgfx.BGFX.BGFX_RENDERER_TYPE_COUNT
import org.lwjgl.bgfx.BGFX.BGFX_RENDERER_TYPE_METAL
import org.lwjgl.bgfx.BGFX.BGFX_RENDERER_TYPE_NOOP
import org.lwjgl.bgfx.BGFX.BGFX_RENDERER_TYPE_VULKAN
import org.lwjgl.bgfx.BGFX.BGFX_RESET_VSYNC
import org.lwjgl.bgfx.BGFX.BGFX_STATE_DEFAULT
import org.lwjgl.bgfx.BGFX.bgfx_copy
import org.lwjgl.bgfx.BGFX.bgfx_create_index_buffer
import org.lwjgl.bgfx.BGFX.bgfx_create_program
import org.lwjgl.bgfx.BGFX.bgfx_create_shader
import org.lwjgl.bgfx.BGFX.bgfx_create_vertex_buffer
import org.lwjgl.bgfx.BGFX.bgfx_destroy_index_buffer
import org.lwjgl.bgfx.BGFX.bgfx_destroy_program
import org.lwjgl.bgfx.BGFX.bgfx_destroy_vertex_buffer
import org.lwjgl.bgfx.BGFX.bgfx_frame
import org.lwjgl.bgfx.BGFX.bgfx_get_renderer_type
import org.lwjgl.bgfx.BGFX.bgfx_init
import org.lwjgl.bgfx.BGFX.bgfx_init_ctor
import org.lwjgl.bgfx.BGFX.bgfx_set_index_buffer
import org.lwjgl.bgfx.BGFX.bgfx_set_state
import org.lwjgl.bgfx.BGFX.bgfx_set_vertex_buffer
import org.lwjgl.bgfx.BGFX.bgfx_set_view_clear
import org.lwjgl.bgfx.BGFX.bgfx_set_view_rect
import org.lwjgl.bgfx.BGFX.bgfx_shutdown
import org.lwjgl.bgfx.BGFX.bgfx_submit
import org.lwjgl.bgfx.BGFX.bgfx_touch
import org.lwjgl.bgfx.BGFX.bgfx_vertex_layout_add
import org.lwjgl.bgfx.BGFX.bgfx_vertex_layout_begin
import org.lwjgl.bgfx.BGFX.bgfx_vertex_layout_end
import org.lwjgl.bgfx.BGFXInit
import org.lwjgl.bgfx.BGFXPlatform.bgfx_render_frame
import org.lwjgl.bgfx.BGFXVertexLayout
import org.lwjgl.glfw.GLFW.GLFW_CLIENT_API
import org.lwjgl.glfw.GLFW.GLFW_FALSE
import org.lwjgl.glfw.GLFW.GLFW_NO_API
import org.lwjgl.glfw.GLFW.GLFW_RESIZABLE
import org.lwjgl.glfw.GLFW.GLFW_TRUE
import org.lwjgl.glfw.GLFW.GLFW_VISIBLE
import org.lwjgl.glfw.GLFW.glfwCreateWindow
import org.lwjgl.glfw.GLFW.glfwDestroyWindow
import org.lwjgl.glfw.GLFW.glfwInit
import org.lwjgl.glfw.GLFW.glfwPollEvents
import org.lwjgl.glfw.GLFW.glfwTerminate
import org.lwjgl.glfw.GLFW.glfwWindowHint
import org.lwjgl.glfw.GLFW.glfwWindowShouldClose
import org.lwjgl.glfw.GLFWNativeCocoa.glfwGetCocoaWindow
import org.lwjgl.glfw.GLFWNativeWin32.glfwGetWin32Window
import org.lwjgl.glfw.GLFWNativeX11.glfwGetX11Display
import org.lwjgl.glfw.GLFWNativeX11.glfwGetX11Window
import org.lwjgl.glfw.GLFWNativeWayland.glfwGetWaylandDisplay
import org.lwjgl.glfw.GLFWNativeWayland.glfwGetWaylandWindow
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.Platform

fun main() =
  application {
    val width = 1280
    val height = 720
    install(
      acquire = {
        check(glfwInit()) { "Unable to initialize GLFW" }
      },
      release = { _, _ ->
        glfwTerminate()
      },
    )

    glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE)
    glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE)
    glfwWindowHint(GLFW_CLIENT_API, GLFW_NO_API)
    val window =
      install(
        acquire = {
          glfwCreateWindow(width, height, "", 0, 0)
        },
        release = { window, _ ->
          glfwDestroyWindow(window)
        },
      )
    check(window != 0L) { "Failed to create GLFW window" }
    logger.debug { "Created GLFW window" }

    bgfx_render_frame(0)

    MemoryStack.stackPush().use { stack ->
      val init = BGFXInit.calloc(stack)
      bgfx_init_ctor(init)
      init.type(BGFX_RENDERER_TYPE_COUNT)
      val resolution = init.resolution()
      resolution.width(1280)
      resolution.height(720)
      resolution.reset(BGFX_RESET_VSYNC)

      val platformData = init.platformData()
      platformData.context(0L)
      platformData.backBuffer(0L)
      platformData.backBufferDS(0L)
      platformData.type(BGFX_NATIVE_WINDOW_HANDLE_TYPE_DEFAULT)

      val nativeWindowHandle =
        when (Platform.get()) {
          Platform.MACOSX -> {
            glfwGetCocoaWindow(window)
          }

          Platform.WINDOWS -> {
            glfwGetWin32Window(window)
          }

          Platform.LINUX -> {
            val waylandDisplay = glfwGetWaylandDisplay()
            if (waylandDisplay != 0L) {
              platformData.type(BGFX_NATIVE_WINDOW_HANDLE_TYPE_WAYLAND)
              platformData.ndt(waylandDisplay)
              glfwGetWaylandWindow(window)
            } else {
              platformData.ndt(glfwGetX11Display())
              glfwGetX11Window(window)
            }
          }

          else -> {
            error("Unsupported platform: ${Platform.get()}")
          }
        }
      platformData.nwh(nativeWindowHandle)

      install(
        acquire = {
          check(bgfx_init(init)) { "Failed to initialize bgfx" }
        },
        release = { _, _ ->
          bgfx_shutdown()
        },
      )
    }
    logger.debug { "Initialized bgfx" }

    bgfx_set_view_rect(0, 0, 0, width, height)
    val background = Color.gray80.toRgba8888()
    bgfx_set_view_clear(
      0,
      BGFX_CLEAR_COLOR or BGFX_CLEAR_DEPTH,
      background,
      1.0f,
      0,
    )

    val (vBuf, iBuf, sh) =
      MemoryStack.stackPush().use { stack ->
        val layout = BGFXVertexLayout.calloc(stack)
        bgfx_vertex_layout_begin(layout, BGFX_RENDERER_TYPE_NOOP)
        bgfx_vertex_layout_add(
          layout,
          BGFX_ATTRIB_POSITION,
          3,
          BGFX_ATTRIB_TYPE_FLOAT,
          false, // normalized
          false, // asInt
        )
        bgfx_vertex_layout_add(
          layout,
          BGFX_ATTRIB_COLOR0,
          4,
          BGFX_ATTRIB_TYPE_UINT8,
          true, // normalized
          false, // asInt
        )
        bgfx_vertex_layout_end(layout)

        val vertexBuffer =
          run {
            val vertexStride = 16
            val vertexCount = 3
            val vertexBuffer =
              BufferUtils.createByteBuffer(vertexCount * vertexStride)

            fun putVertex(
              x: Float,
              y: Float,
              z: Float,
              color: Int,
            ) {
              vertexBuffer.putFloat(x)
              vertexBuffer.putFloat(y)
              vertexBuffer.putFloat(z)
              vertexBuffer.putInt(color)
            }

            val cyanAbgr = Color.cyan.toAbgr8888()
            val magentaAbgr = Color.magenta.toAbgr8888()
            val yellowAbgr = Color.yellow.toAbgr8888()

            putVertex(-0.5f, -0.5f, 0f, cyanAbgr)
            putVertex(0.5f, -0.5f, 0f, magentaAbgr)
            putVertex(0f, 0.5f, 0f, yellowAbgr)

            vertexBuffer.flip()

            val vertexMemory =
              checkNotNull(bgfx_copy(vertexBuffer)) {
                "Failed to copy vertex buffer data"
              }
            install(
              acquire = {
                bgfx_create_vertex_buffer(
                  vertexMemory,
                  layout,
                  BGFX_BUFFER_NONE,
                )
              },
              release = { buf, _ ->
                bgfx_destroy_vertex_buffer(buf)
              },
            )
          }
        val indexBuffer =
          run {
            val indexBuffer = BufferUtils.createShortBuffer(3)
            indexBuffer.put(0)
            indexBuffer.put(1)
            indexBuffer.put(2)
            indexBuffer.flip()
            val indexMemory =
              checkNotNull(bgfx_copy(indexBuffer)) {
                "Failed to copy index buffer data"
              }
            install(
              acquire = {
                bgfx_create_index_buffer(
                  indexMemory,
                  BGFX_BUFFER_NONE,
                )
              },
              release = { buf, _ ->
                bgfx_destroy_index_buffer(buf)
              },
            )
          }
        val shader =
          run {
            fun loadShader(path: String): Short {
              val bytes =
                object {}
                  .javaClass
                  .getResourceAsStream(path)
                  ?.readBytes()
                  ?: error("Failed to load shader: $path")
              val buffer = BufferUtils.createByteBuffer(bytes.size)
              buffer.put(bytes)
              buffer.flip()
              val shaderMemory =
                checkNotNull(bgfx_copy(buffer)) {
                  "Failed to copy shader data: $path"
                }
              return bgfx_create_shader(shaderMemory)
            }

            val rendererType = bgfx_get_renderer_type()
            val subdir =
              when (rendererType) {
                BGFX_RENDERER_TYPE_VULKAN -> "spirv"
                BGFX_RENDERER_TYPE_METAL -> "metal"
                else -> "opengl"
              }
            install(
              acquire = {
                bgfx_create_program(
                  loadShader("shaders/$subdir/vs_triangle.bin"),
                  loadShader("shaders/$subdir/fs_triangle.bin"),
                  true,
                )
              },
              release = { program, _ ->
                bgfx_destroy_program(program)
              },
            )
          }
        Triple(vertexBuffer, indexBuffer, shader)
      }
    logger.debug { "Prepared buffers and shader" }

    while (!glfwWindowShouldClose(window)) {
      glfwPollEvents()

      bgfx_touch(0)

      bgfx_set_vertex_buffer(0, vBuf, 0, 3)
      bgfx_set_index_buffer(iBuf, 0, 3)
      bgfx_set_state(BGFX_STATE_DEFAULT, 0)

      bgfx_submit(0, sh, 0, BGFX_DISCARD_NONE.toInt())

      bgfx_frame(false)
    }
  }

private val logger = KotlinLogging.logger("Main")
