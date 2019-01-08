package flightradarbot2.com.flightradarbot2;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Context ctx;
    private static String URL = "https://data-live.flightradar24.com/zones/fcgi/feed.js?bounds=69.04,-7.81,-212.16,212.16&faa=1&mlat=1&flarm=1&adsb=1&gnd=1&air=1&vehicles=1&estimated=1&maxage=14400&gliders=1&stats=1";
    private static int HALF_HOUR = 30 * 60 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ctx = this;
        doThread();
    }

    private void doThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true){
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(ctx,"Dziala", Toast.LENGTH_SHORT).show();
                        }
                    });

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    String jsonResponse = fetchJson();
                    saveAsFile(jsonResponse);

                    try {
                        Thread.sleep(HALF_HOUR);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void saveAsFile(String jsonResponse) {
        String currDateStr = new Date().toString()
                .replace(" ", "_")
                .replace(":", "-");

        File file = new File(getSdCardPath()  + "/" + currDateStr + ".txt");
        BufferedWriter writer = null;

        try {
            boolean newFile = file.createNewFile();

            if(!newFile){
                throw new RuntimeException("Plik nie powstal");
            }

            writer = new BufferedWriter(new FileWriter(file));
            writer.write(jsonResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if(writer != null){
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private String fetchJson() {
        String jsonResponse;

        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(URL);

        HttpResponse response;
        try {
            response = client.execute(request);

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            jsonResponse = result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Cos poszlo nie tak");
        }
        return jsonResponse;
    }

    private String getSdCardPath() {
        File[] dirs = ContextCompat.getExternalFilesDirs(this.ctx, null);
        return dirs[1].getAbsolutePath();
    }
}
