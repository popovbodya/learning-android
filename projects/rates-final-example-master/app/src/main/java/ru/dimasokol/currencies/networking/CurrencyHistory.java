package ru.dimasokol.currencies.networking;

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
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

/**
 * @author Дмитрий Соколов <DPSokolov.SBT@sberbank.ru>
 */

@Root(name = "ValCurs")
public class CurrencyHistory {

    @Attribute(name = "ID")
    private String id;

    @Attribute(name = "DateRange1")
    private String fromDate;

    @Attribute(name = "DateRange2")
    private String toDate;

    @Attribute(name = "name")
    private String name;

    @ElementList(entry = "Record", inline = true, type = HistoryRecord.class, required = false)
    private List<HistoryRecord> mHistory;

    public String getId() {
        return id;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public String getName() {
        return name;
    }

    public List<HistoryRecord> getHistory() {
        if (mHistory == null) {
            return Collections.emptyList();
        }

        return mHistory;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("CurrencyHistory ");
        builder.append(System.identityHashCode(this)).append(" {");
        builder.append("id = ").append(id);
        builder.append(", from = ").append(fromDate.toString());
        builder.append(", to = ").append(toDate.toString());
        builder.append(", name = ").append(name);
        builder.append(", history size = ").append(mHistory.size()).append("}");

        return builder.toString();
    }

    public static CurrencyHistory readFromStream(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream, Charset.forName("windows-1251")));

        RegistryMatcher m = new RegistryMatcher();
        m.bind(Double.class, new DoubleTransformer());
        Serializer serializer = new Persister(m);

        try {
            return serializer.read(CurrencyHistory.class, reader);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}
