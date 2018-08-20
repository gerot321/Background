package com.example.gerrys.proof;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

public class MainActivity extends AppCompatActivity {

    private static final String Job_tag = "my_job_tag";
    private FirebaseJobDispatcher jobDispatcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Button start = (Button) findViewById(R.id.startButton);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startJobs();
            }
        });
        Button cancel = (Button) findViewById(R.id.stopButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopJobs();
            }
        });
    }

    public void startJobs(){
        Job job = jobDispatcher.newJobBuilder()
                .setService(MyServices.class)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTag(Job_tag)
                .setTrigger(Trigger.executionWindow(30,30))
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setReplaceCurrent(false).build();
        jobDispatcher.mustSchedule(job);

        Toast.makeText(this,"Job started",Toast.LENGTH_LONG).show();
    }
    public void stopJobs(){
        jobDispatcher.cancel(Job_tag);
        Toast.makeText(this,"Job Cancelled",Toast.LENGTH_LONG).show();

    }

}
