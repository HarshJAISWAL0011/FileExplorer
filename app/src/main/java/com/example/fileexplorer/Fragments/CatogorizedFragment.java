package com.example.fileexplorer.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fileexplorer.FileAdapter;
import com.example.fileexplorer.FileOpener;
import com.example.fileexplorer.R;
import com.example.fileexplorer.onFileSelectedListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CatogorizedFragment extends Fragment implements onFileSelectedListener {
    private RecyclerView recyclerView;
    private FileAdapter fileAdapter;
    private List<File> fileList;
    View view;

    File storage,path;
    String data;
    String [] items={"Details","Rename","Delete","Share"};

    @Nullable
    @Override
    public View onCreateView ( @NonNull LayoutInflater inflater , @Nullable ViewGroup container , @Nullable Bundle savedInstanceState ) {
        view = inflater.inflate ( R.layout.fragment_catogorizedl , container , false );

        Bundle bundle=this.getArguments ();
        if ( bundle.getString ( "fileType" ).equals ( "download" ) ){
            path= Environment.getExternalStoragePublicDirectory ( Environment.DIRECTORY_DOWNLOADS );
        }
else
    path=Environment.getExternalStorageDirectory ();

        runtimePermission ( );

        return view;
    }

    private void runtimePermission () {
        Dexter.withContext ( getContext ( ) ).withPermissions ( Manifest.permission.WRITE_EXTERNAL_STORAGE ,
                Manifest.permission.READ_EXTERNAL_STORAGE ).withListener ( new MultiplePermissionsListener ( ) {
            @Override
            public void onPermissionsChecked ( MultiplePermissionsReport multiplePermissionsReport ) {
                displayFiles ( );
            }

            @Override
            public void onPermissionRationaleShouldBeShown ( List<PermissionRequest> list , PermissionToken permissionToken ) {
                permissionToken.continuePermissionRequest ( );
            }
        } ).check ( );
    }


    public ArrayList<File> findfiles ( File file ) {

        ArrayList<File> arrayList = new ArrayList<> ( );
        File[] files = file.listFiles ( );
        for ( File singleFile : files ) {

            if ( singleFile.isDirectory ( ) && ! singleFile.isHidden ( ) ) {
                arrayList.addAll ( findfiles ( singleFile ));
            }
            else{
                switch (getArguments ().getString ( "fileType" ))
                {
                    case "images":
                        if( singleFile.getName ( ).toLowerCase ( ).endsWith ( ".jpeg" ) || singleFile.getName ( ).toLowerCase ( ).endsWith ( ".png" ) ||
                                singleFile.getName ( ).toLowerCase ( ).endsWith ( ".jpg" ))
                            arrayList.add ( singleFile );
                    break;

                    case "video":
                        if( singleFile.getName ( ).toLowerCase ( ).endsWith ( ".mp4" ) )
                            arrayList.add ( singleFile );
                        break;

                    case "apk":
                        if( singleFile.getName ( ).toLowerCase ( ).endsWith ( ".apk" ) )
                            arrayList.add ( singleFile );
                        break;


                    case "audio":
                        if( singleFile.getName ( ).toLowerCase ( ).endsWith ( ".mp3" ) || singleFile.getName ( ).toLowerCase ( ).endsWith ( ".wav" ))
                            arrayList.add ( singleFile );
                        break;

                    case "docs":
                        if( singleFile.getName ( ).toLowerCase ( ).endsWith ( ".pdf" ) || singleFile.getName ( ).toLowerCase ( ).endsWith ( ".docx" ))
                            arrayList.add ( singleFile );
                        break;
                    case "download":
                        if(  singleFile.getName ( ).toLowerCase ( ).endsWith ( ".jpeg" ) || singleFile.getName ( ).toLowerCase ( ).endsWith ( ".png" ) ||
                                singleFile.getName ( ).toLowerCase ( ).endsWith ( ".jpg" ) || singleFile.getName ( ).toLowerCase ( ).endsWith ( ".mp3" )
                                || singleFile.getName ( ).toLowerCase ( ).endsWith ( ".mp4" ) || singleFile.getName ( ).toLowerCase ( ).endsWith ( ".png" )
                                || singleFile.getName ( ).toLowerCase ( ).endsWith ( ".apk" ) || singleFile.getName ( ).toLowerCase ( ).endsWith ( ".pdf" )
                                || singleFile.getName ( ).toLowerCase ( ).endsWith ( ".docx" ) || singleFile.getName ( ).toLowerCase ( ).endsWith ( ".wav" ))
                            arrayList.add ( singleFile );
                        break;

                }
            }
        }


        return arrayList;

    }

    private void displayFiles () {
        recyclerView = view.findViewById ( R.id.recycler_internal );
        recyclerView.setHasFixedSize ( true );
        recyclerView.setLayoutManager ( new GridLayoutManager ( getContext ( ) , 2) );
        fileList = new ArrayList<> ( );
        fileList.addAll ( findfiles ( path ) );
        fileAdapter = new FileAdapter ( getContext ( ) , fileList , this );
        recyclerView.setAdapter ( fileAdapter );


    }

    @Override
    public void onFileClilcked ( File file ) {
        if ( file.isDirectory ( ) ) {
            Bundle bundle = new Bundle ( );
            bundle.putString ( "path" , file.getAbsolutePath ( ) );
            CatogorizedFragment internalFragment = new CatogorizedFragment ( );
            internalFragment.setArguments ( bundle );
            getFragmentManager ().beginTransaction ().replace ( R.id.fragment_container , internalFragment ).addToBackStack ( null ).commit ( );
        } else {
            try {
                FileOpener.openFile(getContext (),file);
            } catch (IOException e) {
                e.printStackTrace ( );
            }

        }
    }


    @Override
    public void onFileLongClicked ( File file ,int pos) {
        final Dialog optionDialog =new Dialog ( getContext () );
        optionDialog.setContentView ( R.layout.option_dialog );
        optionDialog.setTitle ( "Selected Options." );
        ListView options=(ListView) optionDialog.findViewById ( R.id.list );
            CustomAdapter customAdapter= new CustomAdapter ();
            options.setAdapter ( customAdapter );
            optionDialog.show();

    options.setOnItemClickListener(
        new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String selectedItems = adapterView.getItemAtPosition(i).toString();
            switch (selectedItems) {
              case "Details":
                  optionDialog.dismiss ();
                AlertDialog.Builder detaildialog = new AlertDialog.Builder(getContext());
                detaildialog.setTitle("Details");
                final TextView details = new TextView(getContext());
                details.setPadding ( 35,47,34,3 );
                detaildialog.setView(details);
                Date lastmodified = new Date(file.lastModified());
                SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yy HH:mm:ss");
                String formattedDate = formatter.format(lastmodified);
                details.setText(
                    String.format(
                        "File Name "
                            + file.getName()
                            + "\n"
                            + "Path: "
                            + file.getAbsolutePath()
                            + "\n"
                            + "Size "
                            + Formatter.formatShortFileSize(getContext(), file.length())
                            + "\n"
                            + "Last Modified "
                            + lastmodified));

                detaildialog.setPositiveButton(
                    "Ok",
                    new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialogInterface, int i) {
                        optionDialog.cancel();
                      }
                    });

                AlertDialog alertdialog_detail = detaildialog.create();
                alertdialog_detail.show();
                break;

              case "Rename":
                AlertDialog.Builder renameDialog = new AlertDialog.Builder(getContext());
                renameDialog.setTitle("Rename");
                final EditText name = new EditText(getContext());
                renameDialog.setView(name);
                renameDialog.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialogInterface, int i) {
                        String new_name = name.getEditableText().toString();
                        String extension =
                            file.getAbsolutePath()
                                .substring((file.getAbsolutePath()).lastIndexOf("."));
                        File current = new File(file.getAbsolutePath());
                        File destination =
                            new File(
                                file.getAbsolutePath().replace(file.getName(), new_name)
                                    + extension);

                        if (current.renameTo(destination)) {
                          fileList.set(pos, destination);
                          fileAdapter.notifyItemChanged(pos);
                          Toast.makeText(getContext(), "Renamed", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(getContext () , "couldn't Renamed", Toast.LENGTH_SHORT).show();
                      }
                    });
            renameDialog.setNegativeButton ( "Cancel" , new DialogInterface.OnClickListener ( ) {
                @Override
                public void onClick (DialogInterface dialogInterface , int i) {
                    optionDialog.cancel ();
                }
            } );
            AlertDialog alertDialog_rename=renameDialog.create ();
            alertDialog_rename.show ();

            break;

                case "Delete":
                    AlertDialog.Builder deleteDialog = new AlertDialog.Builder(getContext());
                    deleteDialog.setTitle("Delete "+file.getName ()+" ?");

                    deleteDialog.setPositiveButton ( "Delete" , new DialogInterface.OnClickListener ( ) {
                        @Override
                        public void onClick (DialogInterface dialogInterface , int i) {
                      file.delete ();
                      fileList.remove ( pos );
                      fileAdapter.notifyDataSetChanged ();
                      Toast.makeText(getContext (), "Deleted", Toast.LENGTH_SHORT).show();
                        }
                    } );
                deleteDialog.setNegativeButton ( "No" , new DialogInterface.OnClickListener ( ) {
                    @Override
                    public void onClick (DialogInterface dialogInterface , int i) {
                        optionDialog.cancel ();
                    }
                } );
                    AlertDialog alertDialog_delete=deleteDialog.create ();
                    alertDialog_delete.show ();
            break;

                case "Share":
                    String filename=file.getName ();
                    Intent intent= new Intent (  );

                    StrictMode.VmPolicy.Builder builder=new StrictMode.VmPolicy.Builder ();
                    StrictMode.setVmPolicy ( builder.build () );
                    builder.detectFileUriExposure ();

                    intent.setAction ( Intent.ACTION_SEND );
                    intent.setType ( "image/jpeg" );
                    intent.putExtra ( Intent.EXTRA_STREAM, Uri.fromFile ( file ) );
                    startActivity ( Intent.createChooser ( intent,"Share "+filename ) );
                    optionDialog.dismiss ();
                    break;
            }
          }
        });
    }

    class CustomAdapter extends BaseAdapter{


        @Override
        public int getCount () {
            return items.length;
        }

        @Override
        public Object getItem (int i) {
            return items[i];
        }

        @Override
        public long getItemId (int i) {
            return 0;
        }

        @Override
        public View getView (int i , View view , ViewGroup viewGroup) {
            View myview=getLayoutInflater ().inflate ( R.layout.option_layout,null );
            TextView txtOptions=myview.findViewById ( R.id.txtoption );
            ImageView imgoptions=myview.findViewById ( R.id.imgOption );
            txtOptions.setText ( items[i] );
            if(items[i].equals ( "Details" )){
                imgoptions.setImageResource ( R.drawable.details );
            }
            else if(items[i].equals ( "Rename" )){
                imgoptions.setImageResource ( R.drawable.rename );
            }
            else if(items[i].equals ( "Delete" )){
                imgoptions.setImageResource ( R.drawable.delete );
            }
            else if(items[i].equals ( "Share" )){
                imgoptions.setImageResource ( R.drawable.share );
            }

                return myview;



        }
    }

}