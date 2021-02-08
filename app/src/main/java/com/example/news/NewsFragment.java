package com.example.news;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.news.newsapi.News;
import com.example.news.newsapi.NewsData;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class NewsFragment extends Fragment {

    private static final String TAG = "NewsFragment";

    public static NewsFragment newInstance(NewsData newsData,
                                           int index, int max)
    {
        NewsFragment f = new NewsFragment();
        Bundle bdl = new Bundle(1);
        bdl.putSerializable("News_DATA", newsData);
        bdl.putSerializable("INDEX", index);
        bdl.putSerializable("TOTAL_COUNT", max);
        f.setArguments(bdl);
        return f;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragment_layout = inflater.inflate(R.layout.news_fragment, container, false);
        Bundle args = getArguments();
        if (args != null) {
            final NewsData newsData = (NewsData) args.getSerializable("News_DATA");
            if(newsData == null){
                return null;
            }
            int index = args.getInt("INDEX");
            int total = args.getInt("TOTAL_COUNT");
            TextView headline = fragment_layout.findViewById(R.id.headline);
            headline.setText(newsData.getTitle());
            TextView date = fragment_layout.findViewById(R.id.date);
        //    String datePart1 = newsData.getDate().replace("T"," ");
         //   String datePart2 = datePart1.replace("Z","");
          //  Log.d(TAG, "onCreateView: "+datePart2);

            String currentFormat = "yyyy-MM-dd'T'hh:mm:ss'Z'";
            String targetFormat = "MMM dd, yyyy HH:mm";
            DateFormat srcDf = new SimpleDateFormat(currentFormat);
            srcDf.setTimeZone(TimeZone.getDefault());
            DateFormat destDf = new SimpleDateFormat(targetFormat);
            try {
                Date date2 = srcDf.parse(newsData.getDate());
                String targetDate = destDf.format(date2);
                Log.d(TAG, "onCreateView: "+targetDate);
                date.setText(targetDate);
            }
            catch (Exception e){
                Log.d(TAG, "onCreateView: "+e.getMessage());
            }
          //  SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:aa");
          //  String dateFinal = dateFormat.format(datePart2);
          //  Log.d(TAG, "onCreateView: "+dateFinal);
            // String string = "January 2, 2010";
          //  SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:aa");
          //  Date d;
          //  try {
             //   d = dateFormat.parse(newsData.getDate());
             //   date.setText(d.toString());
            //} catch (ParseException e) {
          //      e.printStackTrace();
           // }
            // LocalDate dateData = LocalDate.parse(newsData.getDate(), dateFormat);

            //SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:aa");
            //Date d = new Date(newsData.getDate());
            //date.setText(dateFormat.format(d).toString());
            TextView author = fragment_layout.findViewById(R.id.author);
            author.setText(newsData.getAuthor());
            TextView body = fragment_layout.findViewById(R.id.newsbody);
            body.setText(newsData.getDescription());

            ImageView image = fragment_layout.findViewById(R.id.imageView);
            Picasso.get().load(newsData.getImageUrl()).into(image, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {

                }
            });

            return fragment_layout;
        }
        else {
            return null;
        }

    }

}
