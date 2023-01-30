package edu.uncc.midtermapp.models;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Question implements Serializable {
    public String question_id, question_text, question_url;
    public ArrayList<Answer> answers;

    public Question(String question_id, String question_text, String question_url, ArrayList<Answer> answers) {
        this.question_id = question_id;
        this.question_text = question_text;
        this.question_url = question_url;
        this.answers = answers;
    }

    public Question(JSONObject json) {

    }

    public Question() {
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getQuestion_text() {
        return question_text;
    }

    public void setQuestion_text(String question_text) {
        this.question_text = question_text;
    }

    public String getQuestion_url() {
        return question_url;
    }

    public void setQuestion_url(String question_url) {
        this.question_url = question_url;
    }

    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<Answer> answers) {
        this.answers = answers;
    }
}
