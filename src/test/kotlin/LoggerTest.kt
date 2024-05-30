import channels.DummyReader
import channels.DummyWriter
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.text.toByteArray
import technology.idlab.bridge.MemoryReader
import technology.idlab.bridge.MemoryWriter
import technology.idlab.runner.Pipeline

/** Simple pipeline containing the processor. */
private const val pipeline =
    """
@prefix jvm: <https://w3id.org/conn/jvm#>.
@prefix xsd: <http://www.w3.org/2001/XMLSchema#>.
@prefix owl: <http://www.w3.org/2002/07/owl#>.

<reader> a jvm:MemoryChannelReader.
<writer> a jvm:MemoryChannelWriter.

# Define a filter processor.
[]
  a jvm:Logger;
  jvm:input <reader>;
  jvm:output <writer>.
"""

private val pipelineFile = File.createTempFile("pipeline", "ttl").apply { writeText(pipeline) }

class LoggerTest {
  @Test
  fun definition() {
    // Initialize pipeline.
    val pipeline = Pipeline(pipelineFile)

    // Extract processor.
    val processors = pipeline.processors
    assertEquals(1, processors.size)
    val logger = processors[0] as Logger

    // Check arguments.
    assert(logger.getArgument<Any>("input") is MemoryReader)
    assert(logger.getArgument<Any>("output") is MemoryWriter)
  }

  @Test
  fun functional() {
    val reader = DummyReader(arrayOf("Hello, world!".toByteArray()))
    val writer = DummyWriter()
    val args: Map<String, Any> = mapOf("input" to reader, "output" to writer)

    // Overwrite logging.
    val out = System.out
    val stream = ByteArrayOutputStream()
    val printStream = PrintStream(stream)
    System.setOut(printStream)

    // Run the logger. It closes automatically when all data is read.
    val logger = Logger(args)
    logger.exec()

    // Reset output.
    System.setOut(out)
    print(stream.toString())

    // Check the output.
    assertEquals(1, writer.getValues().size)
    assertEquals("Hello, world!", writer.getValues()[0].decodeToString())
    assertContains(stream.toString(), "Logger::exec")
    assertContains(stream.toString(), "Hello, world!")
  }
}
