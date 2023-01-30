package edu.uncc.midtermapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import edu.uncc.midtermapp.databinding.FragmentTriviaBinding;
import edu.uncc.midtermapp.models.Answer;
import edu.uncc.midtermapp.models.Question;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TriviaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TriviaFragment extends Fragment {

    FragmentTriviaBinding binding;

    private final OkHttpClient client = new OkHttpClient();
    private ArrayList<Question> mTriviaQuestions = new ArrayList<Question>();
    private
    AppAdapter adapter;
    int count = 0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String Q_KEY = "KEY";

    public TriviaFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TriviaFragment newInstance(ArrayList<Question> mTriviaQuestions) {
        TriviaFragment fragment = new TriviaFragment();
        Bundle args = new Bundle();
        args.putSerializable(Q_KEY, mTriviaQuestions);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTriviaQuestions = (ArrayList<Question>) getArguments().getSerializable(Q_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTriviaBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.textViewTriviaTopStatus.setText("Question 1 of "+ mTriviaQuestions.size());

        if(mTriviaQuestions.get(0).question_url != null){
            Picasso.get().load(mTriviaQuestions.get(0).question_url)
                    .error(R.drawable.ic_launcher_background)
                    .into(binding.imageViewQuestion);
        }



        binding.textViewTriviaQuestion.setText(mTriviaQuestions.get(0).question_text);


        adapter = new AppAdapter(getContext(), R.layout.fragment_answer_row_layout,mTriviaQuestions.get(0).answers);

        binding.listViewAnswers.setAdapter(adapter);
        binding.listViewAnswers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Answer answer = mTriviaQuestions.get(0).answers.get(position);
                String answerID = answer.answer_id;
                String questionID = mTriviaQuestions.get(0).question_id;

                FormBody formBody = new FormBody.Builder()
                        .add("question_id", questionID)
                        .add("answer_id", answerID)
                        .build();


                Request request = new Request.Builder()
                        .url("https://www.theappsdr.com/api/trivia/checkAnswer")
                        .post(formBody)
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

                                Boolean isCorrectAnswer = json.getBoolean("isCorrectAnswer");
                                if (isCorrectAnswer){
                                    count += 1;
                                }
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), isCorrectAnswer.toString(), Toast.LENGTH_SHORT).show();

                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            ResponseBody responseBody = response.body();
                            Log.d("demo", "onResponse: "+ responseBody.string());
                        }
                    }
                });

            }
        });


    }
}