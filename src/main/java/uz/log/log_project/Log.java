package uz.log.log_project;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "log",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"pan","client"})
})
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    private String methodName;
    @Column(name = "pan")
    @org.hibernate.annotations.Index(name = "pan")
    private String pan;
    @org.hibernate.annotations.Index(name = "client")
    @Column(name = "client")
    private String client;

    public Log(String methodName, String pan, String client) {
        this.methodName = methodName;
        this.pan = pan;
        this.client = client;
    }
}
