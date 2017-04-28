package ru.boyda.popov.currencyconverter.networking;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Валюта
 */
@Root(name = "Valute")
public class Currency {
    @Attribute(name = "ID")
    private String mId;

    @Element(name = "Name")
    private String mName;

    @Element(name = "NumCode")
    private int mNumCode;

    @Element(name = "CharCode")
    private String mCharCode;

    @Element(name = "Nominal")
    private Double mNominal;

    @Element(name = "Value")
    private Double mValue;

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public int getNumCode() {
        return mNumCode;
    }

    public String getCharCode() {
        return mCharCode;
    }

    public Double getNominal() {
        return mNominal;
    }

    public Double getValue() {
        return mValue;
    }
}