package view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import umaahi.pravik.constructionnoteplus.R;


/**
 * Created by Vikrant on 2/28/2017.
 */

public  class SiteViewHolder extends RecyclerView.ViewHolder {

    public TextView authorView;
    public TextView bodyView;
     public TextView buttonViewOption;

    public SiteViewHolder(View itemView) {
        super(itemView);

        authorView = (TextView) itemView.findViewById(R.id.sitename);
        bodyView = (TextView) itemView.findViewById(R.id.location);
        buttonViewOption = (TextView) itemView.findViewById(R.id.textViewOptions);
    }
}