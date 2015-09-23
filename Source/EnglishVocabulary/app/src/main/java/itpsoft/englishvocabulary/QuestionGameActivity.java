package itpsoft.englishvocabulary;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import itpsoft.englishvocabulary.models.Question;
import itpsoft.englishvocabulary.ultils.SPUtil;

/**
 * Created by Thanh Tu on 7/23/2015.
 */
public class QuestionGameActivity extends Activity implements View.OnClickListener {
    private LinearLayout layoutStart, layoutGaming, layoutGameOver;
    private TextView english, vietnamese, score, playScore, hightScore, gameTitle;
    private Button play, replay, yes, no;
    private ProgressBar time;
    private int timeReply = 2 * 1000, timeRunner = 0, onepercent = timeReply / 100, pQuestion = 0, pScore = 0, hScore;
    private ImageView imgIcBack;
    private Handler handler;
    private Runnable runnable;
    private Question question;
    private ArrayList<Question> questions;
    private MediaPlayer mediaPlayer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_game);
        imgIcBack = (ImageView) findViewById(R.id.drawer_indicator);
        imgIcBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
            }
        });
        initView();
        setListener();
        working();
    }

    private void initView() {
        layoutStart = (LinearLayout) findViewById(R.id.start);
        layoutGaming = (LinearLayout) findViewById(R.id.game);
        layoutGameOver = (LinearLayout) findViewById(R.id.gameOver);
        english = (TextView) findViewById(R.id.english);
        vietnamese = (TextView) findViewById(R.id.vietnamese);
        score = (TextView) findViewById(R.id.score);
        playScore = (TextView) findViewById(R.id.play_score);
        hightScore = (TextView) findViewById(R.id.hight_score);
        gameTitle = (TextView) findViewById(R.id.game_title);
        play = (Button) findViewById(R.id.play);
        replay = (Button) findViewById(R.id.replay);
        yes = (Button) findViewById(R.id.yes);
        no = (Button) findViewById(R.id.no);
        time = (ProgressBar) findViewById(R.id.time);
    }

    private void setListener() {
        play.setOnClickListener(this);
        replay.setOnClickListener(this);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
    }

    private void working() {
        question = new Question(QuestionGameActivity.this);
    }

    private void newQuestion() {
        if (questions.size() - 1 > pQuestion) {
            if (handler != null && runnable != null)
                handler.removeCallbacks(runnable);
            time.setMax(100);
            time.setProgress(100);
            timeRunner = timeReply;
            handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    timeRunner = timeRunner - 50;
                    int p = timeRunner / onepercent;
                    time.setProgress(p);
                    if (timeRunner > 0)
                        handler.postDelayed(this, 50);
                    else
                        gameOver();
                }
            };
            handler.postDelayed(runnable, 50);
        } else {
            gameOver();
            gameTitle.setTextColor(getResources().getColor(R.color.finish));
            gameTitle.setText(getResources().getString(R.string.finish));
            Toast.makeText(QuestionGameActivity.this, getResources().getString(R.string.het_cau_hoi), Toast.LENGTH_SHORT).show();
        }

    }

    private void playGame() {
        questions = question.getQuestions();
        if (questions.size() > 0) {
            layoutStart.setVisibility(View.GONE);
            layoutGaming.setVisibility(View.VISIBLE);
            layoutGameOver.setVisibility(View.GONE);
            pQuestion = 0;
            pScore = 0;
            playScore.setText(Integer.toString(pScore));
            english.setText(questions.get(pQuestion).getEnglish());
            vietnamese.setText(questions.get(pQuestion).getVietnamese());
        } else {
            Toast.makeText(QuestionGameActivity.this, getResources().getString(R.string.kho_tu_rong), Toast.LENGTH_SHORT).show();
        }
        newQuestion();
    }

    private void gameOver() {
        if (handler != null && runnable != null)
            handler.removeCallbacks(runnable);

        hScore = SPUtil.instance(QuestionGameActivity.this).get("HIGHT_SCORE_GAME_QUESTTION", 0);
        if (pScore > hScore) {
            gameTitle.setTextColor(getResources().getColor(R.color.newscore));
            gameTitle.setText(getResources().getString(R.string.new_score));
            hightScore.setText(Integer.toString(pScore));
            SPUtil.instance(QuestionGameActivity.this).set("HIGHT_SCORE_GAME_QUESTTION", pScore);
        } else {
            gameTitle.setTextColor(getResources().getColor(R.color.gameover));
            gameTitle.setText(getResources().getString(R.string.gameover));
            hightScore.setText(Integer.toString(hScore));
        }
        score.setText(Integer.toString(pScore));
        layoutStart.setVisibility(View.GONE);
        layoutGaming.setVisibility(View.GONE);
        layoutGameOver.setVisibility(View.VISIBLE);
    }

    private void answer(boolean answer) {
        if (question.answer(pQuestion, answer)) {
            startSoundTrue();
            pScore = pScore + 1;
            playScore.setText(Integer.toString(pScore));
            newQuestion();
            pQuestion = pQuestion + 1;
            if (pQuestion < questions.size()) {
                english.setText(questions.get(pQuestion).getEnglish());
                vietnamese.setText(questions.get(pQuestion).getVietnamese());
            }
        } else {
            startSoundFalse();
            gameOver();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.play:
                playGame();
                break;
            case R.id.yes:
                answer(true);
                break;
            case R.id.no:
                answer(false);
                break;
            case R.id.replay:
                playGame();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
    }

    public void startSoundTrue() {
        mediaPlayer = MediaPlayer.create(QuestionGameActivity.this, R.raw.dock);
        mediaPlayer.start();
    }

    public void startSoundFalse() {
        mediaPlayer = MediaPlayer.create(QuestionGameActivity.this, R.raw.error);
        mediaPlayer.start();
    }
}
