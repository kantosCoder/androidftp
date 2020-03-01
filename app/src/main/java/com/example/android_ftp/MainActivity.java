package com.example.android_ftp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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

    FTPClient client;
    private String sFTP = "";
    private String sPort ="";
    private String sUser = "";
    private String sPassword = "";
    private EditText ip;
    private EditText port;
    private EditText user;
    private EditText pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        client = new FTPClient();
        ip = findViewById(R.id.ipfield);
        port = findViewById(R.id.portfield);
        user = findViewById(R.id.userfield);
        pass = findViewById(R.id.passfield);
    }

    protected boolean checkserver(){
        boolean valid = false;
        //obtener valores de campos
        sFTP = ip.getText().toString();
        sPort = port.getText().toString();
        sUser = user.getText().toString();
        sPassword = pass.getText().toString();
        try {
            client.connect(sFTP);
            valid = client.login(sUser,sPassword);
        }
        catch (Exception e) {
            Toast toast1 =
                    Toast.makeText(getApplicationContext(),
                            "no entra en try "+sFTP, Toast.LENGTH_SHORT);

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
            //toast de que no naja
        }
    }

}
