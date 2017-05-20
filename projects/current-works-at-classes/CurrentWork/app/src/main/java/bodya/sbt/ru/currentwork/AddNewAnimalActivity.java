package bodya.sbt.ru.currentwork;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddNewAnimalActivity extends AppCompatActivity {

    private EditText ageEditText;
    private EditText heightEditText;
    private EditText weightEditText;
    private EditText nameEditText;

    private Button addButton;
    private EditText[] editTexts;

    public static Intent newIntent(Context context) {
        return new Intent(context, AddNewAnimalActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_animal_layout);

        ageEditText = (EditText) findViewById(R.id.age_edit_text);
        nameEditText = (EditText) findViewById(R.id.name_edit_text);
        weightEditText = (EditText) findViewById(R.id.weight_edit_text);
        heightEditText = (EditText) findViewById(R.id.height_edit_text);

        addButton = (Button) findViewById(R.id.add_animal_button);

        editTexts = new EditText[]{ageEditText, nameEditText, weightEditText, heightEditText};
        for (EditText editText : editTexts) {
            editText.addTextChangedListener(new TextWatcherImpl());
        }

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAnimal();
            }
        });
    }

    private void createAnimal() {
        int age = Integer.valueOf(ageEditText.getText().toString());
        int weight = Integer.valueOf(weightEditText.getText().toString());
        int height = Integer.valueOf(heightEditText.getText().toString());
        String name = nameEditText.getText().toString();
        Animal animal = new Animal(name, age, weight, height);
        ((MyApplication) getApplication()).addAnimal(animal);
        finish();
    }

    private class TextWatcherImpl implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            boolean buttonEnabled = true;
            for (EditText editText : editTexts) {
                if (TextUtils.isEmpty(editText.getText())) {
                    buttonEnabled = false;
                    break;
                }
            }
            addButton.setEnabled(buttonEnabled);
        }
    }
}
