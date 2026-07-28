package io.github.asmolenkov.tennismatchscoreboard.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;



@Entity
@Table(name = "MATCHES")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Check(constraints = "PLAYER1_ID != PLAYER2_ID")
@Check(constraints = "PLAYER1_ID = WINNER_ID OR PLAYER2_ID = WINNER_ID")
public class Match {
    public Match(Player playerOne, Player playerSecond, Player winner) {
        this.playerOne = playerOne;
        this.playerSecond = playerSecond;
        this.winner = winner;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLAYER1_ID", referencedColumnName = "ID", nullable = false)
    private Player playerOne;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLAYER2_ID", referencedColumnName = "ID", nullable = false)
    private Player playerSecond;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WINNER_ID", referencedColumnName = "ID", nullable = false)
    private Player winner;
}
