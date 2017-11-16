package com.insomniac.geoquiz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class QuizActivity extends AppCompatActivity {

    private Button mFalseButton,mTrueButton,mNextButton,mCheatButton;
    private TextView mQuestionTextView,mScoreText,mYourScore;

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String EXTRA_ANSWER_IS_TRUE = "com.insomniac.geoquiz.answer_is_true";
    private static final int REQUEST_CODE_CHEAT = 0;
    private boolean mIsCheater = false;
    private int correctAnswer = 0;
    private int incorrectAnswer = 0;
    private static final String EXTRA_CHEATER = "com.insomniac.geoquiz.ischeater";

    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_ocean,true),
            new Question(R.string.question_mid_east,false),
            new Question(R.string.question_africa,false),
            new Question(R.string.question_america,true),
            new Question(R.string.question_asia,true),
    };

    private int mQuestionIndex = 0;

    private void updateQuestion(){
        Log.d(TAG,"updateQuestion");
        int question = mQuestionBank[mQuestionIndex].getQuestionId();
        mQuestionTextView.setText(question);
    }


    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mQuestionIndex].isAnswerTrue();
        int messageResId = 0;
        Log.d(TAG,"checkAnswer");
        if (mIsCheater) {
            messageResId = R.string.judgment_toast;
            Log.d(TAG,"checkAn");
        }
        else {
            Log.d(TAG,"checkA");
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
                correctAnswer++;
            }
            else {
                messageResId = R.string.incorrect_toast;
                incorrectAnswer++;
            }
        }
        Toast.makeText(getApplicationContext(), messageResId, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        mScoreText = (TextView) findViewById(R.id.score_text);
        mYourScore = (TextView) findViewById(R.id.your_sore);
        mYourScore.setVisibility(View.INVISIBLE);
        mScoreText.setVisibility(View.INVISIBLE);
        Log.d(TAG,"oncreate");
        if(savedInstanceState != null){
            Log.d(TAG,"saveInstance");
            mQuestionIndex = savedInstanceState.getInt(KEY_INDEX,0);
            mIsCheater = savedInstanceState.getBoolean(EXTRA_CHEATER,false);
        }

        mQuestionTextView = (TextView) findViewById(R.id.question_text);
        int question = mQuestionBank[mQuestionIndex].getQuestionId();
        mQuestionTextView.setText(question);

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
                Log.d(TAG,"false");
            }
        });

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);
                Log.d(TAG,"true");
            }
        });

        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mQuestionIndex < (mQuestionBank.length - 1)) {
                    mQuestionIndex++;
                    updateQuestion();
                    Log.d(TAG,"next");
                    mIsCheater = false;
                }
                else{
                    Toast.makeText(getApplicationContext(),"Game ends",Toast.LENGTH_SHORT).show();
                    Log.d(TAG,"Game ends");
                    mYourScore.setVisibility(View.VISIBLE);
                    mScoreText.setVisibility(View.VISIBLE);
                    mScoreText.setText(correctAnswer + " " + incorrectAnswer);
                }
            }
        });

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean answerIsTrue = mQuestionBank[mQuestionIndex].isAnswerTrue();
                Log.d(TAG,"cheat");
                Intent i = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(i,REQUEST_CODE_CHEAT);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstance){
        super.onSaveInstanceState(savedInstance);
        Log.d(TAG,"onSaveInstanceState");
        savedInstance.putInt(KEY_INDEX,mQuestionIndex);
        savedInstance.putBoolean(EXTRA_CHEATER,mIsCheater);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        Log.d(TAG,"onActivityResult");
        if(resultCode != Activity.RESULT_OK)
            return;
        if(requestCode == REQUEST_CODE_CHEAT){
            if(data == null)
                return;
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }
}
