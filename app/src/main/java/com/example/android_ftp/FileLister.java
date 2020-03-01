package com.example.android_ftp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.commons.net.ftp.FTPFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileLister extends MainActivity {

    String filename ="";
    File Savelocation = null;
    FileOutputStream fileOut = null;
    ByteArrayOutputStream fileget = null;
    private EditText dir;
    private TextView filegrid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_lister);
        dir = findViewById(R.id.directory);
        filegrid = findViewById(R.id.filedisplay);
        //Comprobar si la SD está montada, para guardar en SD/FTP o en INTERNA/FTP
        if((Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) &&
                (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY))){
            Savelocation = Environment.getExternalStorageDirectory();
        }
        else{
            Savelocation = Environment.getRootDirectory();
        }
        reconnect();
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
    public void uploader(){
        File firstLocalFile = new File("D:/Test/Projects.zip");

        String firstRemoteFile = "Projects.zip";
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(firstLocalFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        boolean done = false;
        try {
            done = engine.mFTPClient.storeFile(firstRemoteFile, inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (done) {
            System.out.println("The first file is uploaded successfully.");
        }
    }
    public void fileread() {

    }

    public void actualizado(View v){
        MainActivity.currentpath=dir.getText().toString();
        reconnect();
    }

    public void reconnect(){
        //volvemos a conectar
        try {
            engine.mFTPClient.abort();
        }
        catch(Exception e){

        }
        connect(sFTP,sUser,sPassword,sPort);
        filegrid.setText(engine.grid);
    }
}
