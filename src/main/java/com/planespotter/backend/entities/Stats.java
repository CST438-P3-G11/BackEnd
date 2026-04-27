package com.planespotter.backend.entities;

import jakarta.persistence.*;


@Entity
@Table(name = "stats")
public class Stats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long stats_id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private int gamesPlayed;
    private int correctGuesses;
    private int winningStreak;
    private int bestStreak;

    public Stats() {}
    /**
     * Constructor for the Stats class
     * @param user user instance of the User
     * @param gamesPlayed total games played of the User
     * @param winningStreak current winning streak of the User
     * @param bestStreak best winning streak of the User
     * @param correctGuesses total correct guesses of the User
     */

    public Stats(User user, int gamesPlayed, int winningStreak, int bestStreak, int correctGuesses){
        this.user = user;
        this.gamesPlayed = gamesPlayed;
        this.winningStreak = winningStreak;
        this.bestStreak = bestStreak;
        this.correctGuesses = correctGuesses;
    }

    /**
     * Getter for best streak
     * @return best streak of a User, an int
     */
    public int getBestStreak() {
        return bestStreak;
    }

    /**
     * Getter for total correct guesses
     * @return total correct guesses of a User, an int
     */
    public int getCorrectGuesses() {
        return correctGuesses;
    }

    /**
     * Getter for current win streak
     * @return current win streak of a User, an int
     */
    public int getWinningStreak() {
        return winningStreak;
    }
    /**
     * Getter for total games played
     * @return total games played of a User, an int
     */
    public int getGamesPlayed() {
        return gamesPlayed;
    }

    /**
     * Getter for the associated User
     * @return the User this stats record belongs to
     */
    public User getUser() {
        return user;
    }
    /**
     * Getter for the Stats row ID
     * @return the database ID of this stats record, a long
     */
    public long getStats_id() {
        return stats_id;
    }
    /**
     * Sets the associated User for this stats record
     * @param user the User this stats record belongs to
     */
    public void setUser(User user){
        this.user = user;
    }

    /**
     * Sets a BestStreak
     * @param bestStreak the number of games a user won in their best streak
     */
    public void setBestStreak(int bestStreak) {
        this.bestStreak = bestStreak;
    }

    /**
     * Set a user's Correct Guesses
     * @param correctGuesses the number of correct guesses a user has made
     */
    public void setCorrectGuesses(int correctGuesses) {
        this.correctGuesses = correctGuesses;
    }

    /**
     * Set a user's Games Played
     * @param gamesPlayed the number of games a user has played in total
     */
    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }
    /**
     * Set a user's Current Winning Streak
     * @param winningStreak the number of games currently won in a row
     */
    public void setWinningStreak(int winningStreak) {
        this.winningStreak = winningStreak;
    }
}
