package ru.boyda.popov.usergit.parsing;


import android.support.test.InstrumentationRegistry;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import ru.boyda.popov.usergit.pojo.Repository;
import ru.boyda.popov.usergit.test.R;

import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isIn;
import static ru.boyda.popov.usergit.parsing.ParsingRepositoryHelper.createMultiplySpecifiedUsers;
import static ru.boyda.popov.usergit.parsing.ParsingRepositoryHelper.createOneSpecifiedRepo;

public class ParsingRepositoryTest {

    @Test
    public void testParsingSingleRepository() throws IOException {
        Repository expected = createOneSpecifiedRepo("coursera-projects", "https://api.github.com/repos/MoonightCS/coursera-projects",
                "1285", "78668851", "2017-01-11T18:50:44Z");

        ObjectMapper mapper = new ObjectMapper();
        InputStream inputStream = null;
        List<Repository> repositories;

        try {
            inputStream = InstrumentationRegistry.getContext().getResources().openRawResource(R.raw.test_single_repository);
            repositories = Arrays.asList(mapper.readValue(inputStream, Repository[].class));
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        assertThat(repositories, notNullValue());
        assertThat(repositories.size(), is(1));
        Repository actual = repositories.get(0);
        assertThat(actual, is(expected));
    }

    @Test
    public void testParsingMultiplyRepo() throws IOException {
        List<Repository> expected = createMultiplySpecifiedUsers();

        ObjectMapper mapper = new ObjectMapper();
        InputStream inputStream = null;
        List<Repository> actual;

        try {
            inputStream = InstrumentationRegistry.getContext().getResources().openRawResource(R.raw.test_multiply_repository);
            actual = Arrays.asList(mapper.readValue(inputStream, Repository[].class));
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        assertThat(actual, notNullValue());
        assertThat(actual.size(), is(2));
        assertThat(actual, everyItem(isIn(expected)));
    }

}
