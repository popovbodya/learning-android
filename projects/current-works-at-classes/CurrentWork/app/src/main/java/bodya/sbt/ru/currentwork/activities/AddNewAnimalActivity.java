package bodya.sbt.ru.currentwork.activities;


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

import bodya.sbt.ru.currentwork.Animal;
import bodya.sbt.ru.currentwork.AnimalStorage;
import bodya.sbt.ru.currentwork.R;
import bodya.sbt.ru.currentwork.interfaces.AnimalsStorageProvider;

public class AddNewAnimalActivity extends AppCompatActivity {

    private static final String NAME_KEY = "name_key";
    private static final String AGE_KEY = "age_key";
    private static final String WEIGHT_KEY = "weight_key";
    private static final String HEIGHT_KEY = "height_key";
    private static final String ID_KEY = "id_key";
    private static final String TYPE_KEY = "type_key";

    private EditText ageEditText;
    private EditText heightEditText;
    private EditText weightEditText;
    private EditText nameEditText;
    private EditText[] editTexts;
    private Button addButton;

    private AnimalStorage animalStorage;
    private boolean updateMode = false;

    public static Intent newIntent(Context context) {
        return new Intent(context, AddNewAnimalActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_animal_layout);

        AnimalsStorageProvider animalsStorageProvider = (AnimalsStorageProvider) getApplication();
        animalStorage = animalsStorageProvider.getAnimalsStorage();

        nameEditText = (EditText) findViewById(R.id.name_edit_text);
        ageEditText = (EditText) findViewById(R.id.age_edit_text);
        weightEditText = (EditText) findViewById(R.id.weight_edit_text);
        heightEditText = (EditText) findViewById(R.id.height_edit_text);

        addButton = (Button) findViewById(R.id.add_animal_button);

        editTexts = new EditText[]{ageEditText, nameEditText, weightEditText, heightEditText};

        for (EditText editText : editTexts) {
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

        if (getIntent().getExtras() != null) {
            getDataFromExtras();
            updateMode = true;
        }
    }

    private Animal createAnimalFromEditTexts() {
        int age = Integer.valueOf(ageEditText.getText().toString());
        int weight = Integer.valueOf(weightEditText.getText().toString());
        int height = Integer.valueOf(heightEditText.getText().toString());
        String name = nameEditText.getText().toString();

        return new Animal(name, age, weight, height);
    }

    private void fillNotEditableParams(Animal animal) {
        Bundle bundle = getIntent().getExtras();
        animal.setId(bundle.getLong(ID_KEY));
        animal.setAnimalType(Animal.AnimalType.valueOf(bundle.getString(TYPE_KEY)));
    }

    private void updateAnimal() {
        Animal animal = createAnimalFromEditTexts();
        fillNotEditableParams(animal);
        animalStorage.updateAnimal(animal);
        updateMode = false;
        finish();
    }

    private void createAnimal() {
        Animal animal = createAnimalFromEditTexts();
        animalStorage.addAnimal(animal);
        finish();
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
                if (TextUtils.isEmpty(editText.getText())) {
                    setEnabled = false;
                    break;
                }
            }
            addButton.setEnabled(setEnabled);
        }
    }

    private void getDataFromExtras() {
        Bundle bundle = getIntent().getExtras();
        nameEditText.setText(bundle.getString(NAME_KEY));
        ageEditText.setText(String.valueOf(bundle.getInt(AGE_KEY)));
        weightEditText.setText(String.valueOf(bundle.getInt(WEIGHT_KEY)));
        heightEditText.setText(String.valueOf(bundle.getInt(HEIGHT_KEY)));
    }
}
