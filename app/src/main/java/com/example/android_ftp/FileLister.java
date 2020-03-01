package com.example.android_ftp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class FileLister extends AppCompatActivity {

    String filename ="";
    File Savelocation = null;
    FileOutputStream fileOut = null;
    ByteArrayOutputStream fileget = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_lister);
        //Comprobar si la SD está montada, para guardar en SD/FTP o en INTERNA/FTP
        if((Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) &&
                (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY))){
            Savelocation = Environment.getExternalStorageDirectory();
        }
        else{
            Savelocation = Environment.getRootDirectory();
        }
    }
    public void downloader(){
        try{
            //escribimos el fichero descargado al directorio FTP en la sd
            fileOut = new FileOutputStream(Savelocation+"/ftp/"+filename);
            fileget = new ByteArrayOutputStream();
        }
        catch(Exception IOException){
            //mostrar mensaje de error de descarga
        }
        finally{
            if(fileOut != null){
                try{
                    fileOut.close();
                }
                catch(Exception e){
                    //ignorar
                }
            }
        }
    }
    public void fileread(){

    }
}
