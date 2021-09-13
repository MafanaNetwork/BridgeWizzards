package me.TahaCheji.itemData;

public enum AbilityType {

    LEFT_CLICK("ON HIT"),
    RIGHT_CLICK("RIGHT CLICK"),
    NONE("");

    private final String text;

    AbilityType(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

}
