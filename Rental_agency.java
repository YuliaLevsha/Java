public class Rental_agency extends Car{
    int id;
    String address_agency;
    int id_car;
    public Rental_agency(){}
    public Rental_agency(int id, String address_agency, int id_car){
        this.id = id;
        this.address_agency = address_agency;
        this.id_car = id_car;
    }
    @Override
    public String toString() {//убрала айди и айди машины
        return "Адрес: " + address_agency;
    }
}
