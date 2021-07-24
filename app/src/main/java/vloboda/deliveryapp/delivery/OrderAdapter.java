package vloboda.deliveryapp.delivery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    Context context;
    ArrayList<Order> list;

    public OrderAdapter(Context context, ArrayList<Order> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.order_card, parent, false);
        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull OrderAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public  class MyViewHolder extends RecyclerView.ViewHolder{


        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);


        }
    }
}
