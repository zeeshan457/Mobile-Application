package com.example.weatherradar;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {


    /*
     * ARGUMENTS & VARIABLES etc
     *
     *
     *
     *
     *
     */
    private final Context context;
    private final ArrayList<WeatherModel> weatherModelArrayList;

    /*
     * constructor for WeatherAdapter
     * setting attributes to the argument
     *
     *
     *
     *
     */
    public WeatherAdapter(Context context, ArrayList<WeatherModel> weatherModelArrayList) {
        this.context = context;
        this.weatherModelArrayList = weatherModelArrayList;
    }

    /*
     * creating view holder for the components
     * Returns the ViewHolder and a view object as argument
     *
     *
     *
     *
     */
    @NonNull
    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_item, parent, false);
        return new ViewHolder(view);
    }


    /*
     * on Bind view holder
     * using Picasso to process the json API images.
     * gets the current date and time, then sets it to the view holder.
     * catch parse errors from date
     *
     *
     *
     */
    @Override
    public void onBindViewHolder(@NonNull WeatherAdapter.ViewHolder holder, int position) {
        WeatherModel model = weatherModelArrayList.get(position);
            holder.Temperature.setText(model.getTemperature() + "°c");
        Picasso.get().load("http:".concat(model.getIcon())).into(holder.Condition);
        holder.Wind.setText(model.getWindSpeed() + "Km/h");

        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat output = new SimpleDateFormat("hh:mm aa");

        try {
            Date date = input.parse(model.getTime());
            holder.Time.setText(output.format(date));

        } catch (ParseException e) {
            System.out.println("Parse error!");
        }
    }



    /*
     * Gets the item count from the arraylist
     * Returns the size of the arraylist
     *
     *
     *
     *
     */
    @Override
    public int getItemCount() {
        return weatherModelArrayList.size();
    }



    /*
     * ViewHolder class to display the components
     * Extends RecyclerView
     *
     *
     *
     *
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView Wind;
        private final TextView Temperature;
        private final TextView Time;
        private final ImageView Condition;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Wind = itemView.findViewById(R.id.idTVWind);
            Temperature = itemView.findViewById(R.id.idTVTemperature);
            Time = itemView.findViewById(R.id.idTVTime);
            Condition = itemView.findViewById(R.id.idIVCondition);
        }
    }
}
