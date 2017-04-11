package com.folken.quizz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

/*
 * Created by Marco Gullo on 12/03/2017.
 *
 * Quizz is an app that read a JSON file stored in RAW folder, display a set of question and calculate user score
 * JSON can contain up to 12 question: 4 checkbox styled (3-answer), 4 radiobutton styled (3-answer), 4 free answer styled (1-answer)
 *
 * each question contains a two digit number (field "type"):
 * first digit is question type(1 Radiobutton, 2 Checkbox, 3 EditText), second digit is question order
 *
 * Quizz detects question style and choose the appropriate layout among 12 layouts (inflate the layout in the main ViewGroup container)
 *
 * When user is ready and press SUBMIT btnSubmit the app iterate across the view and check answers against JSON file
 * calculating the overall score
 *
 * In order to iterate among views TextViews with visibility GONE is used to store "question unique id" and "question type"
 *
 * A submit button presents a toast with the overall score to the users
 * A reset button clear fields.
 *
 * Pros:
 * - it's possible to create a JSON in order to change questions!
 * Cons:
 * - you have only three question-templates
 * - you have to use 3 answer for each multiple-answer-question
 * - each layout item must have a unique id, so I had to create 4 similar layout first of all for each question-template
 *
 * Note: JSON file reading and Scroll position stuff during orientation change grabbed from NET
 * Quiz from: http://www.softschools.com/quizzes/math/4th_grade_geometry/quiz3506.html
 */

public class MainActivity extends AppCompatActivity {

