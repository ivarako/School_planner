package nsi.schoolplanner.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import nsi.schoolplanner.Interfaces.ItemClickListener;
import nsi.schoolplanner.Model.DBManagerSingletone;
import nsi.schoolplanner.R;

public class WeekPlansAdapter extends RecyclerView.Adapter<WeekPlansAdapter.ViewHolder>{

    private Context context;
    private ItemClickListener itemClickListener;
    private List<Date> dates;
    private ArrayList<Integer> colors;
    private SimpleDateFormat simpleDateFormat;

    public WeekPlansAdapter(Context context, ItemClickListener itemClickListener){
        this.context = context;
        this.itemClickListener = itemClickListener;

        dates = DBManagerSingletone.getInstance(context).getDatesForToDoList();
        colors = new ArrayList<>();
        colors.add(context.getResources().getColor(R.color.blue));
        colors.add(context.getResources().getColor(R.color.yellow));
        colors.add(context.getResources().getColor(R.color.green));
        colors.add(context.getResources().getColor(R.color.orange));
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.week_plans_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dates.get(position));
        String text = context.getResources().getString(R.string.week2) + " " + calendar.get(Calendar.WEEK_OF_YEAR) + ": " + simpleDateFormat.format(calendar.getTime()) + " - ";
        calendar.add(Calendar.DAY_OF_YEAR, 6);
        text+= simpleDateFormat.format(calendar.getTime());

        holder.textView.setText(text);
        holder.view.setBackgroundColor(colors.get(position%4));
        holder.setOnClickListener(dates.get(position));
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        View view;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            view = itemView.findViewById(R.id.view);
            textView = itemView.findViewById(R.id.txtText);
        }

        void setOnClickListener(final Date date){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClick(date);
                }
            });
        }
    }
}
