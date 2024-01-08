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

    @ColumnDefault("5L")
    private Long mgp;
    @ColumnDefault("5L")
    private Long safe;
    @ColumnDefault("5L")
    private Long space;
    @ColumnDefault("5L")
    private Long design;
    @ColumnDefault("5L")
    private Long fun;

    private String purpose;

    @ColumnDefault("0L")
    private Long commentCount;



}