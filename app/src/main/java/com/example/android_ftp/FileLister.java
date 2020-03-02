package com.example.android_ftp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
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
    private TextView filegrid;
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
        try {
            engine.mFTPClient.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentfilename = dir2.getText().toString();
        uploadTask async=new uploadTask();
        async.execute();
    }

    //async de descarga
    class downloadTask extends AsyncTask<String, Void, String> {

        private FTPClient mFtpClient =null;
        Boolean status = false;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
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
                if (!parentDir.exists())
                    parentDir.mkdir();
                OutputStream outputStream = null;
                try {
                    outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile));
                    mFtpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                    status= mFtpClient.retrieveFile(currentdir, outputStream);
                    Log.e("Status", String.valueOf(status));
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

                return new String("Download Successful");
            }catch (Exception e){
                String t="Failure : " + e.getLocalizedMessage();
                return t;
            }
        }
        @Override
        protected void onPostExecute(String str) {
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
                boolean status = false;
                try {
                    InputStream srcFileStream = new BufferedInputStream((new FileInputStream(getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath()+"/Download"+currentfilename)));
                    mFtpClient.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
                    status = mFtpClient.storeFile(currentfilename, srcFileStream);
                    Log.e("Status", String.valueOf(status));
                    srcFileStream.close();
                } catch (Exception e) {
                    throw e;
                }

                return new String("Upload Successful");
            } catch (Exception e) {
                return "d";
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
