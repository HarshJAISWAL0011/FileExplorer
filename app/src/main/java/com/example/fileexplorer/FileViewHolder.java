package com.example.fileexplorer;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class FileViewHolder extends RecyclerView.ViewHolder {

    public TextView  tvname,tvsize;
    CardView container;
    ImageView imgfile;

    public FileViewHolder(@NonNull View itemView) {
        super(itemView);

    tvname=itemView.findViewById(R.id.tv_filename);
    tvsize=itemView.findViewById(R.id.filesize);
    container=itemView.findViewById( R.id.container);
    imgfile=itemView.findViewById(R.id.img_filetype);

    }
}
