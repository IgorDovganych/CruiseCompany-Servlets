package model;

public class Ship {
    int id;
    int capacity;
    String model;

    public Ship(int id, int capacity, String model) {
        this.id = id;
        this.capacity = capacity;
        this.model = model;
    }

    public int getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getModel() {
        return model;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return model;
    }

}
