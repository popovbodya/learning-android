package ru.popov.bodya.myapplication;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView1 = (TextView) findViewById(R.id.text_view_1);
        TextView textView2 = (TextView) findViewById(R.id.text_view_2);
        TextView textView3 = (TextView) findViewById(R.id.text_view_3);

        File file1 = getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        File file2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
        File file3 = getFilesDir();
        textView1.setText(file1.getAbsolutePath());
        textView2.setText(file2.getAbsolutePath());
        textView3.setText(file3.getAbsolutePath());
    }
}
