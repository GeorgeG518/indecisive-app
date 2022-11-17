package com.example.indecisive;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.indecisive.databinding.FragmentMagicBallBinding;

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link magicBall#newInstance} factory method to
 * create an instance of this fragment.
 */
public class magicBall extends Fragment {
    //private ImageView ballImage;
    private TextView answerText;

    private String[] answersArray = {"It is certain", "It is decidedly so", "Without a doubt", "Yes definitely", "You may rely on it", "As I see it, yes",
            "Most likely", "Outlook good", "Yes", "Signs point to yes", "Reply hazy try again", "Ask again later",
            "Better not tell you now", "Cannot predict now", "Concentrate and ask again", "Don't count on it",
            "My reply is no", "My sources say no", "Outlook not so good", "Very doubtful"};

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentMagicBallBinding binding;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public magicBall() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment magicBall.
     */
    // TODO: Rename and change types and number of parameters
    public static magicBall newInstance(String param1, String param2) {
        magicBall fragment = new magicBall();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ballImage.findViewById(R.id.imageView3);
        //answerText.findViewById(R.id.textView16);
        //ballImage.setOnClickListener((View.OnClickListener) this);
        //binding.ballImage !!!
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /*public void onClick(View view){
        switch(view.getId())
        {
            case R.id.imageView3:
                int rand = new Random().nextInt(answersArray.length);
                answerText.setText(answersArray[rand]);
                break;
        }
    }*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMagicBallBinding.inflate(inflater, container, false);
        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_magic_ball, container, false);
    }
}