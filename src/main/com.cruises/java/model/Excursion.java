package model;

public class Excursion {
    int id;
    String name;
    String portName;
    int price;
    String description;
    boolean isActive;

    public Excursion() {
    }

    public Excursion(int id, String name, String portName, int price, String description, boolean isActive) {
        this.id = id;
        this.name = name;
        this.portName = portName;
        this.price = price;
        this.description = description;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPortName() {
        return portName;
    }

    public int getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public boolean getIsActive() {
        return isActive;
    }

    @Override
    public String toString() {
        return "Excursion{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", portName='" + portName + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
