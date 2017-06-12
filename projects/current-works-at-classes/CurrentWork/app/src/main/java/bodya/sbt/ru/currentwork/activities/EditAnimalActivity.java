package bodya.sbt.ru.currentwork.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import bodya.sbt.ru.currentwork.Animal;
import bodya.sbt.ru.currentwork.R;
import bodya.sbt.ru.currentwork.async.DataBaseLoaderFunctions;
import bodya.sbt.ru.currentwork.async.DataBaseWorker;
import bodya.sbt.ru.currentwork.interfaces.ModelProvider;

public class EditAnimalActivity extends AppCompatActivity {

    private static final String TAG = "EditAnimalActivity";
    private static final byte MAX_EDIT_TEXT_LENGTH = 9;
    private static final String UPDATE_MODE_KEY = "update_mode_key";


    private TextInputEditText ageEditText;
    private TextInputEditText heightEditText;
    private TextInputEditText weightEditText;
    private TextInputEditText nameEditText;
    private TextInputEditText[] editTexts;
    private Button addButton;

    private Animal extrasAnimal;
    private DataBaseWorker dataBaseWorker;
    private boolean updateMode = false;

    public static Intent newIntent(Context context) {
        return new Intent(context, EditAnimalActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_animal_layout);

        ModelProvider modelProvider = (ModelProvider) getApplication();
        dataBaseWorker = modelProvider.getDataBaseWorker();

        nameEditText = (TextInputEditText) findViewById(R.id.name_edit_text);
        ageEditText = (TextInputEditText) findViewById(R.id.age_edit_text);
        weightEditText = (TextInputEditText) findViewById(R.id.weight_edit_text);
        heightEditText = (TextInputEditText) findViewById(R.id.height_edit_text);
        addButton = (Button) findViewById(R.id.add_animal_button);

        editTexts = new TextInputEditText[]{ageEditText, nameEditText, weightEditText, heightEditText};
        for (TextInputEditText editText : editTexts) {
            editText.addTextChangedListener(new EditTextWatcherImpl());
        }

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateMode) {
                    updateAnimal();
                } else {
                    createAnimal();
                }
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            extrasAnimal = (Animal) extras.getSerializable(UPDATE_MODE_KEY);
            if (extrasAnimal != null) {
                fillEditTexts(extrasAnimal);
                addButton.setText(getResources().getString(R.string.update_animal_upper_case));
                updateMode = true;
            }
        }
    }

    private void updateAnimal() {
        Log.e(TAG, "updateAnimal");
        updateCachedAnimal();
        dataBaseWorker.queueTask(DataBaseLoaderFunctions.UPDATE_ANIMAL, extrasAnimal);
        updateMode = false;
        finish();
    }

    private void updateCachedAnimal() {
        Log.e(TAG, "updateCachedAnimal");
        String name = nameEditText.getText().toString();
        int age = Integer.valueOf(ageEditText.getText().toString());
        int weight = Integer.valueOf(weightEditText.getText().toString());
        int height = Integer.valueOf(heightEditText.getText().toString());

        extrasAnimal.setName(name);
        extrasAnimal.setAge(age);
        extrasAnimal.setWeight(weight);
        extrasAnimal.setHeight(height);

    }



    private void createAnimal() {
        Log.e(TAG, "createAnimal");
        String name = nameEditText.getText().toString();
        int age = Integer.valueOf(ageEditText.getText().toString());
        int weight = Integer.valueOf(weightEditText.getText().toString());
        int height = Integer.valueOf(heightEditText.getText().toString());

        Animal animal = new Animal(name, age, weight, height);
        dataBaseWorker.queueTask(DataBaseLoaderFunctions.CREATE_ANIMAL, animal);
        finish();
    }


    private void fillEditTexts(Animal animal) {
        if (animal == null) {
            return;
        }
        nameEditText.setText(animal.getName());
        ageEditText.setText(String.valueOf(animal.getAge()));
        weightEditText.setText(String.valueOf(animal.getWeight()));
        heightEditText.setText(String.valueOf(animal.getHeight()));
    }

    private class EditTextWatcherImpl implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            boolean setEnabled = true;
            for (EditText editText : editTexts) {
                Editable text = editText.getText();
                if (TextUtils.isEmpty(text) || text.length() > MAX_EDIT_TEXT_LENGTH) {
                    setEnabled = false;
                    break;
                }
            }
            addButton.setEnabled(setEnabled);
        }
    }
}
