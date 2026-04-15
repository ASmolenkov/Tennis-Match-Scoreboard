package io.github.asmolenkov.tennismatchscoreboard.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "MATCHES")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne()
    @JoinColumn(name = "PLAYER1", referencedColumnName = "ID")
    private Player playerOne;

    @ManyToOne
    @JoinColumn(name = "PLAYER2", referencedColumnName = "ID")
    private Player playerSecond;

    @ManyToOne
    @JoinColumn(name = "WINNER", referencedColumnName = "ID")
    private Player winner;
}
