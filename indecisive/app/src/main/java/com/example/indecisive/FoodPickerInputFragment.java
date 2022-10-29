package com.example.indecisive;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.indecisive.databinding.FragmentFirstBinding;
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
        binding.findFoodButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Bundle bundle = new Bundle();
                bundle.putString("keyword", String.valueOf(binding.keywordInput.getEditText().getText()));
                try {
                    bundle.putInt("magicNumber", Integer.parseInt(String.valueOf(binding.magicInput.getEditText().getText())));
                }catch (NumberFormatException e){
                    bundle.putInt("magicNumber", 5182000);
                }
                NavHostFragment.findNavController(FoodPickerInputFragment.this).navigate(R.id.action_food_picker_input_to_food_picker, bundle);
            }
        }

        );
    }
}
