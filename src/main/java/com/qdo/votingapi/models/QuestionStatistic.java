package com.qdo.votingapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QuestionStatistic {
    private final Question question;
    private final int countYes;
    private final int countNo;
    private final int total;

    private static int share = 1000;

    /**
     * Question Statistic constructor
     *
     * @param question Question
     * @param countYes Count of Yes vote
     * @param countNo  Count of No vote
     */
    public QuestionStatistic(
            @JsonProperty("question") Question question,
            @JsonProperty("countYes") int countYes,
            @JsonProperty("countNo") int countNo) {
        this.question = question;
        this.countYes = countYes;
        this.countNo = countNo;
        this.total = countYes + countNo;
    }

    /**
     * Get Question
     *
     * @return Question
     */
    public Question getQuestion() {
        return question;
    }

    /**
     * Get number of Yes vote
     *
     * @return number of Yes vote
     */
    public int getCountYes() {
        return countYes;
    }

    /**
     * Get number of No vote
     *
     * @return number of No vote
     */
    public int getCountNo() {
        return countNo;
    }

    /**
     * Get total vote count for this question
     *
     * @return total vote of this question
     */
    public int getTotal() {
        return total;
    }

    /**
     * Get percentage of Yes vote
     *
     * @return percentage of Yes vote
     */
    @JsonProperty("percentageYes")
    public String getPercentageYes() {
        if (total == 0) return null;
        return formatFloat((double) countYes / total * 100);
    }

    /**
     * Get percentage of No vote
     *
     * @return percentage of No vote
     */
    @JsonProperty("percentageNo")
    public String getPercentageNo() {
        if (total == 0) return null;
        return formatFloat((double) countNo / total * 100);
    }

    /**
     * Format number to percentage format
     *
     * @param num A number needs formatting
     * @return a string in percentage format
     */
    private String formatFloat(double num) {
        return String.format("%d", Math.round(num)) + "%";
    }
}
