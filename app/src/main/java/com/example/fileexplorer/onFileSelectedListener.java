package com.example.fileexplorer;

import java.io.File;

public interface onFileSelectedListener {

    void onFileClilcked( File file);
    void onFileLongClicked(File file,int position);
}
