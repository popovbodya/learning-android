package ru.boyda.popov.homework_2;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;


public class ToolsActivity extends Activity {


    private int shapeId;

    ImageView finalResult;
    ImageButton rectangle;
    ImageButton square;
    ImageButton circle;
    ImageButton straightLine;
    Button saveToolsButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tools);

        rectangle = (ImageButton) findViewById(R.id.rectangle);
        square = (ImageButton) findViewById(R.id.square);
        circle = (ImageButton) findViewById(R.id.circle);
        straightLine = (ImageButton) findViewById(R.id.line);
        finalResult = (ImageView) findViewById(R.id.final_result);

        saveToolsButton = (Button) findViewById(R.id.save_tool);

        rectangle.setClickable(true);
        rectangle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalResult.setImageResource(R.drawable.rectangle);
                shapeId = ToolsBundle.RECTANGLE;
            }
        });

        circle.setClickable(true);
        circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalResult.setImageResource(R.drawable.circle);
                shapeId = ToolsBundle.CIRCLE;
            }
        });

        straightLine.setClickable(true);
        straightLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalResult.setImageResource(R.drawable.straight_line);
                shapeId = ToolsBundle.LINE;
            }
        });

        square.setClickable(true);
        square.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalResult.setImageResource(R.drawable.square);
                shapeId = ToolsBundle.SQUARE;
            }
        });

        saveToolsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("Shape", shapeId);
                setResult(RESULT_OK, intent);
                finish();

            }
        });

    }


}
