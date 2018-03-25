package com.bitbus.fiftyeight.baseball.matchup;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
@EqualsAndHashCode(exclude = "player")
@ToString(exclude = "player")
@NoArgsConstructor
public class BaseballPlayerPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int playerPositionId;

    @Enumerated(EnumType.STRING)
    private BaseballPosition position;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private BaseballPlayer player;

    public BaseballPlayerPosition(BaseballPosition position, BaseballPlayer player) {
        this.position = position;
        this.player = player;
    }

}
