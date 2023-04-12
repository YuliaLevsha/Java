import java.util.Date;

public class Contract {
    int id;
    int id_client;
    int id_car;
    Date date_issue;
    Date date_return;
    int sum;
    int id_rentalagency;
    public Contract(){}
    public Contract(int id, int id_client, int id_car, Date date_issue, Date date_return, int sum, int id_rentalagency) {
        this.id = id;
        this.id_client = id_client;
        this.id_car = id_car;
        this.date_issue = date_issue;
        this.date_return = date_return;
        this.sum = sum;
        this.id_rentalagency = id_rentalagency;
    }
    @Override
    public String toString(){
        return id + " " + id_client + " " + id_car + " " + date_issue + " " + date_return + " " + sum + "BYN" + " " + id_rentalagency;
    }
}
