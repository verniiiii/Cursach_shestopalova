package com.example.cursach_shestopalova;

public class Faq {
    private int id;
    private String question;
    private String answer;


    public Faq(int id, String question, String answer) {
        this.id = id;
        this.question = question;
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }


}

