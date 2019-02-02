package nsi.schoolplanner.Activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.Date;

import nsi.schoolplanner.Adapters.WeekPlansAdapter;
import nsi.schoolplanner.Interfaces.ItemClickListener;
import nsi.schoolplanner.R;

public class AllWeekPlansActivity extends AppCompatActivity implements View.OnClickListener, ItemClickListener{

    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_week_plans);

        findViewByID();
        setOnClickListeners();
        initRecyclerView();
    }

    private void findViewByID(){
        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);
    }

    private void setOnClickListeners(){
        fab.setOnClickListener(this);
    }

    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new WeekPlansAdapter(this, this));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab:{
                Intent intent = new Intent(AllWeekPlansActivity.this, WeekPlanActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("modeEdit", false);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    public void onItemClick(Object object) {
        Intent intent = new Intent(AllWeekPlansActivity.this, WeekPlanActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("modeEdit", true);
        bundle.putSerializable("date", (Date)object);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        initRecyclerView();
        super.onResume();
    }
}
