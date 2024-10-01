package p5.backend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import p5.backend.service.DockerService;

import java.io.IOException;

@RestController
@CrossOrigin("http://localhost:5173")
public class DockerController {

    @GetMapping("/")
    public String hello(@RequestParam(value = "input", defaultValue = "") String input) throws IOException {


        DockerService dockerService = new DockerService();


        return dockerService.runMultipleCommands(input);
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello world";
    }
}
