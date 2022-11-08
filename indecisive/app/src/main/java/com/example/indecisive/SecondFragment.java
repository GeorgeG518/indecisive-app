package com.example.indecisive;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.indecisive.databinding.FragmentSecondBinding;

import java.util.Random;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    Integer num1;
    Integer num2;
    Integer num3;

    boolean fieldChecked = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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

        binding.numberEnterbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                fieldChecked = CheckValues();

                if(CheckValues())
                {

                    findNumbers();

                    Random random = new Random();
                    binding.finalRandomNumbers.setText(" ");

                    for (int i = 0; i < num3; i++) {
                       int x = random.nextInt(num2 - num1 + 1) + num1;
                       binding.finalRandomNumbers.append(x + " ");
                    }
                }
            }
        });

    }

    public void findNumbers() {

        num1 = Integer.parseInt(String.valueOf(binding.NumberOneTextBox.getText()));
        num2 = Integer.parseInt(String.valueOf(binding.TextNumber2Box.getText()));
        num3 = Integer.parseInt(String.valueOf(binding.NumberAmountTextBox.getText()));
    }


    private boolean CheckValues()
    {

        if (!binding.NumberOneTextBox.getText().toString().matches("[0-9]+"))
        {
            binding.NumberOneTextBox.setError("This field is required");
            return false;
        }
        if (!binding.TextNumber2Box.getText().toString().matches("[0-9]+"))
        {
            binding.TextNumber2Box.setError("This field is required");
            return false;
        }
        if (!binding.NumberAmountTextBox.getText().toString().matches("[0-9]+"))
        {
            binding.NumberAmountTextBox.setError("This field is required");
            return false;
        }
        else
            return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}