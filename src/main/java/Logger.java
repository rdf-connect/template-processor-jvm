import technology.idlab.bridge.Reader;
import technology.idlab.bridge.Writer;
import technology.idlab.runner.Processor;
import technology.idlab.runner.ProcessorDefinition;

import java.util.Map;

/**
 * Simple processor which logs the input data and passes it to the output.
 */
@ProcessorDefinition(resource = "/logger.ttl")
public class Logger extends Processor {
    private final Reader reader;
    private final Writer writer;

    Logger(Map<String, Object> args) {
        super(args);
        this.reader = getArgument("reader");
        this.writer = getArgument("writer");
    }

    @Override
    public void exec() {
        while (true) {
            // Read data from input.
            Reader.Result result = reader.readSync();

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
            writer.pushSync(data);
        }

        // Close the output channel.
        writer.close();
    }
}