    private ViewGroup view;
    private String jsonString;
    private ScrollView scrollView;
    private int totalQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateUI();
    }

    /* manage scroll position on orientation change */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray("SCROLL_POSITION",
                new int[]{scrollView.getScrollX(), scrollView.getScrollY()});
    }


    /* manage scroll position on orientation change */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        final int[] position = savedInstanceState.getIntArray("SCROLL_POSITION");
        if (position != null)
            scrollView.post(new Runnable() {
                public void run() {
                    scrollView.scrollTo(position[0], position[1]);
                }
            });
    }


    /* Read the JSON file and inflate layouts for each question */
    public void populateUI() {

        scrollView = (ScrollView) findViewById(R.id.SvId);

        /* manage JSON file located in RAW folder*/
        InputStream is = getResources().openRawResource(R.raw.question);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        jsonString = writer.toString();
        /* parsing JSON and writing to UI */
        JSONArray Jarray = null;
        try {
            Jarray = (new JSONObject(jsonString)).getJSONArray("JSON");

            totalQuestions = Jarray.length();

            JSONObject object;

            view = (ViewGroup) findViewById(R.id.activity_main);

            for (int i = 0; i < Jarray.length(); i++) {
                object = Jarray.getJSONObject(i);

                String id = object.getString("id");
                String type = object.getString("type");
                String question = object.getString("question");
                switch (type) {
                    case "11":
                        /* inflate, add View, populate fields */
                        View questionView = getLayoutInflater().inflate(R.layout.question11, view, false);
                        view.addView(questionView);

                        JSONArray jarrayTemp = object.getJSONArray("a1");
                        String answer1 = jarrayTemp.getString(0);

                        jarrayTemp = object.getJSONArray("a2");
                        String answer2 = jarrayTemp.getString(0);

                        jarrayTemp = object.getJSONArray("a3");
                        String answer3 = jarrayTemp.getString(0);

                        TextView questionId = (TextView) questionView.findViewById(R.id.questionId_11);
                        TextView questionType = (TextView) questionView.findViewById(R.id.questionType_11);
                        TextView questionText = (TextView) questionView.findViewById(R.id.questionText_11);
                        RadioButton radioButton1 = (RadioButton) questionView.findViewById(R.id.radioButton1_11);
                        RadioButton radioButton2 = (RadioButton) questionView.findViewById(R.id.radioButton2_11);
                        RadioButton radioButton3 = (RadioButton) questionView.findViewById(R.id.radioButton3_11);

                        questionId.setText(id);
                        questionType.setText(type);
                        questionText.setText("Q-" + id + " " + question);
                        radioButton1.setText(answer1);
                        radioButton2.setText(answer2);
                        radioButton3.setText(answer3);

                        break;

                    case "12":
                        /* inflate, add View, populate fields */
                        questionView = getLayoutInflater().inflate(R.layout.question12, view, false);
                        view.addView(questionView);

                        jarrayTemp = object.getJSONArray("a1");
                        answer1 = jarrayTemp.getString(0);

                        jarrayTemp = object.getJSONArray("a2");
                        answer2 = jarrayTemp.getString(0);

                        jarrayTemp = object.getJSONArray("a3");
                        answer3 = jarrayTemp.getString(0);

                        questionId = (TextView) questionView.findViewById(R.id.questionId_12);
                        questionType = (TextView) questionView.findViewById(R.id.questionType_12);
                        questionText = (TextView) questionView.findViewById(R.id.questionText_12);
                        radioButton1 = (RadioButton) questionView.findViewById(R.id.radioButton1_12);
                        radioButton2 = (RadioButton) questionView.findViewById(R.id.radioButton2_12);
                        radioButton3 = (RadioButton) questionView.findViewById(R.id.radioButton3_12);

                        questionId.setText(id);
                        questionType.setText(type);
                        questionText.setText("Q-" + id + " " + question);
                        radioButton1.setText(answer1);
                        radioButton2.setText(answer2);
                        radioButton3.setText(answer3);

                        break;

                    case "13":
                        /* inflate, add View, populate fields */
                        questionView = getLayoutInflater().inflate(R.layout.question13, view, false);
                        view.addView(questionView);

                        jarrayTemp = object.getJSONArray("a1");
                        answer1 = jarrayTemp.getString(0);

                        jarrayTemp = object.getJSONArray("a2");
                        answer2 = jarrayTemp.getString(0);

                        jarrayTemp = object.getJSONArray("a3");
                        answer3 = jarrayTemp.getString(0);

                        questionId = (TextView) questionView.findViewById(R.id.questionId_13);
                        questionType = (TextView) questionView.findViewById(R.id.questionType_13);
                        questionText = (TextView) questionView.findViewById(R.id.questionText_13);
                        radioButton1 = (RadioButton) questionView.findViewById(R.id.radioButton1_13);
                        radioButton2 = (RadioButton) questionView.findViewById(R.id.radioButton2_13);
                        radioButton3 = (RadioButton) questionView.findViewById(R.id.radioButton3_13);

                        questionId.setText(id);
                        questionType.setText(type);
                        questionText.setText("Q-" + id + " " + question);
                        radioButton1.setText(answer1);
                        radioButton2.setText(answer2);
                        radioButton3.setText(answer3);

                        break;

                    case "21":
                        /* inflate, add View, populate fields */
                        questionView = getLayoutInflater().inflate(R.layout.question21, view, false);
                        view.addView(questionView);

                        jarrayTemp = object.getJSONArray("a1");
                        answer1 = jarrayTemp.getString(0);

                        jarrayTemp = object.getJSONArray("a2");
                        answer2 = jarrayTemp.getString(0);

                        jarrayTemp = object.getJSONArray("a3");
                        answer3 = jarrayTemp.getString(0);

                        questionId = (TextView) questionView.findViewById(R.id.questionId_21);
                        questionType = (TextView) questionView.findViewById(R.id.questionType_21);
                        questionText = (TextView) questionView.findViewById(R.id.questionText_21);
                        CheckBox checkBox1 = (CheckBox) questionView.findViewById(R.id.checkBox1_21);
                        CheckBox checkBox2 = (CheckBox) questionView.findViewById(R.id.checkBox2_21);
                        CheckBox checkBox3 = (CheckBox) questionView.findViewById(R.id.checkBox3_21);

                        questionId.setText(id);

                        questionType.setText(type);
                        questionText.setText("Q-" + id + " " + question);
                        checkBox1.setText(answer1);
                        checkBox2.setText(answer2);
                        checkBox3.setText(answer3);

                        break;

                    case "22":
                        /* inflate, add View, populate fields */
                        questionView = getLayoutInflater().inflate(R.layout.question22, view, false);
                        view.addView(questionView);

                        jarrayTemp = object.getJSONArray("a1");
                        answer1 = jarrayTemp.getString(0);

                        jarrayTemp = object.getJSONArray("a2");
                        answer2 = jarrayTemp.getString(0);

                        jarrayTemp = object.getJSONArray("a3");
                        answer3 = jarrayTemp.getString(0);

                        questionId = (TextView) questionView.findViewById(R.id.questionId_22);
                        questionType = (TextView) questionView.findViewById(R.id.questionType_22);
                        questionText = (TextView) questionView.findViewById(R.id.questionText_22);
                        checkBox1 = (CheckBox) questionView.findViewById(R.id.checkBox1_22);
                        checkBox2 = (CheckBox) questionView.findViewById(R.id.checkBox2_22);
                        checkBox3 = (CheckBox) questionView.findViewById(R.id.checkBox3_22);

                        questionId.setText(id);
                        questionType.setText(type);
                        questionText.setText("Q-" + id + " " + question);
                        checkBox1.setText(answer1);
                        checkBox2.setText(answer2);
                        checkBox3.setText(answer3);

                        break;

                    case "23":
                        /* inflate, add View, populate fields */
                        questionView = getLayoutInflater().inflate(R.layout.question23, view, false);
                        view.addView(questionView);

                        jarrayTemp = object.getJSONArray("a1");
                        answer1 = jarrayTemp.getString(0);

                        jarrayTemp = object.getJSONArray("a2");
                        answer2 = jarrayTemp.getString(0);

                        jarrayTemp = object.getJSONArray("a3");
                        answer3 = jarrayTemp.getString(0);

                        questionId = (TextView) questionView.findViewById(R.id.questionId_23);
                        questionType = (TextView) questionView.findViewById(R.id.questionType_23);
                        questionText = (TextView) questionView.findViewById(R.id.questionText_23);
                        checkBox1 = (CheckBox) questionView.findViewById(R.id.checkBox1_23);
                        checkBox2 = (CheckBox) questionView.findViewById(R.id.checkBox2_23);
                        checkBox3 = (CheckBox) questionView.findViewById(R.id.checkBox3_23);

                        questionId.setText(id);
                        questionType.setText(type);
                        questionText.setText("Q-" + id + " " + question);
                        checkBox1.setText(answer1);
                        checkBox2.setText(answer2);
                        checkBox3.setText(answer3);

                        break;

                    case "31":
                        /* inflate, add View, populate fields */
                        questionView = getLayoutInflater().inflate(R.layout.question31, view, false);
                        view.addView(questionView);

                        questionId = (TextView) questionView.findViewById(R.id.questionId_31);
                        questionType = (TextView) questionView.findViewById(R.id.questionType_31);
                        questionText = (TextView) questionView.findViewById(R.id.questionText_31);

                        questionId.setText(id);
                        questionType.setText(type);
                        questionText.setText("Q-" + id + " " + question);

                        break;

                    case "32":
                        /* inflate, add View, populate fields */
                        questionView = getLayoutInflater().inflate(R.layout.question32, view, false);
                        view.addView(questionView);

                        questionId = (TextView) questionView.findViewById(R.id.questionId_32);
                        questionType = (TextView) questionView.findViewById(R.id.questionType_32);
                        questionText = (TextView) questionView.findViewById(R.id.questionText_32);

                        questionId.setText(id);
                        questionType.setText(type);
                        questionText.setText("Q-" + id + " " + question);

                        break;

                    case "33":
                        /* inflate, add View, populate fields */
                        questionView = getLayoutInflater().inflate(R.layout.question33, view, false);
                        view.addView(questionView);

                        questionId = (TextView) questionView.findViewById(R.id.questionId_33);
                        questionType = (TextView) questionView.findViewById(R.id.questionType_33);
                        questionText = (TextView) questionView.findViewById(R.id.questionText_33);

                        questionId.setText(id);
                        questionType.setText(type);
                        questionText.setText("Q-" + id + " " + question);
                        break;
                }
            }

            /* prepare button */
            Button btnSubmit = (Button) findViewById(R.id.btn_submit);
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    check();
                }
            });

            /* prepare button */
            Button btnReset = (Button) findViewById(R.id.btn_reset);
            btnReset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reset();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /* Traverse main LinearLayout, and check answers against JSON files
     * "answerGrade" is a temporary variable related to a single answer of a multiple response question
     * "questionGrade" is the score related to the question
     * "totalGrade" is the overall test score
     * A toast is shown to the users  */

    public void check() {

        int totalGrade = 0; // overall test score

        /* each child view is a LinearLayout */
        for (int i = 0; i < view.getChildCount(); i++) {

            boolean questionGrade = false;                      // single question score

            ViewGroup v = (ViewGroup) view.getChildAt(i);       // this is a LinearLayout
            View vId = v.getChildAt(0);                         // the first view is "vId" and it contains the question id
            String id = (String) ((TextView) vId).getText();

            /* parsing JSON and checking answer */
            JSONArray Jarray = null;
            try {
                Jarray = (new JSONObject(jsonString)).getJSONArray("JSON");
                JSONObject object = Jarray.getJSONObject(i);

                String jsonId = object.getString("id");

                if (id.equals(jsonId)) {
                    String jsonType = object.getString("type");
                    int answerGrade = 0;    // single answer check

                    switch (jsonType) {
                        case "11":
                            JSONArray jarrayTemp = object.getJSONArray("a1");
                            boolean jsonCheck = jarrayTemp.getBoolean(1);
                            /* item compare */
                            RadioButton radioButton = (RadioButton) v.findViewById(R.id.radioButton1_11);
                            if (radioButton.isChecked() == jsonCheck) {
                                answerGrade++;
                            }

                            jarrayTemp = object.getJSONArray("a2");
                            jsonCheck = jarrayTemp.getBoolean(1);
                            /* item compare */
                            radioButton = (RadioButton) v.findViewById(R.id.radioButton2_11);
                            if (radioButton.isChecked() == jsonCheck) {
                                answerGrade++;
                            }

                            jarrayTemp = object.getJSONArray("a3");
                            jsonCheck = jarrayTemp.getBoolean(1);
                            /* item compare */
                            radioButton = (RadioButton) v.findViewById(R.id.radioButton3_11);
                            if (radioButton.isChecked() == jsonCheck) {
                                answerGrade++;
                            }
                            if (answerGrade == 3) {
                                questionGrade = true;
                            }
                            break;
                        case "12":
                            jarrayTemp = object.getJSONArray("a1");
                            jsonCheck = jarrayTemp.getBoolean(1);
                            /* item compare */
                            radioButton = (RadioButton) v.findViewById(R.id.radioButton1_12);
                            if (radioButton.isChecked() == jsonCheck) {
                                answerGrade++;
                            }

                            jarrayTemp = object.getJSONArray("a2");
                            jsonCheck = jarrayTemp.getBoolean(1);
                            /* item compare */
                            radioButton = (RadioButton) v.findViewById(R.id.radioButton2_12);
                            if (radioButton.isChecked() == jsonCheck) {
                                answerGrade++;
                            }

                            jarrayTemp = object.getJSONArray("a3");
                            jsonCheck = jarrayTemp.getBoolean(1);
                            /* item compare */
                            radioButton = (RadioButton) v.findViewById(R.id.radioButton3_12);
                            if (radioButton.isChecked() == jsonCheck) {
                                answerGrade++;
                            }
                            if (answerGrade == 3) {
                                questionGrade = true;
                            }
                            break;
                        case "13":
                            jarrayTemp = object.getJSONArray("a1");
                            jsonCheck = jarrayTemp.getBoolean(1);
                            /* item compare */
                            radioButton = (RadioButton) v.findViewById(R.id.radioButton1_13);
                            if (radioButton.isChecked() == jsonCheck) {
                                answerGrade++;
                            }

                            jarrayTemp = object.getJSONArray("a2");
                            jsonCheck = jarrayTemp.getBoolean(1);
                            /* item compare */
                            radioButton = (RadioButton) v.findViewById(R.id.radioButton2_13);
                            if (radioButton.isChecked() == jsonCheck) {
                                answerGrade++;
                            }

                            jarrayTemp = object.getJSONArray("a3");
                            jsonCheck = jarrayTemp.getBoolean(1);
                            /* item compare */
                            radioButton = (RadioButton) v.findViewById(R.id.radioButton3_13);
                            if (radioButton.isChecked() == jsonCheck) {
                                answerGrade++;
                            }
                            if (answerGrade == 3) {
                                questionGrade = true;
                            }
                            break;
                        case "21":
                            jarrayTemp = object.getJSONArray("a1");
                            jsonCheck = jarrayTemp.getBoolean(1);
                            /* item compare */
                            CheckBox checkBox = (CheckBox) v.findViewById(R.id.checkBox1_21);
                            if (checkBox.isChecked() == jsonCheck) {
                                answerGrade++;
                            }

                            jarrayTemp = object.getJSONArray("a2");
                            jsonCheck = jarrayTemp.getBoolean(1);
                            /* item compare */
                            checkBox = (CheckBox) v.findViewById(R.id.checkBox2_21);
                            if (checkBox.isChecked() == jsonCheck) {
                                answerGrade++;
                            }

                            jarrayTemp = object.getJSONArray("a3");
                            jsonCheck = jarrayTemp.getBoolean(1);
                            /* item compare */
                            checkBox = (CheckBox) v.findViewById(R.id.checkBox3_21);
                            if (checkBox.isChecked() == jsonCheck) {
                                answerGrade++;
                            }
                            if (answerGrade == 3) {
                                questionGrade = true;
                            }
                            break;
                        case "22":
                            jarrayTemp = object.getJSONArray("a1");
                            jsonCheck = jarrayTemp.getBoolean(1);
                            /* item compare */
                            checkBox = (CheckBox) v.findViewById(R.id.checkBox1_22);
                            if (checkBox.isChecked() == jsonCheck) {
                                answerGrade++;
                            }

                            jarrayTemp = object.getJSONArray("a2");
                            jsonCheck = jarrayTemp.getBoolean(1);
                            /* item compare */
                            checkBox = (CheckBox) v.findViewById(R.id.checkBox2_22);
                            if (checkBox.isChecked() == jsonCheck) {
                                answerGrade++;
                            }

                            jarrayTemp = object.getJSONArray("a3");
                            jsonCheck = jarrayTemp.getBoolean(1);
                            /* item compare */
                            checkBox = (CheckBox) v.findViewById(R.id.checkBox3_22);
                            if (checkBox.isChecked() == jsonCheck) {
                                answerGrade++;
                            }
                            if (answerGrade == 3) {
                                questionGrade = true;
                            }
                            break;
                        case "23":
                            jarrayTemp = object.getJSONArray("a1");
                            jsonCheck = jarrayTemp.getBoolean(1);
                            /* item compare */
                            checkBox = (CheckBox) v.findViewById(R.id.checkBox1_23);
                            if (checkBox.isChecked() == jsonCheck) {
                                answerGrade++;
                            }

                            jarrayTemp = object.getJSONArray("a2");
                            jsonCheck = jarrayTemp.getBoolean(1);
                            /* item compare */
                            checkBox = (CheckBox) v.findViewById(R.id.checkBox2_23);
                            if (checkBox.isChecked() == jsonCheck) {
                                answerGrade++;
                            }

                            jarrayTemp = object.getJSONArray("a3");
                            jsonCheck = jarrayTemp.getBoolean(1);
                            /* item compare */
                            checkBox = (CheckBox) v.findViewById(R.id.checkBox3_23);
                            if (checkBox.isChecked() == jsonCheck) {
                                answerGrade++;
                            }
                            if (answerGrade == 3) {
                                questionGrade = true;
                            }
                            break;
                        case "31":
                            String jsonAnswer = object.getString("a1");
                            /* item compare */
                            EditText editText = (EditText) v.findViewById(R.id.answerText_31);
                            String userAnswer = editText.getText().toString();
                            if (userAnswer.equals(jsonAnswer)) {
                                answerGrade++;
                            }
                            if (answerGrade == 1) {
                                questionGrade = true;
                            }
                            break;
                        case "32":
                            jsonAnswer = object.getString("a1");
                            /* item compare */
                            editText = (EditText) v.findViewById(R.id.answerText_32);
                            userAnswer = editText.getText().toString();
                            if (userAnswer.equals(jsonAnswer)) {
                                answerGrade++;
                            }
                            if (answerGrade == 1) {
                                questionGrade = true;
                            }
                            break;
                        case "33":
                            jsonAnswer = object.getString("a1");
                            /* item compare */
                            editText = (EditText) v.findViewById(R.id.answerText_33);
                            userAnswer = editText.getText().toString();
                            if (userAnswer.equals(jsonAnswer)) {
                                answerGrade++;
                            }
                            if (answerGrade == 1) {
                                questionGrade = true;
                            }
                            break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            /* the answers to the question are right, increase total score */
            if (questionGrade) {
                totalGrade++;
            }

        }

        /* Display overall score */
        String toastMessage;
        if (totalGrade == 0) {
            toastMessage = "No luck!";
        } else {
            toastMessage = "Total score: " + totalGrade + "/" + totalQuestions;
        }
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();

    }


    /* Traverse main LinearLayout resetting each view
     * at the end smooth scrolls to the top of the view */

    public void reset() {

        for (int i = 0; i < view.getChildCount(); i++) {

            ViewGroup v = (ViewGroup) view.getChildAt(i);           // this is a LinearLayout
            View vType = v.getChildAt(1);                           // the second view is "questionType" and it contains the question type
            String type = (String) ((TextView) vType).getText();

            RadioButton radioButton;
            CheckBox checkBox;
            EditText editText;


            switch (type) {
                case "11":
                            /* reset item */
                    radioButton = (RadioButton) v.findViewById(R.id.radioButton1_11);
                    radioButton.setChecked(false);
                    radioButton = (RadioButton) v.findViewById(R.id.radioButton2_11);
                    radioButton.setChecked(false);
                    radioButton = (RadioButton) v.findViewById(R.id.radioButton3_11);
                    radioButton.setChecked(false);
                    break;
                case "12":
                            /* reset item */
                    radioButton = (RadioButton) v.findViewById(R.id.radioButton1_12);
                    radioButton.setChecked(false);
                    radioButton = (RadioButton) v.findViewById(R.id.radioButton2_12);
                    radioButton.setChecked(false);
                    radioButton = (RadioButton) v.findViewById(R.id.radioButton3_12);
                    radioButton.setChecked(false);
                    break;
                case "13":
                            /* reset item */
                    radioButton = (RadioButton) v.findViewById(R.id.radioButton1_13);
                    radioButton.setChecked(false);
                    radioButton = (RadioButton) v.findViewById(R.id.radioButton2_13);
                    radioButton.setChecked(false);
                    radioButton = (RadioButton) v.findViewById(R.id.radioButton3_13);
                    radioButton.setChecked(false);
                    break;
                case "21":
                            /* reset item */
                    checkBox = (CheckBox) v.findViewById(R.id.checkBox1_21);
                    checkBox.setChecked(false);
                    checkBox = (CheckBox) v.findViewById(R.id.checkBox2_21);
                    checkBox.setChecked(false);
                    checkBox = (CheckBox) v.findViewById(R.id.checkBox3_21);
                    checkBox.setChecked(false);
                    break;
                case "22":
                            /* reset item */
                    checkBox = (CheckBox) v.findViewById(R.id.checkBox1_22);
                    checkBox.setChecked(false);
                    checkBox = (CheckBox) v.findViewById(R.id.checkBox2_22);
                    checkBox.setChecked(false);
                    checkBox = (CheckBox) v.findViewById(R.id.checkBox3_22);
                    checkBox.setChecked(false);
                    break;
                case "23":
                           /* reset item */
                    checkBox = (CheckBox) v.findViewById(R.id.checkBox1_23);
                    checkBox.setChecked(false);
                    checkBox = (CheckBox) v.findViewById(R.id.checkBox2_23);
                    checkBox.setChecked(false);
                    checkBox = (CheckBox) v.findViewById(R.id.checkBox3_23);
                    checkBox.setChecked(false);
                    break;
                case "31":
                           /* reset item */
                    editText = (EditText) v.findViewById(R.id.answerText_31);
                    editText.setText("");
                    break;
                case "32":
                           /* reset item */
                    editText = (EditText) v.findViewById(R.id.answerText_32);
                    editText.setText("");
                    break;
                case "33":
                           /* reset item */
                    editText = (EditText) v.findViewById(R.id.answerText_33);
                    editText.setText("");
                    break;
            }
        }

        scrollView.post(new Runnable() {
            public void run() {
                scrollView.smoothScrollTo(0, 0);
            }
        });

    }
}
