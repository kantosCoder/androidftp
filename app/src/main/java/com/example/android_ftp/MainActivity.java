package com.example.android_ftp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

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

    FTPClient client = new FTPClient();
    private String sFTP = "";
    private String sPort ="";
    private String sUser = "";
    private String sPassword = "";
    private TextView ip;
    private TextView port;
    private TextView user;
    private TextView pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ip = findViewById(R.id.ipfield);
        port = findViewById(R.id.portfield);
        user = findViewById(R.id.userfield);
        pass = findViewById(R.id.passfield);
    }

    protected void checkserver(){
        //obtener valores de campos
        try {
            client.connect(sFTP+":"+sPort);
            boolean login = client.login(sUser,sPassword);
            //como es valido, pasar a la siguiente pantalla con un intent
        }
        catch (Exception e) {
            //mensaje de error al conectar toast
        }
    }

}
