package ru.dimasokol.currencies.networking;

import org.simpleframework.xml.transform.Transform;

/**
 * @author Дмитрий Соколов <me@dimasokol.ru>
 */

public class DoubleTransformer implements Transform<Double> {
    @Override
    public Double read(String s) throws Exception {
        return Double.parseDouble(s.replace(",", "."));
    }

    @Override
    public String write(Double aDouble) throws Exception {
        return Double.toString(aDouble);
    }

}
