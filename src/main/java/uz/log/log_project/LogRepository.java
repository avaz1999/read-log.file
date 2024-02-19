package uz.log.log_project;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log,Long> {
    boolean existsByClient(String client);
    boolean existsByPan(String pan);
    boolean existsByClientAndPan(String client, String pan);
}
