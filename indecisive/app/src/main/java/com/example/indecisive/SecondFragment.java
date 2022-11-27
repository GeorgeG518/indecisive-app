package com.example.indecisive;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.indecisive.databinding.FragmentSecondBinding;

import java.util.Random;
//Author: Sarra Zerairia fragment
//Class and Section: 3033-001
//Date: November 30th, 2022
//Instructors Name: Professor Rafet Al-Tobasei
//this fragment takes in a range of numbers, asks the user
//how many numbers they wnat chose, then inputs all the numbers they want

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    //variables for the numbers they enter
    Integer num1;
    Integer num2;
    Integer num3;

    //variable that holds the boolean to check a field was entered for validation
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

        //creates the binding function so i can connect the variables from the
        //xml frgament to this fragment and take in the input the user entered
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //a button that returns them to the previous home page function
        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        //This button is for when the user entered all the numbers, it then  checks if each
        //field had something entered, and if so it takes in how many numbers they
        //wanted and between what range. Then it prints out the ranodm numbers
        binding.numberEnterbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                //goes to field checked function to check there is not empty field
                fieldChecked = CheckValues();

                //if all fields have something entered
                if(CheckValues())
                {
                    //go to the findNumbers function, and receive the input of the user
                    findNumbers();

                    //create a random object to use
                    Random random = new Random();

                    //makes the text view that shows the final numbers chosen a scroll view
                    //because a user might need alot of numbers and this makes it scrollable so
                    //all the numbers can be viewed
                    binding.finalRandomNumbers.setMovementMethod(new ScrollingMovementMethod());

                    //set the text to an empty string so it can be reset every time
                    //with different variables
                    binding.finalRandomNumbers.setText(" ");




                    //this for loop uses the number range the user inputed to find the
                    //random numbers chosen
                    for (int i = 0; i < num3; i++) {
                        //add +1 so it includes the number they want it to end out
                       int x = random.nextInt(num2 - num1 + 1) + num1;
                       //prints out the number plus a space, and uses append, so it
                        //adds on and not just replaces
                       binding.finalRandomNumbers.append(x + " ");
                    }
                }
            }
        });

    }

    //This function gets the number inputted in the fields provided to the user
    //then it changes its value to an integer so we can use it in the random number for
    //loop in the enternumber button
    public void findNumbers() {

        num1 = Integer.parseInt(String.valueOf(binding.NumberOneTextBox.getText()));
        num2 = Integer.parseInt(String.valueOf(binding.TextNumber2Box.getText()));
        num3 = Integer.parseInt(String.valueOf(binding.NumberAmountTextBox.getText()));
    }


    //this function gets the number that is inputted in the textboxes, and checks if
    //the text inputted is a value 0-9 and not a letter or a space, if it is a letter or space
    //it outputs an error stating this field is required as a number!
    private boolean CheckValues()
    {

        if (!binding.NumberOneTextBox.getText().toString().matches("[0-9]+"))
        {
            binding.NumberOneTextBox.setError("This field is required as a number");
            return false;
        }
        if (!binding.TextNumber2Box.getText().toString().matches("[0-9]+"))
        {
            binding.TextNumber2Box.setError("This field is required as a number");
            return false;
        }
        if (!binding.NumberAmountTextBox.getText().toString().matches("[0-9]+"))
        {
            binding.NumberAmountTextBox.setError("This field is required as a number");
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