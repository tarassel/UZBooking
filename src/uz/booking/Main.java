package uz.booking;

import com.jayway.jsonpath.Filter;
import com.jayway.jsonpath.JsonPath;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.jayway.jsonpath.Criteria.where;
import static com.jayway.jsonpath.Filter.filter;

public class Main {

    public static void main(String[] args) {

        System.out.println("Started");

        Integer nCoupe = null;
        Integer nCoupeNew;

        try {
            new JFXPanel();
            String startSound = "C:\\Windows\\Media\\chimes.wav";
            Media hit = new Media(new File(startSound).toURI().toString());
            new MediaPlayer(hit).play();

            do {
                System.out.print(".");
                if (nCoupe != null)
                    Thread.sleep(30 * 1000);

                HttpURLConnection urlConn;
                URL mUrl = new URL("https://booking.uz.gov.ua/purchase/search/");
                urlConn = (HttpURLConnection) mUrl.openConnection();
                urlConn.setDoOutput(true);
                String query = "station_id_from=2210700&station_id_till=2218000&station_from=%D0%94%D0%BD%D1%96%D0%BF%D1%80%D0%BE-%D0%93%D0%BE%D0%BB%D0%BE%D0%B2%D0%BD%D0%B8%D0%B9&station_till=%D0%9B%D1%8C%D0%B2%D1%96%D0%B2&date_dep=02.01.2018&time_dep=00%3A00&time_dep_till=&another_ec=0&search=";

                urlConn.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConn.setRequestProperty("Content-Length", Integer.toString(query.length()));
                urlConn.getOutputStream().write(query.getBytes("UTF8"));
//                System.out.println("Resp: " + urlConn.getResponseCode());
//                System.out.println("Size: " + urlConn.getInputStream().available());

                Filter cheapFictionFilter = filter(
                        where("id").is("К")
                );

                List<Integer> l = new ArrayList();

                try {
                    l = JsonPath.read(urlConn.getInputStream(), "$.value[0].types[?].places", cheapFictionFilter);
                } catch (Exception e) {
                }

                if (!l.isEmpty()) {
                    nCoupeNew = l.get(0);
                }
                else nCoupeNew = 0;

                if (nCoupe == null) {
                    System.out.println("Initial free places number is: " + nCoupeNew);
                    nCoupe = nCoupeNew;
                }

                if (nCoupe != null && nCoupe != nCoupeNew) {
                    java.util.Date date = new java.util.Date();
                    System.out.println("");
                    System.out.println(date + " ! New free places number is: " + nCoupeNew);

                    if (nCoupeNew > 3 && nCoupeNew > nCoupe) {
                        String updateSound = "C:\\Windows\\Media\\alarm01.wav";
                        Media hit1 = new Media(new File(updateSound).toURI().toString());
                        new MediaPlayer(hit1).play();
                    }

                    nCoupe = nCoupeNew;
                }

            } while (true);
        } catch (Exception e) {
            System.out.println("Eception: " + e.getMessage());
            String errorSound = "C:\\Windows\\Media\\ringout.wav";
            Media hit1 = new Media(new File(errorSound).toURI().toString());
            new MediaPlayer(hit1).play();
        }
    }
}
