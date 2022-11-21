/*
* user input randomizer fragment
* developed by Ahmad Mohammad
* 11-21
* */

package com.example.indecisive;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.indecisive.databinding.FragmentSecondBinding;
import com.example.indecisive.databinding.FragmentUserinputBinding;

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link userinput#newInstance} factory method to
 * create an instance of this fragment.
 */
public class userinput extends Fragment {

    private FragmentUserinputBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int i = 0;
    private String[] arr = new String[100];

    public userinput() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment userinput.
     */
    // TODO: Rename and change types and number of parameters
    public static userinput newInstance(String param1, String param2) {
        userinput fragment = new userinput();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentUserinputBinding.inflate(inflater, container, false);
        return binding.getRoot();

        //return inflater.inflate(R.layout.fragment_userinput, container, false);



    }

       // binding = FragmentSecondBinding.inflate(inflater, container, false);
        //return binding.getRoot();




    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        binding.enterinputbutton.setOnClickListener(new View.OnClickListener(){

                @Override


                public void onClick(View view) {
                    // create function to get input field box (binding input names)
                    // maybe add to array

                     /*
                     * init empty string to fill with
                     * "enterinputnames text field"
                     * */

                    String holder = "";

                    holder = String.valueOf(binding.enterinputnames.getText());
                    arr[i] = holder; // assign array at i to textvield value
                    i++;

                    // clear

                    binding.enterinputnames.setHint("Enter next choice!");

                    binding.textView14.setText("items added: " + i);

                }
            });







        binding.finishedbutton.setOnClickListener(new View.OnClickListener(){

            @Override


            public void onClick(View view) {
                // maybe create function to get input firld box
                // amd maybe add to array

                Random randnum = new Random(100-1);
                int q = randnum.nextInt(i-1);

                i = 0;

                binding.winnernametextfield.setText(arr[q]);
                binding.textView14.setText("items added: " + i);

                for(int p = 0; p < arr.length; p++)
                {
                    arr[p] = "";
                }



            }
        }

        );


    }



}

