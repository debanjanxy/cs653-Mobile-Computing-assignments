package com.example.deka.locationapp; /**
 * Created by deka on 12/4/18.
 */
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by deka on 12/4/18.
 */

public class ListOnlineviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView text_email;
    itemClickedListner item_Clicked_Listner;



    public ListOnlineviewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        text_email = (TextView)itemView.findViewById(R.id.textEmail);

    }

    public void setText_email(TextView text_email) {
        this.text_email = text_email;
    }

    public void setItem_Clicked_Listner(itemClickedListner item_Clicked_Listner) {
        this.item_Clicked_Listner = item_Clicked_Listner;
    }

    @Override
    public void onClick(View v) {

        item_Clicked_Listner.onClick(v,getAdapterPosition());

        Log.i("called:::::","xxxxxx");
    }
}

/*

https://stackoverflow.com/questions/24471109/recyclerview-onclick
https://stackoverflow.com/questions/44151979/how-to-add-onclick-listener-to-recycler-view
https://stackoverflow.com/questions/24885223/why-doesnt-recyclerview-have-onitemclicklistener
https://stackoverflow.com/questions/34110497/how-to-implement-a-setonitemclicklistener-firebaserecyclerviewadapter/40134429#40134429*/
