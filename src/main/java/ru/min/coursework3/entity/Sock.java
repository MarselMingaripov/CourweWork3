package ru.min.coursework3.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
public class Sock implements Cloneable{

    private Color color;
    private int size;
    private int cottonPercentage;
    private int stockBalance;

    public Sock(Color color, Size size, int cottonPercentage) {
        this.color = color;
        this.size = size.getDescription();
        this.cottonPercentage = cottonPercentage;
    }

    public int getStockBalance() {
        return stockBalance;
    }

    public void setStockBalance(int stockBalance) {
        this.stockBalance = stockBalance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sock sock = (Sock) o;
        return size == sock.size && cottonPercentage == sock.cottonPercentage && color == sock.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, size, cottonPercentage);
    }

    @Override
    public Sock clone() {
        try {
            return (Sock) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
