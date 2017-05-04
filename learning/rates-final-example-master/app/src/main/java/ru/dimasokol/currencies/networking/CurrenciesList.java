package ru.dimasokol.currencies.networking;

import android.support.annotation.Nullable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.transform.RegistryMatcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;

import ru.dimasokol.currencies.utils.DateFormatUtils;


/**
 * Список курсов валют
 *
 * @author Дмитрий Соколов <me@dimasokol.ru>
 */
@Root(name = "ValCurs")
public class CurrenciesList implements Serializable {

    @Attribute(name = "Date")
    private String mDate;

    @Attribute(name = "name")
    private String mName;

    @ElementList(entry = "Valute", inline = true)
    private List<Currency> currencies;

    @Nullable
    public Date getDate() {
        return DateFormatUtils.dateFromString(mDate);
    }

    public String getName() {
        return mName;
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }

    public static CurrenciesList readFromStream(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream, Charset.forName("windows-1251")));

        RegistryMatcher m = new RegistryMatcher();
        m.bind(Double.class, new DoubleTransformer());
        Serializer serializer = new Persister(m);

        try {
            return serializer.read(CurrenciesList.class, reader);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}