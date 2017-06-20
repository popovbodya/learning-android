package ru.boyda.popov.usergit.parsing;

import android.support.test.InstrumentationRegistry;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import ru.boyda.popov.usergit.networking.Response;
import ru.boyda.popov.usergit.pojo.User;
import ru.boyda.popov.usergit.test.R;

import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isIn;
import static ru.boyda.popov.usergit.parsing.ParsingUserHelper.createOneSpecifiedUser;

public class ParsingUserTest {

    @Test
    public void testParseSingleUser() throws IOException {
        User expected = createOneSpecifiedUser("MoonightCS", 37.651367,
                "https://api.github.com/users/MoonightCS", "https://avatars3.githubusercontent.com/u/9767952?v=3");
        ObjectMapper mapper = new ObjectMapper();
        InputStream inputStream = null;
        Response response;

        try {
            inputStream = InstrumentationRegistry.getContext().getResources().openRawResource(R.raw.test_single_user);
            response = mapper.readValue(inputStream, Response.class);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        assertThat(response, notNullValue());
        assertThat(response.getTotalCount(), is(1));
        User actual = response.getUserList().get(0);
        assertThat(actual, is(expected));
    }

    @Test
    public void testParseMultiplyUsers() throws IOException {
        List<User> expected = ParsingUserHelper.createMultiplySpecifiedUsers();
        int expectedSize = expected.size();
        ObjectMapper mapper = new ObjectMapper();
        InputStream inputStream = null;
        Response response;

        try {
            inputStream = InstrumentationRegistry.getContext().getResources().openRawResource(R.raw.test_multiply_users);
            response = mapper.readValue(inputStream, Response.class);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        assertThat(response, notNullValue());
        assertThat(response.getTotalCount(), is(expectedSize));
        List<User> actualUsers = response.getUserList();
        assertThat(actualUsers, everyItem(isIn(expected)));
    }

}
