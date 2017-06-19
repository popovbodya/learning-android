package bodya.sbt.ru.currentwork;

import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

import bodya.sbt.ru.currentwork.db.AnimalsDao;
import bodya.sbt.ru.currentwork.interfaces.OnAnimalContentChangeListener;

public class AnimalsStorageTest {

    private static final String TAG = AnimalsStorageTest.class.getSimpleName();

    @Mock
    public AnimalsDao animalsDaoMock;

    @Mock
    public OnAnimalContentChangeListener contentChangeListenerMock;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public TestName testName = new TestName();

    private AnimalStorage animalStorage;


    @Before
    public void setUp() {
        Log.e(TAG, "setUp");
        animalStorage = new AnimalStorage(animalsDaoMock);
        animalStorage.addOnContentChangeListener(contentChangeListenerMock);
    }

    @After
    public void tearDown() {
        Log.e(TAG, "tearDown");
        animalStorage.removeOnContentChangeListener(contentChangeListenerMock);
    }

    @Test
    public void testGetAnimals() {
        Log.e(TAG, testName.getMethodName());
        List<Animal> mockedList = EntitiesGenerator.createRandomAnimalsList();
        when(animalsDaoMock.getAnimals()).thenReturn(mockedList);
        List<Animal> actual = animalStorage.getAnimalList();
        verify(animalsDaoMock, times(1)).getAnimals();
        assertThat(actual, is(mockedList));
    }

    @Test
    public void testAddAnimal() {
        Log.e(TAG, testName.getMethodName());
        Animal animal = EntitiesGenerator.createRandomAnimal();
        animalStorage.addAnimal(animal);
        verify(animalsDaoMock, times(1)).insertAnimal(animal);
        verify(contentChangeListenerMock, times(1)).onContentChanged();
    }

    @Test
    public void testGetAnimalById() {
        Log.e(TAG, testName.getMethodName());
        long id = EntitiesGenerator.createRandomLong();
        animalStorage.getAnimalByID(id);
        verify(animalsDaoMock, times(1)).getAnimalById(id);
    }

    @Test
    public void testDeleteAnimal() {
        Log.e(TAG, testName.getMethodName());
        Animal animal = EntitiesGenerator.createRandomAnimal();
        animalStorage.deleteAnimal(animal);
        verify(animalsDaoMock, times(1)).deleteAnimal(animal);
        verify(contentChangeListenerMock, times(1)).onContentChanged();
    }

    @Test
    public void testUpdateAnimalWithCacheContains() {
        Log.e(TAG, testName.getMethodName());
        Animal animal = EntitiesGenerator.createRandomAnimal();
        List<Animal> animalList = new ArrayList<>(Collections.singletonList(animal));
        animalStorage.setCachedAnimalList(animalList);
        animalStorage.updateAnimal(animal);
        verifyZeroInteractions(animalsDaoMock);
        verifyZeroInteractions(contentChangeListenerMock);
    }

    @Test
    public void testUpdateAnimalWithCacheNotContains() {
        Log.e(TAG, testName.getMethodName());
        Animal animal = EntitiesGenerator.createRandomAnimal();
        animalStorage.setCachedAnimalList(new ArrayList<Animal>());
        animalStorage.updateAnimal(animal);
        verify(animalsDaoMock, times(1)).updateAnimal(animal);
        verify(contentChangeListenerMock, times(1)).onContentChanged();
    }

}
