package com.example.bloodfinder;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by zed_home on 7/9/15.
 */
public class PostAdapter extends ArrayAdapter<Post> {
    Context ctx;
    int res;
    Post data[]=null;
    public PostAdapter(Context context, int resource, Post[] objects) {
        super(context, resource, objects);
        ctx=context;
        res=resource;
        data=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        PostHolder holder=null;
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)ctx).getLayoutInflater();
            row = inflater.inflate(res, parent, false);

            holder = new PostHolder();
            holder.tvDesc=(TextView)row.findViewById(R.id.tvDescPost);
            holder.tvMobile=(TextView)row.findViewById(R.id.tvMobilePost);
            holder.tvDate=(TextView)row.findViewById(R.id.tvDatePost);
            holder.tvBlood=(TextView)row.findViewById(R.id.tvBlood);

            holder.tvHeader=(TextView)row.findViewById(R.id.tvHeadingPost);


            row.setTag(holder);
        }
        else
        {
            holder = (PostHolder)row.getTag();
        }

        Post post = data[position];
        holder.tvDesc.setText(post.getDesc());
        holder.tvBlood.setText(post.getBlood());
        holder.tvDate.setText(post.getDatePost());
        holder.tvMobile.setText(post.getMobile());
        holder.tvHeader.setText(post.getHeading());

        return row;
    }
    static class PostHolder
    {

        TextView tvHeader,tvDesc,tvDate,tvMobile,tvBlood;
    }
}
