package com.example.fileexplorer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;
import java.util.Locale;

public class FileAdapter extends RecyclerView.Adapter<FileViewHolder> {
private Context context;
private List<File> file;
private onFileSelectedListener listener;

    public FileAdapter(Context context, List<File> file,onFileSelectedListener listener) {
        this.context = context;
        this.file = file;
        this.listener=listener;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view= LayoutInflater.from(context).inflate(R.layout.file_container,parent,false);

        return new FileViewHolder(LayoutInflater.from ( context ).inflate(R.layout.file_container,parent,false));
    }

    @Override
    public void onBindViewHolder ( @NonNull FileViewHolder holder , @SuppressLint("RecyclerView") int position ) {
        holder.tvname.setText ( file.get ( position ).getName ( ) );
        holder.tvname.setSelected ( true );//horizontal effect
        int items = 0;
        if ( file.get ( position ).isDirectory ( ) ) {
            File[] files = file.get ( position ).listFiles ( );
            for ( File singleFile : files ) {
                if ( ! singleFile.isHidden ( ) ) {
                    items += 1;
                }
            }
            holder.tvsize.setText ( String.valueOf ( items ) + " Files" );
        }
    else
        holder.tvsize.setText ( Formatter.formatShortFileSize(context,file.get(position).length ()) );


    if(file.get ( position ).getName ().toLowerCase( Locale.ROOT ).endsWith ( ".jpeg" )||file.get ( position ).getName ().toLowerCase( Locale.ROOT ).endsWith ( ".jpg" )
    ||file.get ( position ).getName ().toLowerCase( Locale.ROOT ).endsWith ( ".png" )){
        holder.imgfile.setImageResource ( R.drawable.photo);
    }
else if(file.get ( position ).getName ().toLowerCase( Locale.ROOT ).endsWith ( ".apk" )){
        holder.imgfile.setImageResource ( R.drawable.apk);
    }
    else if(file.get ( position ).getName ().toLowerCase( Locale.ROOT ).endsWith ( ".mp4" )){
        holder.imgfile.setImageResource ( R.drawable.movie);
    }
    else if(file.get ( position ).getName ().toLowerCase( ).endsWith ( ".mp3" )){
        holder.imgfile.setImageResource ( R.drawable.mp3);
    }
    else if(file.get ( position ).getName ().toLowerCase( Locale.ROOT ).endsWith ( ".docx" )||file.get ( position ).getName ().toLowerCase( Locale.ROOT ).endsWith ( ".pdf" )){
        holder.imgfile.setImageResource ( R.drawable.pdf);
    }
    else if(file.get ( position ).getName ().toLowerCase( ).endsWith ( ".wav" )){
        holder.imgfile.setImageResource ( R.drawable.mp3);
    }
    else {
        holder.imgfile.setImageResource ( R.drawable.folder);
    }


    holder.container.setOnClickListener ( new View.OnClickListener ( ) {
        @Override
        public void onClick ( View view ) {
            listener.onFileClilcked ( file.get ( position ) );
        }
    } );


    holder.container.setOnLongClickListener ( new View.OnLongClickListener ( ) {
        @Override
        public boolean onLongClick ( View view ) {
            listener.onFileLongClicked ( file.get ( position ),position);
            return true;
        }
    } );





    }
    @Override
    public int getItemCount() {
        return file.size ();
    }
}
