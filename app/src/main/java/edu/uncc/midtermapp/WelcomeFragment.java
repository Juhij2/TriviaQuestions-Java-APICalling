package edu.uncc.midtermapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import edu.uncc.midtermapp.databinding.FragmentWelcomeBinding;
import edu.uncc.midtermapp.models.Answer;
import edu.uncc.midtermapp.models.Question;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WelcomeFragment extends Fragment {

    FragmentWelcomeBinding binding;

    private final OkHttpClient client = new OkHttpClient();

    private ArrayList<Question> mTriviaQuestions = new ArrayList<Question>();

    public WelcomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Request request = new Request.Builder()
                .url("https://www.theappsdr.com/api/trivia")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject json = new JSONObject(response.body().string());

                        JSONArray questionsArray = json.getJSONArray("questions");
                        for (int i = 0; i < questionsArray.length(); i++) {
                            JSONObject questionJsonObject = questionsArray.getJSONObject(i);
                            Question question = new Question();
                            question.setQuestion_id(questionJsonObject.getString("question_id"));
                            question.setQuestion_text(questionJsonObject.getString("question_text"));
                            question.setQuestion_url(questionJsonObject.getString("question_url"));
                            JSONArray answersArray = questionJsonObject.getJSONArray("answers");
                            ArrayList<Answer> answerArrayList = new ArrayList<>();
                            for (int j = 0; j < answersArray.length(); j++) {
                                JSONObject answerJsonObject = answersArray.getJSONObject(j);
                                Answer answer = new Answer();
                                answer.setAnswer_id(answerJsonObject.getString("answer_id"));
                                answer.setAnswer_text(answerJsonObject.getString("answer_text"));
                                answerArrayList.add(answer);
                            }
                            question.setAnswers(answerArrayList);
                            mTriviaQuestions.add(question);
                            Log.d("TAG", "onResponse: " + json);
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                binding.buttonStart.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        welcomeFragmentListener.setQuestions(mTriviaQuestions);
                                    }
                                });
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWelcomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        welcomeFragmentListener = (WelcomeFragmentListener) context;
    }

    WelcomeFragmentListener welcomeFragmentListener;

    public interface WelcomeFragmentListener {
        void setQuestions(ArrayList<Question> mTriviaQuestions);
    }



}