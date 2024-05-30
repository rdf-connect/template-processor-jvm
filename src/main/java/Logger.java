import java.util.Map;
import org.jetbrains.annotations.NotNull;
import technology.idlab.bridge.Reader;
import technology.idlab.bridge.Writer;
import technology.idlab.runner.Processor;
import technology.idlab.runner.ProcessorDefinition;

/** Simple processor which logs the input data and passes it to the output. */
@ProcessorDefinition(resource = "/logger.ttl")
public class Logger extends Processor {
  private final Reader input;
  private final Writer output;

  public Logger(@NotNull Map<String, ?> args) {
    super(args);
    this.input = getArgument("input");
    this.output = getArgument("output");
  }

  @Override
  public void exec() {
    while (true) {
      // Read data from input.
      Reader.Result result = input.readSync();

      // Check if channel has been closed.
      if (result.isClosed()) {
        break;
      }

      // Get raw data as byte array.
      byte[] data = result.getValue();

      // Convert byte array to string and log.
      String message = new String(data);
      log.info(message);

      // Pass back into the output.
      output.pushSync(data);
    }

    // Close the output channel.
    output.close();
  }
}
