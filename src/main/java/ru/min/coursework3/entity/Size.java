package ru.min.coursework3.entity;

public enum Size {
    THIRTY(30),
    THIRTY_ONE(31),
    THIRTY_TWO(32),
    THIRTY_THREE(33),
    THIRTY_FOUR(34),
    THIRTY_FIVE(35),
    THIRTY_SIX(36),
    THIRTY_SEVEN(37),
    THIRTY_EIGHT(38),
    THIRTY_NINE(39),
    FOURTY(40),
    FOURTY_ONE(41),
    FOURTY_THREE(42),
    FOURTY_FOUR(43),
    FOURTY_FIVE(44),
    FOURTY_SIX(45),
    FOURTY_SEVEN(46),
    FOURTY_EIGHT(47),
    FOURTY_NINE(48);


    private final int description;

    Size(int description) {
        this.description = description;
    }

    public int getDescription() {
        return description;
    }
}
