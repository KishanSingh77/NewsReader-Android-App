package com.example.newsapi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsapi.Article;
import com.example.newsapi.Source;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

//Group - 15
//Kishan Singh
//Amit S Kalyanpur

public class MainActivity extends AppCompatActivity {
     String category ="";
    public static String API_URL = "https://newsapi.org/v2/top-headlines";
    public String[] arr={"Business","Entertainment","General","Health","Science"};
    Button select_btn ;
    ImageButton prev_btn , next_btn ;
    TextView category_textView , newsTitle , publishedDate , pageNumber_textView , description_textView;
    ImageView imageView ;
    int current ;
    ProgressDialog pgr;
 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        select_btn  = findViewById(R.id.button_select);
        prev_btn = findViewById(R.id.imagePrev);
        next_btn = findViewById(R.id.imageNext) ;
        category_textView = findViewById(R.id.textView_Category);
        imageView = findViewById(R.id.imageView);


        // select_btn.setVisibility(View.INVISIBLE);
        prev_btn.setVisibility(View.VISIBLE);
        next_btn.setVisibility(View.VISIBLE);
        next_btn.setEnabled(false);

        prev_btn.setEnabled(false);

        imageView.setVisibility(View.INVISIBLE);
        newsTitle = findViewById(R.id.textView_NewsTitle);
        publishedDate = findViewById(R.id.textView_PublishDate);
        pageNumber_textView = findViewById(R.id.pageNumber_textView);
        description_textView = findViewById(R.id.description);


        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);

        Button select=findViewById(R.id.button_select);

        category_textView=findViewById(R.id.textView_Category);
        builder.setTitle(R.string.category)
                .setItems(arr, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //on click code
                        category_textView.setText(arr[which]);
                        category = arr[which];

                        if(isConnected()){
                            Toast.makeText(getApplicationContext() , "  Connected" , Toast.LENGTH_SHORT).show();
                            String totalUrl = API_URL+"?country=us&category="+category+"&apiKey=6161f39decbb4cc1869b0593ccae16bb";
                            current = 0;
                            new JSONParseAsync().execute(totalUrl);
                        }
                        else{
                            Toast.makeText(getApplicationContext() , "Not Connected" , Toast.LENGTH_SHORT).show();
                            return ;
                        }
                    }
                });
        final AlertDialog alert=builder.create();
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.show();

            }
        });

    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }

    private class JSONParseAsync extends AsyncTask<String, Void, ArrayList<Article>> {

        @Override
        protected void onPostExecute(final ArrayList<Article> articleArrayList) {

            pgr.dismiss();
            if(articleArrayList.size()==0)
            {
                Toast.makeText(MainActivity.this, "No News FOund!", Toast.LENGTH_SHORT).show();
                return ;
            }
            if(articleArrayList.size()!=0){

                articleArrayList.subList(20, articleArrayList.size()).clear();
                Log.d("Demo changed list size", articleArrayList.size()+"");

                for(Article article : articleArrayList) {
                    Log.d("Demo", article + "");
                }
                //   select_btn.setVisibility(View.VISIBLE);
                prev_btn.setEnabled(true);
                next_btn.setEnabled(true);
                imageView.setVisibility(View.VISIBLE);
                newsTitle.setVisibility(View.VISIBLE);
                publishedDate.setVisibility(View.VISIBLE);
                pageNumber_textView.setVisibility(View.VISIBLE);
                description_textView.setVisibility(View.VISIBLE);

                //setting deafult values

                Article article1 = articleArrayList.get(current);
                Picasso.get().load(article1.urlToImage).into(imageView);
                newsTitle .setText(article1.title);
                publishedDate .setText(article1.publishedAt);
                pageNumber_textView .setText(current+1 + " out of "+ Integer.valueOf(articleArrayList.size()));
                description_textView.setText(article1.description);


                Picasso.get().load(articleArrayList.get(current).urlToImage).into(imageView);

                // Prev Button logic
                prev_btn.setOnClickListener(new View.OnClickListener() {



                    @Override
                    public void onClick(View v) {

                        pgr = ProgressDialog.show(MainActivity.this, "", "Loading News...",true);

                        if(!isConnected()){
                            Toast.makeText(getApplicationContext() , "  Not Connected" , Toast.LENGTH_SHORT).show();

                            return;
                        }
                        if(current == 0 ){
                            pgr.dismiss();
//                            Toast.makeText(MainActivity.this, "Already on first news ", Toast.LENGTH_SHORT).show();
//                            return ;
                            current = articleArrayList.size();
                        }
                        current--;
                        Article article1 = articleArrayList.get(current);

                        Picasso.get().load(article1.urlToImage).into(imageView, new Callback() {
                            @Override
                            public void onSuccess() {
                               pgr.dismiss();
                            }

                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(MainActivity.this, "No IMAGE", Toast.LENGTH_SHORT).show();
                                pgr.dismiss();
                            }
                        });
                        newsTitle .setText(article1.title);
                        publishedDate .setText(article1.publishedAt);
                        pageNumber_textView .setText(current+1 + " out of "+ Integer.valueOf(articleArrayList.size()));
                        description_textView.setText(article1.description);

                    }
                });


                // Next Button logic
                next_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pgr = ProgressDialog.show(MainActivity.this, "", "Loading News...",true);

                        if(!isConnected()){
                            Toast.makeText(getApplicationContext() , "  Not Connected" , Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if(current == articleArrayList.size()-1){
                          pgr.dismiss();
//                            Toast.makeText(MainActivity.this, "Already on last news ", Toast.LENGTH_SHORT).show();
////                            return ;
                            current = -1 ;
                        }
                        current++;
                        Article article2 = articleArrayList.get(current);
                        Picasso.get().load(article2.urlToImage).into(imageView, new Callback() {
                            @Override
                            public void onSuccess() {
                                pgr.dismiss();
                            }

                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(MainActivity.this, "No IMAGE", Toast.LENGTH_SHORT).show();
                                pgr.dismiss();
                            }
                        });

                        newsTitle .setText(article2.title);
                        publishedDate .setText(article2.publishedAt);
                        pageNumber_textView .setText(current+1 + " out of "+ Integer.valueOf(articleArrayList.size()));
                        description_textView.setText(article2.description);

                    }
                });





            }
            else Log.d("Demo", "null");
        }

        @Override
        protected void onPreExecute() {

            pgr = ProgressDialog.show(MainActivity.this, "", "Loading News...",true);
        }

        @Override
        protected ArrayList<Article> doInBackground(String... strings) {

            HttpURLConnection connection = null;
            ArrayList<Article> articleArrayList = new ArrayList<>();

            String result = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    result = IOUtils.toString(connection.getInputStream(), "UTF-8");
                    JSONObject jsonObj = new JSONObject(result);
                    JSONArray ArticleArray =  jsonObj.getJSONArray("articles");

                    for( int i = 0 ; i < ArticleArray.length();i++)
                    {
                        JSONObject articleJSON = ArticleArray.getJSONObject(i);
                        Article article = new Article();

                        //resolving source json
                        JSONObject sourceJSON = articleJSON.getJSONObject("source");
                        Source source = new Source();
                        source.id = sourceJSON.getString("id");
                        source.name= sourceJSON.getString("name");


                        article.source= source;
                        article.author = articleJSON.getString("author");
                        article.title = articleJSON.getString("title");
                        article.description = articleJSON.getString("description");
                        article.url = articleJSON.getString("url");
                        article.urlToImage = articleJSON.getString("urlToImage");
                        article.publishedAt = articleJSON.getString("publishedAt");
                        article.content = articleJSON.getString("content");



                        articleArrayList.add(article);

                    }

                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            finally {
                //Close open connections and reader
                if (connection != null) {
                    connection.disconnect();
                }

            }
            return articleArrayList;

        }

    }


}
