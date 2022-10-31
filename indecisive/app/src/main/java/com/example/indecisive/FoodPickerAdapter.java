package com.example.indecisive;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FoodPickerAdapter extends RecyclerView.Adapter<FoodPickerAdapter.ViewHolder> {
    private List<Bitmap> bitmapList;

    public FoodPickerAdapter(List<Bitmap> dabitmaps){
        this.bitmapList = dabitmaps;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        public ViewHolder(View view){
            super(view);
            image = (ImageView) view.findViewById(R.id.card_view_image);

        }
    }

    @NonNull
    @Override
    public FoodPickerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View imageView = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_picker_photo_item,parent,false);
        return new ViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodPickerAdapter.ViewHolder holder, int position) {
        holder.image.setImageBitmap(bitmapList.get(position));
    }



    @Override
    public int getItemCount() {
        return bitmapList.size();
    }
}
