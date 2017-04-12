package ru.popovbodya.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MyActivity extends Activity {

    private TextView textView;
    private Button button;
    private ImageButton kittyButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(R.string.hot_welcome);
            }
        });

        kittyButton = (ImageButton) findViewById(R.id.imageButton);
        kittyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisible(v);
            }
        });
    }

    public void sayHello(View view) {
        textView.setText(R.string.new_hello);
    }

    private void setVisible(View view) {
        if (textView.getVisibility() == View.VISIBLE) {
            textView.setVisibility(View.INVISIBLE);
        } else {
            textView.setVisibility(View.VISIBLE);
        }
    }
}
