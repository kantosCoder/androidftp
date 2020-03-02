package com.example.android_ftp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

public class FileLister extends MainActivity {

    private static final int READ_REQUEST_CODE = 42;
    long startTime = System.currentTimeMillis() ;
    Context ctxt;
    String filename ="";
    File Savelocation = null;
    FileOutputStream fileOut = null;
    ByteArrayOutputStream fileget = null;
    private EditText dir;
    private String currentdir;
    private EditText dir2;
    private String currentfilename;
    private String filePath;
    private TextView filegrid;
    private EditText dentrodelfichero;
    private File filetoload = null;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_lister);
        ctxt = this.getApplicationContext();
        dir = findViewById(R.id.directory);
        dir2 = findViewById(R.id.directory2);
        filegrid = findViewById(R.id.filedisplay);
        dentrodelfichero = findViewById(R.id.dentrodelfichero);

        reconnect();
    }
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    //seleccion del archivo a subir
    public void fileselect(View v){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                filetoload = new File(uri.getPath());
                final String[] split = filetoload.getPath().split(":");
                filePath = split[1];
                filetoload = new File(filePath);
                Toast toast1 =
                        Toast.makeText(getApplicationContext(),
                                Environment.getExternalStorageDirectory()+"/"+filetoload.getPath()+" Y "+filetoload.getName(), Toast.LENGTH_LONG);
                toast1.show();
            }
        }
    }

    public void downloader(View v){
        try {
            engine.mFTPClient.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentdir = dir.getText().toString();
        verifyStoragePermissions(this);
        downloadTask async=new downloadTask();
        async.execute();
    }
    public void uploader(View v){

        /*
        uploadTask async=new uploadTask();
        async.execute();*/

        try
        {
                //engine.mFTPClient.enterLocalPassiveMode(); // important!
                //engine.mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
                String data = Environment.getExternalStorageDirectory()+"/"+filetoload.getPath();
                FileInputStream in = new FileInputStream(new File(data));
                boolean result = engine.mFTPClient.storeFile("/"+filetoload.getName(), in);
                in.close();
                if (result) Log.v("upload result", "succeeded");
                engine.mFTPClient.logout();
                engine.mFTPClient.disconnect();
        }
        catch (Exception e)
        {
            Toast toast1 =
                    Toast.makeText(getApplicationContext(),
                            "error ", Toast.LENGTH_SHORT);
            toast1.show();
            e.printStackTrace();
        }
    }


    public String cacahuete(){
        FTPClient mFtpClient =null;
        Boolean status = false;
        String hola = "";
        try {
            try {
                try {
                    mFtpClient.connect(sFTP);
                }catch (Exception e){
                    e.printStackTrace();
                }
                status = mFtpClient.login(sUser, sPassword);
            } catch (SocketException e) {
                throw e;
            }
            catch (UnknownHostException e) {
                throw e;
            }
            catch (IOException e) {
                throw e;
            }
            File downloadFile=new File("/");
            File parentDir = downloadFile.getParentFile();

            OutputStream outputStream = null;
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                mFtpClient.retrieveFile(dir2.getText().toString(), baos);
                hola = new String(baos.toByteArray());
                baos.close();

            } catch (Exception e) {
                throw e;
            }
            finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        throw e;
                    }
                }
            }

            return hola;
        }catch (Exception e){
            String t="Failure : " + e.getLocalizedMessage();
            return t;
        }
    }

    //async de descarga
    class downloadTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {


            return null;
        }
        @Override
        protected void onPostExecute(String str) {
            String texto = cacahuete();
            dentrodelfichero.setText(texto);
            reconnect();
        }
    }


    //async de subida de datos
    class uploadTask extends AsyncTask<String, Void, String> {
        private FTPClient mFtpClient =null;
        Boolean status = false;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            FTPClient con = null;

            try
            {
                con = new FTPClient();
                con.connect(sFTP);

                if (con.login(sUser, sPassword))
                {
                    con.enterLocalPassiveMode(); // important!
                    con.setFileType(FTP.BINARY_FILE_TYPE);
                    String data = "/"+filetoload.getPath();
                    FileInputStream in = new FileInputStream(new File(data));
                    boolean result = con.storeFile("/"+filetoload.getName(), in);
                    in.close();
                    if (result) Log.v("upload result", "succeeded");
                    con.logout();
                    con.disconnect();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return "d";
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
