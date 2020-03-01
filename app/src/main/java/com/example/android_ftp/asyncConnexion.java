package com.example.android_ftp;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
public class asyncConnexion extends AsyncTask<Void, Void, Boolean>
{
    public FTPClient mFTPClient = null;
    private String host;
    private String username;
    private String password;
    private int port;

    asyncConnexion(String host, String username, String password, int port)
    {
        this.host = host;
        this.password = password;
        this.port = port;
        this.username = username;
    }


    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            mFTPClient = new FTPClient();
            // connecting to the host
            mFTPClient.connect(host, port);
            // now check the reply code, if positive mean connection success
            boolean status = mFTPClient.login(username, password);

            mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
            mFTPClient.enterLocalPassiveMode();
            return status;

        } catch (Exception e) {
            Log.i("testConnection", "Error: could not connect to host " + host);
            e.printStackTrace();

        }
        return false;
    }
}
