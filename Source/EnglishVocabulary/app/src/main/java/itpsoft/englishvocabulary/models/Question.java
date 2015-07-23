package itpsoft.englishvocabulary.models;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

import itpsoft.englishvocabulary.ultils.Log;

/**
 * Created by Thanh Tu on 7/23/2015.
 */
public class Question implements Serializable{
    private String english, vietnamese;
    private boolean answer;
    private ArrayList<Question> questions;

    public Question() {
        getQuestions();
    }

    public Question(String english, String vietnamese, boolean answer) {
        this.english = english;
        this.vietnamese = vietnamese;
        this.answer = answer;
    }

    public String getEnglish() {
        return english;
    }

    public String getVietnamese() {
        return vietnamese;
    }

    public boolean getAnswer() {
        return answer;
    }

    public ArrayList<Question> getQuestions(){
        questions = new ArrayList<Question>();
        questions.add(new Question("vn", "viet nam", true));
        questions.add(new Question("vn", "en", false));
        questions.add(new Question("vn", "ab", false));
        questions.add(new Question("en", "english", true));
        questions.add(new Question("en", "viet nam", false));
        return questions;
    }

    public boolean answer(int position, boolean answer){
        Question question = questions.get(position);
        if(answer == question.getAnswer())
            return true;
        else
            return false;
    }
}
