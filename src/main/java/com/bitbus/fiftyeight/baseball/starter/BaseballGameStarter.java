package com.bitbus.fiftyeight.baseball.starter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.bitbus.fiftyeight.baseball.matchup.BaseballMatchup;
import com.bitbus.fiftyeight.baseball.player.BaseballPlayer;
import com.bitbus.fiftyeight.baseball.player.BaseballPosition;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@EqualsAndHashCode(exclude = {"matchup", "player"})
@ToString(exclude = {"matchup", "player"})
@NoArgsConstructor
public class BaseballGameStarter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int starterId;

    @ManyToOne
    @JoinColumn(name = "matchup_id")
    private BaseballMatchup matchup;

    @Enumerated(EnumType.STRING)
    private BaseballPosition position;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private BaseballPlayer player;

    private Integer battingPosition;

    public BaseballGameStarter(BaseballMatchup matchup, BaseballPlayer player, BaseballPosition position,
            Integer battingPosition) {
        this.matchup = matchup;
        this.player = player;
        this.position = position;
        this.battingPosition = battingPosition;
    }
}
