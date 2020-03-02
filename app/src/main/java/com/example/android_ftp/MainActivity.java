package com.example.android_ftp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPHTTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.commons.net.io.CopyStreamEvent;
import org.apache.commons.net.io.CopyStreamListener;
import org.apache.commons.net.util.TrustManagerUtils;


public class MainActivity extends AppCompatActivity {

    public asyncConnexion engine;
    public static String sFTP = "";
    public static String currentpath = "/";
    public static int sPort = 0;
    public static String sUser = "";
    public static String sPassword = "";
    private EditText ip;
    private EditText port;
    private EditText user;
    private EditText pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ip = findViewById(R.id.ipfield);
        port = findViewById(R.id.portfield);
        user = findViewById(R.id.userfield);
        pass = findViewById(R.id.passfield);
    }

    protected boolean checkserver(){
        boolean valid = false;
        //obtener valores de campos
        sPort = Integer.parseInt(port.getText().toString());
        //sFTP = "ftp://"+ip.getText().toString()+sPort;
        sFTP = ip.getText().toString();
        sUser = user.getText().toString();
        sPassword = pass.getText().toString();
        try {
           valid = connect(sFTP,sUser,sPassword,sPort);
        }
        catch (Exception e) {
            Toast toast1 =
                    Toast.makeText(getApplicationContext(),
                            "error al establecer conexion", Toast.LENGTH_SHORT);

            toast1.show();
            valid = false;
        }
        return valid;
    }
    public void iniciado(View v){
        boolean valid;
        valid = checkserver();
        if(valid){
            Intent intent= new Intent(this, FileLister.class);
            startActivity(intent);
        }
        else{
            Toast toast1 =
                    Toast.makeText(getApplicationContext(),
                            "error al intentar conectar a: \n"+sFTP, Toast.LENGTH_SHORT);
            toast1.show();
        }
    }

    public boolean connect(String host, String username, String password, int port)
    {
        boolean valid = false;
        try
        {
            engine = new asyncConnexion(host, username, password, port);
            return engine.execute().get();
        }
        catch (Exception e)
        {
            return false;
        }
    }
}
