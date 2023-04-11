package algonquin.cst2335.finalproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import algonquin.cst2335.finalproject.data.WeatherBeen;
import algonquin.cst2335.finalproject.databinding.ItemWeatherBinding;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.MyViewHolder> {
    ItemWeatherBinding itemBinding;

    private Context context;
    private List<WeatherBeen> examBeens;
    private OnItemClickListener onItemClickListener;

    public WeatherAdapter(Context context, List<WeatherBeen> examBeens, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.onItemClickListener = onItemClickListener;
        this.examBeens = examBeens;
    }

    public interface OnItemClickListener {
        void onItemClicked(int position);
        void onDetailClicked(int position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        itemBinding = ItemWeatherBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        MyViewHolder holder = new MyViewHolder(itemBinding);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.itemErrorQuestionBinding.tvCity.setText(examBeens.get(position).getCity());
        holder.itemErrorQuestionBinding.tvWeather.setText(examBeens.get(position).getWeather());
        holder.itemErrorQuestionBinding.tvTime.setText(examBeens.get(position).getTimeStr());
        holder.itemErrorQuestionBinding.tvTemp.setText(examBeens.get(position).getTemp() + "°");
        holder.itemErrorQuestionBinding.tvWind.setText(examBeens.get(position).getTemp() + "kmph");

        Glide.with(context).load(examBeens.get(position).getImage()).into(holder.itemErrorQuestionBinding.ivImage);

        holder.itemErrorQuestionBinding.ivDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClicked(position);
            }
        });
        holder.itemErrorQuestionBinding.llAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onDetailClicked(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return examBeens.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private ItemWeatherBinding itemErrorQuestionBinding;

        public MyViewHolder(ItemWeatherBinding itemErrorQuestionBinding) {
            super(itemErrorQuestionBinding.getRoot());
            this.itemErrorQuestionBinding = itemErrorQuestionBinding;
        }
    }

}
