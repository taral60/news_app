package com.patelfenil211.newsapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class SearchNews extends AppCompatActivity {

    //UI views from search_news.xml file
    private EditText searchEditText;
    private RecyclerView searchNewsRV;

    private NewsAdapter newsAdapter;
    private ArrayList<NewsModel> newsList;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_news);

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.action_home:
                        Intent intent = new Intent(SearchNews.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_search:
                        break;
                }

                return false;
            }
        });

        int gridColumnCount =
                getResources().getInteger(R.integer.grid_column_count);

        //initialize UI views
        searchEditText = findViewById(R.id.searchEditText);
        searchNewsRV = findViewById(R.id.searchNewsRV);

        //set the LayoutManager.
        searchNewsRV.setLayoutManager(new GridLayoutManager(this, gridColumnCount));

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH)
                {
                    String searchKey = v.getText().toString();

                    if(searchKey.equals("")) {
                        Toast.makeText(SearchNews.this, "Please Enter Your Search!", Toast.LENGTH_LONG).show();
                    }
                    else {
                        //method call...
                        displaySearchedNews(searchKey);
                    }

                    //hide the keyword and remove focus from EditText
                    InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    searchEditText.clearFocus();

                    return true;
                }
                return false;
            }
        });
    }

    /**
     * displaySearchedNews method...
     */
    public void displaySearchedNews(String searchKey)
    {
        //initialize list
        newsList = new ArrayList<>();
        newsList.clear();

        //instantiate the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //endpoint to get data
        String url = "https://gnews.io/api/v4/search?q=" + searchKey + "&lang=en&token=" + Constants.API_KEY4;

        //request data
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(String response)
            {
                try {
                    //convert string to JSON object
                    JSONObject jsonObject = new JSONObject(response);
                    //get articles array from that JSON object
                    JSONArray jsonArray = jsonObject.getJSONArray("articles");

                    //get all data from that array using loop
                    for(int i=0; i<jsonArray.length(); i++)
                    {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        JSONObject sourceObject = jsonObject1.getJSONObject("source");
                        String name = sourceObject.getString("name");
                        String title = jsonObject1.getString("title");
                        String description = jsonObject1.getString("description");
                        String url = jsonObject1.getString("url");
                        String image = jsonObject1.getString("image");
                        String publishedAt = jsonObject1.getString("publishedAt");
                        String content = jsonObject1.getString("content");

                        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat =
                                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat1 =
                                new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        String formattedDate;

                        try{
                            Date date = simpleDateFormat.parse(publishedAt);
                            formattedDate = simpleDateFormat1.format(date);
                        }
                        catch(Exception e) {
                            formattedDate = publishedAt;
                        }

                        //set data to newsModel
                        NewsModel newsModel = new NewsModel(
                                "" + name,
                                "" + title,
                                "" + description,
                                "" + url,
                                "" + image,
                                "" + formattedDate,
                                "" + content
                        );

                        //add newsModel to list
                        newsList.add(newsModel);
                    }

                    //setup adapter
                    newsAdapter = new NewsAdapter(SearchNews.this, newsList);
                    //set adapter to recycler view
                    searchNewsRV.setAdapter(newsAdapter);
                }
                catch(Exception e) {
                    //exception while loading JSON data
                    Toast.makeText(SearchNews.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //error while requesting for response
                Toast.makeText(SearchNews.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //add request to queue
        requestQueue.add(stringRequest);
    }

}