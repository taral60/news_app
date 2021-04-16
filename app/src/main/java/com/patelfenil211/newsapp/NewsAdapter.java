package com.patelfenil211.newsapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.HolderNewsList> {

    /**
     * ViewHolder class
     */
    public class HolderNewsList extends RecyclerView.ViewHolder {
        //UI views from news_item.xml file
        TextView sourceNameTV, newsTitleTV, newsDescriptionTV, publishedAtTV;
        ImageView newsImageView;
        Button btnSend;

        public HolderNewsList(@NonNull View itemView) {
            super(itemView);
            //initialize UI views
            sourceNameTV = itemView.findViewById(R.id.sourceNameTV);
            newsTitleTV = itemView.findViewById(R.id.newsTitleTV);
            newsDescriptionTV = itemView.findViewById(R.id.newsDescriptionTV);
            publishedAtTV = itemView.findViewById(R.id.publishedAtTV);
            newsImageView = itemView.findViewById(R.id.newsImageView);
            btnSend = itemView.findViewById(R.id.btnSend);
        }
    }

    //data members
    private Context context;
    private ArrayList<NewsModel> newsList;

    //constructor
    public NewsAdapter(Context context, ArrayList<NewsModel> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    //methods...
    @NonNull
    @Override
    public HolderNewsList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout news_item.xml
        View view = LayoutInflater.from(context).inflate(R.layout.news_item, parent, false);
        return new HolderNewsList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderNewsList holder, int position) {
        //get data
        NewsModel newsModel = newsList.get(position);
        String sourceName = newsModel.getSourceName();
        String newsTitle = newsModel.getNewsTitle();
        String newsDescription = newsModel.getNewsDescription();
        String newsUrl = newsModel.getNewsUrl();
        String newsImage = newsModel.getNewsImage();
        String publishedAt = newsModel.getPublishedAt();
        //String newsContent = newsModel.getNewsContent();

        //set data
        holder.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, newsUrl);
                context.startActivity(Intent.createChooser(shareIntent, "Share Via"));
            }
        });
        holder.sourceNameTV.setText(sourceName);
        holder.newsTitleTV.setText(newsTitle);
        holder.newsDescriptionTV.setText(newsDescription);
        holder.publishedAtTV.setText(publishedAt);
        Picasso.get().load(newsImage).into(holder.newsImageView);

        //handle click event
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start news details activity
                Intent intent = new Intent(context, NewsDetails.class);
                intent.putExtra("url", newsUrl);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();  //return size of list
    }
}
