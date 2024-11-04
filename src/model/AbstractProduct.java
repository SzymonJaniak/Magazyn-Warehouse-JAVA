package model;

public abstract class AbstractProduct {
    protected String name;
    protected double price;
    protected int quantity;
    protected Category category;

    public AbstractProduct(String name, double price, int quantity, Category category) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }

    public abstract AbstractProduct cloneProduct();


    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public Category getCategory() {
        return category;
    }
}
