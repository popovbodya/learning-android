package ru.boyda.popov.homework_2;


import android.graphics.Color;

public class ToolsBundle {

    static final int CURVE_LINE = 1;
    static final int LINE = 2;
    static final int RECTANGLE = 3;
    static final int SQUARE = 4;
    static final int CIRCLE = 5;

    private int shapeType;
    private Color color;

    public ToolsBundle(int shapeType, Color color) {
        this.shapeType = shapeType;
        this.color = color;
    }

    public int getShapeType() {
        return shapeType;
    }

    public Color getColor() {
        return color;
    }
}
