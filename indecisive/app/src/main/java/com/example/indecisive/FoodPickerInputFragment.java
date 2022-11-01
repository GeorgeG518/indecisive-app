package com.example.indecisive;
/*
    George Gannon
    FoodPickerInputFragment.java

    Responsible for collecting user input before we do a nearby search. It's mostly textboxes so it's not too complicated.
 */
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.indecisive.databinding.FragmentFoodPickerInputBinding;

public class FoodPickerInputFragment extends Fragment {


    private FragmentFoodPickerInputBinding binding;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentFoodPickerInputBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Listener for the button. if you click it, find the food.
        binding.findFoodButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                findFood();
            }
        }

        );
    }
    /*
     Arguably the most important function in the file. Responsible for error checking input and placing
     it into a bundle to get passed to the actual food picker input fragment.
     */
    public void findFood(){
        Bundle bundle = new Bundle(); // acts like a dictionary that is passed to another fragment
        bundle.putString("keyword", String.valueOf(binding.keywordInput.getEditText().getText())); // place the input from keyword into the bundle.
        try {
            bundle.putInt("magicNumber", Integer.parseInt(String.valueOf(binding.magicInput.getEditText().getText()))); // place the input of the magic number into the bundle
        } catch (NumberFormatException e) {
            bundle.putInt("magicNumber", 5182000); // if they didn't provide anything, put my birthday as the number
        }
        NavHostFragment.findNavController(FoodPickerInputFragment.this).navigate(R.id.action_food_picker_input_to_food_picker, bundle); // navigate to the next fragment
    }
}
