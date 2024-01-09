package warmingUp.antifragile.post.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long writerId;
    @Column(nullable = false)
    private Long carId;
    private String title;
    private String contents;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column
    private Long mgp;
    @Column
    private Long safe;
    @Column
    private Long space;
    @Column
    private Long design;
    @Column
    private Long fun;
    private String purpose;

    @Column
    private Long commentCount;

}