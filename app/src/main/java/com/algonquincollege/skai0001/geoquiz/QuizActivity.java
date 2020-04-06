/*
 *   Author : Hasan Skaiky
 *   Book : Android Programming: The Big Nerd Ranch Guide (3rd Edition)
 *   page #20
 * */


package com.algonquincollege.skai0001.geoquiz;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class QuizActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;
    private Button mCheatButton;
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_IS_CHEATER_ARRAY = "is_cheater_array";
    private static final int REQUEST_CODE_CHEAT = 0;
    private int answerCounter = 0;
    private int mCurrentIndex = 0;


    // Link questions from model to view
    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };

    // Save "Cheater Status" of questions.  All values will default to false
    private boolean[] mCheaterStatus = new boolean[mQuestionBank.length];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        // Check to see if we are actually just redrawing after a state change
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mCheaterStatus = savedInstanceState.getBooleanArray(KEY_IS_CHEATER_ARRAY);
        }

        // Link True Button to view and add on click listener
        mTrueButton = findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        // Link False Button to view and add on click listener
        mFalseButton = findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);

            }
        });

        // Link Next Button to view and add on click listener
        mNextButton = findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCurrentIndex == mQuestionBank.length - 1) {
                    Toast.makeText(QuizActivity.this, "Your score is : " + answerCounter * 10 + "%", Toast.LENGTH_SHORT).show();
                    mNextButton.setEnabled(false);
                    mPrevButton.setEnabled(false);
                    return;
                }
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        // Link Previous Button to view and add on click listener
        mPrevButton = findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentIndex != 0) {
                    mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                    updateQuestion();
                }
                Toast.makeText(QuizActivity.this, R.string.first_question, Toast.LENGTH_SHORT).show();
            }
        });

        // Link Cheat Button to view and add on click listener
        mCheatButton = findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(QuizActivity.this, CheatActivity.class);
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                intent.putExtra(CheatActivity.EXTRA_ANSWER_IS_TRUE, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });

        mQuestionTextView = findViewById(R.id.question_text_view);
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    //check user has cheated
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( data == null) {
            return;
        }
        mCheaterStatus[mCurrentIndex] = data.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN, false);
    }


    @Override
    public void onStart() {
        super.onStart();
        // Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        // Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        //Log.d(TAG, "onPause() called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBooleanArray(KEY_IS_CHEATER_ARRAY, mCheaterStatus);
    }


    @Override
    public void onStop() {
        super.onStop();
        // Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Log.d(TAG, "onDestroy() called");
    }

    //update next question
    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        mTrueButton.setEnabled(true);
        mFalseButton.setEnabled(true);
    }

    // check answer
    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId;

        if (mCheaterStatus[mCurrentIndex]) {
            messageResId = R.string.judgment_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {

                messageResId = R.string.correct_toast;
                answerCounter++;
                mFalseButton.setEnabled(false);
                mTrueButton.setEnabled(false);
            } else {

                messageResId = R.string.incorrect_toast;
                mTrueButton.setEnabled(false);
                mFalseButton.setEnabled(false);
            }
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

}