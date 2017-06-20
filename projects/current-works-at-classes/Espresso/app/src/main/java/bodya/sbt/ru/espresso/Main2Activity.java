package bodya.sbt.ru.espresso;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button button = (Button) findViewById(R.id.result_button);
        final EditText editText = (EditText) findViewById(R.id.first_edit_text);
        final EditText editText2 = (EditText) findViewById(R.id.second_edit_text);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder stringBuilder = new StringBuilder();
                TextView textView = (TextView) findViewById(R.id.result_text_view);
                if (editText.getText() != null) {
                    stringBuilder.append(editText.getText());
                }
                if (editText2.getText() != null) {
                    stringBuilder.append(editText2.getText());
                }
                textView.setText(stringBuilder.toString());
            }
        });
    }
}