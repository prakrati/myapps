package view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import umaahi.pravik.constructionnoteplus.R;


/**
 * Created by Vikrant on 2/28/2017.
 */

public  class OtherExpenseViewHolder extends RecyclerView.ViewHolder {

    public EditText name,amount,date;
    public ImageButton edit,delete,save;

    public OtherExpenseViewHolder(View itemView) {
        super(itemView);

        name = (EditText) itemView.findViewById(R.id.name);
        amount = (EditText) itemView.findViewById(R.id.amount);
        date = (EditText) itemView.findViewById(R.id.date);

        edit = (ImageButton) itemView.findViewById(R.id.editrecord);
        delete = (ImageButton) itemView.findViewById(R.id.deleterecord);
        save = (ImageButton) itemView.findViewById(R.id.saverecord);
    }
}