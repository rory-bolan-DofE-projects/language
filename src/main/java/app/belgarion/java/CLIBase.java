package app.belgarion.java;


import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Command(
        name = "mycli",
        mixinStandardHelpOptions = true,
        version = "1.0",
        description = "A simple Java CLI using Picocli"
)
public class CLIBase implements Runnable {

    @Parameters(index = "0", description = "file to execute", defaultValue = "help")
    private String file;
    @Override
    public void run() {
            System.out.println("Running " + file);
        try {
            Response resp = ReadAndTokenize.tokenize(new File(new URI(file)));

        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new CLIBase()).execute(args);
        System.exit(exitCode);
    }
}
