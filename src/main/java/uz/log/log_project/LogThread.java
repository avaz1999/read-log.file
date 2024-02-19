package uz.log.log_project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@NoArgsConstructor
@Data
@Component
public class LogThread implements Runnable{
    private File files;
    private LogServiceImpl service;

    public LogThread(File file, LogServiceImpl service) {
        this.files = file;
        this.service = service;
    }

    @Override
    public void run() {
        try {
            service.threadPool1(files);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
