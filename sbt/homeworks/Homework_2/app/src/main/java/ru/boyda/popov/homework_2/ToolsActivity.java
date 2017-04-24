package ru.boyda.popov.homework_2;


import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;


public class ToolsActivity extends Activity {


    private int shapeId = 0;
    private int colorId = 0;
    TypedArray typedArray;

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

        typedArray = getResources().obtainTypedArray(R.array.paint_colors);

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
                if (shapeId != 0 && colorId != 0) {
                    intent.putExtra("Shape", shapeId);
                    intent.putExtra("Color", colorId);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    setResult(RESULT_CANCELED, intent);
                }

            }
        });

    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.red_radio:
                if (checked)
                    colorId = getResources().getColor(R.color.red);
                break;
            case R.id.green_radio:
                if (checked)
                    colorId = getResources().getColor(R.color.green);
                break;
            case R.id.blue_radio:
                if (checked)
                    colorId = getResources().getColor(R.color.blue);
                break;
            case R.id.yellow_radio:
                if (checked)
                    colorId = getResources().getColor(R.color.yellow);
                break;
            case R.id.grey_radio:
                if (checked)
                    colorId = getResources().getColor(R.color.grey);
                break;
        }
    }


}
