package bph.data;

import java.util.Objects;

public class PlayerScore implements Comparable<PlayerScore> {
    private String name;
    private int score;

    public PlayerScore(String name, int score) {
        this.name = Objects.requireNonNull(name);
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

	@Override
	public int compareTo(PlayerScore o) {
		Objects.requireNonNull(o);
		return this.score-o.score;
	}
	
	public void setScore(int score) {
        this.score = score;
    }
}
