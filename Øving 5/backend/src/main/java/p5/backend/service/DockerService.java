package p5.backend.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

@Service
public class DockerService {


    public String runMultipleCommands(String code) throws IOException {


        String[] list = {"docker", "run", "--rm", "python:3.9-alpine", "python", "-c", code};

        ProcessBuilder build = new ProcessBuilder(list);

        build.redirectErrorStream(true);

        Process process = build.start();


        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        StringBuilder output = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }
        String result = output.toString();

        try {
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.out.println("Error: " + result);
            }
        } catch (InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("Result: " + result);
        return result;
    }





    public void runCommand() throws IOException, InterruptedException, ExecutionException, TimeoutException {

        ProcessBuilder builder = new ProcessBuilder();

        //builder.command("cmd.exe", "/c", "dir");
        builder.command("cmd.exe", "/c", "dir", "dir");

        builder.directory(new File(System.getProperty("user.home")));
        Process process = builder.start();
        StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), System.out::println);
        Future<?> future = Executors.newSingleThreadExecutor().submit(streamGobbler);
        int exitCode = process.waitFor();
        assert exitCode == 0;
        future.get(10, TimeUnit.SECONDS);
    }

    private static class StreamGobbler implements Runnable {
        private InputStream inputStream;
        private Consumer<String> consumer;

        public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream)).lines()
                    .forEach(consumer);
        }
    }
}
