package com.puja.mart.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.puja.mart.Modal.Order;
import com.puja.mart.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList;
    private Context context;

    // Constructor to initialize context and order list
    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    // ViewHolder class to hold each item view
    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        // Define the view elements in the item layout
        TextView productName, quantity, amount, paymentStatus, deliveryStatus;
        ImageView productThumb;

        public OrderViewHolder(View itemView) {
            super(itemView);
            // Initialize views from the item layout
            productName = itemView.findViewById(R.id.tvProductName);
            quantity = itemView.findViewById(R.id.tvQuantity);
            amount = itemView.findViewById(R.id.tvAmount);
            paymentStatus = itemView.findViewById(R.id.tvPaymentStatus);
            deliveryStatus = itemView.findViewById(R.id.tvDeliveryStatus);
            productThumb = itemView.findViewById(R.id.ivProductThumb);
        }
    }

    // Inflating the item layout for each row
    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    // Binding the data to each view in the ViewHolder
    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        // Set data to the views
        holder.productName.setText(order.getProductName());
        holder.quantity.setText("Quantity: " + order.getQuantity());
        holder.amount.setText("Amount: ₹" + order.getAmount());
        holder.paymentStatus.setText("Payment Status: " + order.getPaymentStatus());
        holder.deliveryStatus.setText("Delivery Status: " + order.getDeliveryStatus());

        // Load the image using a library like Picasso or Glide
        Glide.with(this.context).load("https://pujanmart.com/images/product/" + order.getProductThumb()).into(holder.productThumb);
    }

    // Return the total number of items
    @Override
    public int getItemCount() {
        return orderList.size();
    }
}

