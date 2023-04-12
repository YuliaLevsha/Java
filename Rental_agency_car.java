public class Rental_agency_car {
    int id;
    String address_agency;
    int id_car;
    String brand;
    int cost;
    String type;
    int rent_cost;
    public Rental_agency_car(int id, String address_agency, int id_car, String brand, int cost, String type, int rent_cost) {
        this.id = id;
        this.address_agency = address_agency;
        this.id_car = id_car;
        this.brand = brand;
        this.cost = cost;
        this.type = type;
        this.rent_cost = rent_cost;
    }
    public String getAddress_agency() {
        return address_agency;
    }

    public int getId() {
        return id;
    }
    public int getId_car() {
        return id_car;
    }
    public String getBrand() {
        return brand;
    }
    public int getCost() {
        return cost;
    }

    public String getType() {
        return type;
    }
    public int getRent_cost() {
        return rent_cost;
    }

    @Override
    public String toString(){//уюрала айди айди машины
        return "Адрес: " + address_agency + " " + "Марка: " + brand + " " + "Стоимость машины: " + cost + "BYN" + " " + "Тип: "+ type + " " + "Стоимость аренды за 1 день: " + rent_cost + "BYN";
    }
}
