package uz.log.log_project;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.zip.GZIPInputStream;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {
    private final LogJDBC logJDBC;

    public void insertLogs() {
        try {
            File directory = new File("./LogFile");
            List<File> fileList = new ArrayList<>();
            List<File> inerFileList = fetchFilesRecursively(directory, fileList);
            for (int i = 0; i < Objects.requireNonNull(fileList).size(); i++) {
                long start = System.currentTimeMillis();
                File file = inerFileList.get(i);
                Set<Log> logList = getDataFromFile(file);
                logJDBC.insertToTables(logList);
                long end = System.currentTimeMillis();
                System.out.println((i + 1) + " " + file.getName() + " " + (end - start) + " vaqt oralig'ida Fayl o'qildi");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void threadPool1(File file) throws IOException {
        long start = System.currentTimeMillis();
        Set<Log> logList = getDataFromFile(file);
        long end = System.currentTimeMillis();
        logJDBC.insertToTables(logList);
        System.out.println(file.getName() + " " + (end - start) + " vaqt oralig'ida Fayl o'qildi");
    }
    public List<File> fetchFilesRecursively(File directory, List<File> fileList) {
        File[] files = directory.listFiles();
        assert files != null;
        for (File file : files) {
            if (file.isDirectory()) {
                fetchFilesRecursively(file, fileList);
            } else {
                fileList.add(file);
            }
        }
        return fileList;
    }

    private Set<Log> getDataFromFile(File file) throws IOException {
        Set<Log> logList = new HashSet<>();
        try (GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(file));
             BufferedReader reader = new BufferedReader(new InputStreamReader(gzip))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("GetCard")) {
                    String client = maskClient(line);
                    String pan = maskPan(line);
                    logList.add(new Log("GetCard",pan,client));
                    }
                }
            }
            return logList;
        }



    public String maskClient(String logLine) {
        String[] parts = logLine.split(" ");
        String clientPrefix = "CLIENT:";
        for (String part : parts) {
            if (part.startsWith(clientPrefix)) {
                return part.substring(clientPrefix.length());
            }
        }
        return "Client topilmadi";
    }

    public String maskPan(String logLine) {
        String[] parts = logLine.split(" ");
        String panPrefix = "PAN:";
        for (String part : parts) {
            if (part.startsWith(panPrefix)) {
                String pan = part.substring(panPrefix.length());
                if (pan.length() < 6) return "Pan size error";
                else return pan;
            }
        }
        return "PAN topilmadi";
    }
}

