public class Car {
    int id;
    String brand;
    int cost;
    String type;
    int rent_cost;
    public Car(){}
    public Car(int id, String brand, int cost, String type, int rent_cost){
        this.id = id;
        this.brand = brand;
        this.cost = cost;
        this.type = type;
        this.rent_cost = rent_cost;
    }
    @Override
    public String toString(){//убрала айди
        return "Марка: " + brand + " " + "Стоимость машины: " + cost + "BYN" + " " + "Тип: " + type + " " + "Стоимость аренды за 1 день: " + rent_cost + "BYN";
    }
}
