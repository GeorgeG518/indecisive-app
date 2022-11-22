package com.example.indecisive;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.indecisive.databinding.FragmentRealDiceRollerBinding;
//import com.example.indecisive.data binding.FragmentSecondBinding;

import java.util.Random;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RealDiceRoller#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RealDiceRoller extends Fragment {

    private FragmentRealDiceRollerBinding binding;
    int number = 0;  //number of dice
    int total = 0;   //total of all dice rolled
    int random = 0;  //random number
    boolean check = false;    //text field

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RealDiceRoller.
     */

    public static RealDiceRoller newInstance(String param1, String param2) {

        RealDiceRoller fragment = new RealDiceRoller();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        {

            binding = FragmentRealDiceRollerBinding.inflate(inflater, container, false);
            return binding.getRoot();

        }
        //return inflater.inflate(R.layout.fragment_real_dice_roller, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.rollbutton.setOnClickListener(view1 -> {
            check = Check();   //calls Check Function

            if (Check()) {     //if field isn't empty

                diceNumber();   //calls dice number function

                Random rannum = new Random();         //creates a random number
                binding.rolled.setText(" ");
                binding.totalrolled.setText(" ");     //displays total rolled
                for (int i = 0; i < number; i++) {//rolls dice based on how many the user entered
                    random = rannum.nextInt(6) + 1;
                    total = total + random;             //calculates total
                    binding.rolled.append(random + " "); //displays numbers rolled
                    binding.totalrolled.setText(total);  //displays total
                }
            }


        });
            }


        public void diceNumber() {            //puts number entered by user into number variable

            number = Integer.parseInt(String.valueOf(binding.numberfield.getText()));
        }
        private boolean Check() {   //makes sure amount of dice was entered

            if (!binding.numberfield.getText().toString().matches("[0-9]+"))
            {
                binding.numberfield.setError("This field is required");
                return false;
            }
            return true;
        }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}