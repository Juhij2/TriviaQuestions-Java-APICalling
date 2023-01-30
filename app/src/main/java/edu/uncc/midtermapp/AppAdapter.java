package edu.uncc.midtermapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import edu.uncc.midtermapp.models.Answer;

public class AppAdapter extends ArrayAdapter<Answer> {

    public AppAdapter(@NonNull Context context, int resource, @NonNull List<Answer> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_answer_row_layout, parent,false);
        }

        Answer answer = getItem(position);
        TextView answerTextView = convertView.findViewById(R.id.answerTextView);
        answerTextView.setText(answer.answer_text);


        return convertView;
    }
}
