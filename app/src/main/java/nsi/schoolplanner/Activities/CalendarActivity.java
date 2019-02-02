package nsi.schoolplanner.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import nsi.schoolplanner.Adapters.CalendarAdapter;
import nsi.schoolplanner.Interfaces.ItemClickListener;
import nsi.schoolplanner.Model.DBManagerSingletone;
import nsi.schoolplanner.Model.Exam;
import nsi.schoolplanner.R;

public class CalendarActivity extends AppCompatActivity implements View.OnClickListener, ItemClickListener{

    private CompactCalendarView calendarView;
    private TextView txtMonth;
    private ImageView imgLeft;
    private ImageView imgRight;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        getSupportActionBar().setTitle(getResources().getString(R.string.calendar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewsById();
        setCalendarView();
        setOnClickListeners();
    }

    private void findViewsById(){
        calendarView = findViewById(R.id.calendar);
        txtMonth = findViewById(R.id.txtMonth);
        imgLeft = findViewById(R.id.imgLeft);
        imgRight = findViewById(R.id.imgRight);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setOnClickListeners(){
        imgLeft.setOnClickListener(this);
        imgRight.setOnClickListener(this);
    }

    private void setCalendarView(){
        final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        txtMonth.setText(dateFormat.format(calendar.getTime()));

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        for(int i = 0; i<calendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++){
            List<Exam> exams = DBManagerSingletone.getInstance(this).getExamsForDate(calendar.getTime());
            if(exams.size()>0) {
                Event event = new Event(getResources().getColor(R.color.green), calendar.getTimeInMillis(), "");
                calendarView.addEvent(event);
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                CalendarAdapter adapter = new CalendarAdapter(CalendarActivity.this, DBManagerSingletone.getInstance(CalendarActivity.this).getExamsForDate(dateClicked), CalendarActivity.this);
                recyclerView.invalidate();
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                txtMonth.setText(dateFormat.format(firstDayOfNewMonth));
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgLeft:{
                calendarView.scrollLeft();
                break;
            }

            case R.id.imgRight:{
                calendarView.scrollRight();
                break;
            }
        }
    }

    @Override
    public void onItemClick(Object object) {
        Intent intent = new Intent(this, ExamActivity.class);
        Bundle idBundle = new Bundle();
        idBundle.putLong("id", (Long) object);
        intent.putExtras(idBundle);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
