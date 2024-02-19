package uz.log.log_project;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class LogProjectApplication implements ApplicationRunner {
    private final LogServiceImpl service;

    public LogProjectApplication(LogServiceImpl service) {
        this.service = service;
    }

    public static void main(String[] args) {
        SpringApplication.run(LogProjectApplication.class, args);
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
//        System.out.println("Tizim ishga tushdi");
        long start = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        File directory = new File("LogFile");
        List<File> fileList = new ArrayList<>();
        for (File file : service.fetchFilesRecursively(directory, fileList)) {
            CompletableFuture.runAsync(() -> {
                try {
                    service.threadPool1(file);
                }catch (IOException e){
                    System.err.println(e.getMessage());
                }
            }, executorService);
        }
//        service.insertLogs();
        long end = System.currentTimeMillis();
//        executorService.shutdown();
        System.out.println("Ketgan vaqt: " + (end - start));
    }
}
