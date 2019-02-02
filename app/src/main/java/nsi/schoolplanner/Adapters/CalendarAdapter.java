package nsi.schoolplanner.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nsi.schoolplanner.Interfaces.ItemClickListener;
import nsi.schoolplanner.Model.Exam;
import nsi.schoolplanner.R;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder>{

    private List<Exam> exams;
    private ItemClickListener itemClickListener;
    private Context context;

    public CalendarAdapter(Context context, List<Exam> exams, ItemClickListener itemClickListener){
        this.exams = exams;
        this.itemClickListener = itemClickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.calendar_adapter_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String tmp = exams.get(position).getSubject().getName() + ": " + exams.get(position).getTitle();
        holder.txt.setText(tmp);
        holder.setClickListener(exams.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return exams.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
         TextView txt;

         ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.txt);
        }

        void setClickListener(final Long id){
            txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClick(id);
                }
            });
        }
    }
}
