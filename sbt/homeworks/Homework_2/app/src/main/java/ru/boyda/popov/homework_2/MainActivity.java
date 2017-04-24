package ru.boyda.popov.homework_2;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    DrawingView drawingView;
    Button clearButton;
    Button toolsButton;
    Button freeMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawingView = (DrawingView) findViewById(R.id.painting);
        clearButton = (Button) findViewById(R.id.clear_button);
        toolsButton = (Button) findViewById(R.id.tools_button);
        freeMode = (Button) findViewById(R.id.free_mode_button);


        freeMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.setShapeID(ToolsBundle.CURVE_LINE);
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.clear();
            }
        });

        toolsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ToolsActivity.class);
                startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        drawingView.setShapeID(data.getIntExtra("Shape", ToolsBundle.RECTANGLE));
        drawingView.setColorID(data.getIntExtra("Color", 0));

    }
}
