package view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import umaahi.pravik.constructionnoteplus.R;

/**
 * Created by Vikrant on 3/9/2017.
 */

public class UpdateExpenseViewHolder extends RecyclerView.ViewHolder {

    public EditText name,paidBy,total,qty,price;
    public ImageButton edit;


    public UpdateExpenseViewHolder(View itemView) {
        super(itemView);

        name = (EditText) itemView.findViewById(R.id.name);
        paidBy = (EditText) itemView.findViewById(R.id.paidby);
        total = (EditText) itemView.findViewById(R.id.total);
        qty = (EditText) itemView.findViewById(R.id.qty);
        price = (EditText) itemView.findViewById(R.id.price);
        edit = (ImageButton) itemView.findViewById(R.id.editrecord);    }
}