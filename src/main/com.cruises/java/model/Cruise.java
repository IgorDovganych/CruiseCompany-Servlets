package model;

import java.util.Date;
import java.util.List;

public class Cruise {
    int id;
    Ship ship;
    List<String> route;
    Date startDate;
    Date endDate;
    int price;
    boolean isActive;

    public Cruise(int id, Ship ship, List<String> route, Date start_date, Date endDate, int price) {
        this.id = id;
        this.ship = ship;
        this.route = route;
        this.startDate = start_date;
        this.endDate = endDate;
        this.price = price;
    }

    public Cruise(int id, Ship ship, List<String> route, Date start_date, Date endDate, int price, boolean isActive) {
        this.id = id;
        this.ship = ship;
        this.route = route;
        this.startDate = start_date;
        this.endDate = endDate;
        this.price = price;
        this.isActive = isActive;
    }


    public int getPrice() {
        return price;
    }

    public int getId() {
        return id;
    }

    public Ship getShip() {
        return ship;
    }

    public boolean getIsActive() {
        return isActive;
    }


    public List<String> getRoute() {
        return route;
    }

    public String getRouteString(){
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < route.size(); i++) {
            str.append(route.get(i));
            if(i!=route.size()-1){
                str.append(" - ");
            }
        }
        return str.toString();
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    @Override
    public String toString() {
        return "Cruise{" +
                "id=" + id +
                ", ship=" + ship +
                ", route=" + route +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", price=" + price +
                '}';
    }
}
