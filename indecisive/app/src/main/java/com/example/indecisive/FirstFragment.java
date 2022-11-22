package com.example.indecisive;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.indecisive.databinding.FragmentFirstBinding;

//Author: Sarra Zerairia fragment
//Class and Section: 3033-001
//Date: November 30th, 2022
//Instructors Name: Professor Rafet Al-Tobasei
//This fragment connects everyones fragment to a button so the user can
//click on the button from the xml first fragment and it will navigate them
//to that specific fragment

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Takes you to the Food Picker Fragment
        binding.foodpickerapple.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_food_picker);
            }
        });

        //Takes you to the User Input Fragment
        binding.userinputbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_userinput);
            }
        });

        //Takes you to the Random Number Fragment
        binding.randomNumber.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        //Takes you to the Magic Ball Fragment
        binding.magicballbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_magicBall);
            }
        });

        //Takes you to teh Dice Roller Fragment
        binding.diceButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_actualdiceroller);
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}