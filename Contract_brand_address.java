import java.util.Date;

public class Contract_brand_address {
    int id;
    Date date_issue;
    Date date_return;
    int sum;
    String address_agency;
    String brand;
    String fio;
    public Contract_brand_address(int id, Date date_issue, Date date_return, int sum, String address_agency, String brand, String fio) {
        this.id = id;
        this.date_issue = date_issue;
        this.date_return = date_return;
        this.sum = sum;
        this.address_agency = address_agency;
        this.brand = brand;
        this.fio = fio;
    }
    @Override
    public String toString(){
        return id + " " + date_issue + " " + date_return + " " + sum + "BYN" + " " + address_agency + " " + brand;
    }
}
