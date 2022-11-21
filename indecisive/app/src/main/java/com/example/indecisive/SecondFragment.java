package com.example.indecisive;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.indecisive.databinding.FragmentSecondBinding;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    int firstNumber;
    int secondNumber;
    int numberAmount;

    Button Submit;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);}
     //   setContentView(R.layout.fragment_second);

     //  firstNumberInput = (EditText) findViewById(R.id.firstNumberInput);
      //  secondNumberInput = (EditText) findViewById(R.id.secondNumberInput);
        //numberAmountInput = (EditText) findViewById(R.id.numberAmountInput);

      //  submitButton = (Button) findViewById(R.id.submitButton);
        //submitButton.setOnClickListener(new View.OnClickListener()

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }




}