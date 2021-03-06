package com.example.kks_meinplan;


import android.os.AsyncTask;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class ActivityDataSource extends AsyncTask<String, Void, String> {

    public static final String AUTHKEY = "test321";

    public static final String POST_PARAM_KEYVALUE_SEPARATOR = "=";
    public static final String POST_PARAM_SEPARATOR = "&";

    private static final String DESTINATION_METHOD = "allEntrys";

    private TextView textView;

    private  URLConnection conn;

    public ActivityDataSource(TextView textView) {
        this.textView = textView;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            openConnection();
            return readResult();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    //Folgende Methode erstellt die URL mit den benötigten Parametern und öffnet eine Verbindung zu dieser mit einer URLConnection.
    private void openConnection() throws IOException{
        //StringBuffer für das zusammensetzen der URL
        StringBuffer dataBuffer = new StringBuffer();
        dataBuffer.append(URLEncoder.encode("authkey", "UTF-8"));
        dataBuffer.append(POST_PARAM_KEYVALUE_SEPARATOR);
        dataBuffer.append(URLEncoder.encode(AUTHKEY, "UTF-8"));
        dataBuffer.append(POST_PARAM_SEPARATOR);
        dataBuffer.append(URLEncoder.encode("method", "UTF-8"));
        dataBuffer.append(POST_PARAM_KEYVALUE_SEPARATOR);
        dataBuffer.append(URLEncoder.encode(DESTINATION_METHOD, "UTF-8"));
        //Adresse der PHP Schnittstelle für die Verbindung zur MySQL Datenbank
        URL url = new URL("https://192.168.1.195/reader.php");
        conn = url.openConnection();
        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(dataBuffer.toString());
        wr.flush();
    }


    //Ergebnisse aus der Datenbank (geöffneten Verbindung) lesen
    private String readResult()throws IOException{
        String result = null;
        //Lesen der Rückgabewerte vom Server
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line = null;
        //Solange Daten bereitstehen, werden diese gelesen.
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString(); //liefert einen String mit dem gelesenen Werten.
    }

    @Override
    protected void onPostExecute(String result) {
        if(!isBlank(result)) {
            this.textView.setText(result); //ist irgendwas in der Zelle, dann wird es angezeigt
        }
    }

    private boolean isBlank(String value){
        return value == null || value.trim().isEmpty();
    }


}