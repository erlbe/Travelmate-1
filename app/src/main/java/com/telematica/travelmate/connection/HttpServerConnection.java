package com.telematica.travelmate.connection;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Erlend on 11.10.2016.
 */

public class HttpServerConnection {
    public String connectToServer(String myUrl, int timeOut, String requestMethod, JSONObject jsonObject) {
        int status = 0;
        try {
            URL url = new URL(myUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(timeOut);
            if(requestMethod.equals("GET")){
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                conn.connect();

                InputStream is = conn.getInputStream();
                return readInputStream(is);
            }
            else if(requestMethod.equals("POST")){
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput (true);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("charset", "utf-8");

                DataOutputStream wr = new DataOutputStream(conn.getOutputStream ());
                wr.writeBytes(jsonObject.toString());
                wr.flush();
                wr.close();


                InputStream is = conn.getInputStream();
                return readInputStream(is);
            }
            else{
                return null;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            if(status == 401){
                System.out.println("Unauthorized");
            }
            return null;
        }
    }

    /**
     * @param is
     * @return String of JSON object in the InputStream is.
     * @throws IOException
     */
    private String readInputStream(InputStream is) throws IOException {
        Reader reader = null;
        StringBuilder inputStringBuilder = new StringBuilder();

        reader = new InputStreamReader(is, "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(reader);

        String line = bufferedReader.readLine();
        while(line != null){
            inputStringBuilder.append(line);
            inputStringBuilder.append('\n');
            line = bufferedReader.readLine();
        }

        return inputStringBuilder.toString();
    }
}
