import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SeverConfig {
    public List<Socket> list = new ArrayList<>();
    public List<Client> clients = new ArrayList<>();
    public List<Rental_agency> rental_agencies = new ArrayList<>();
    public List<Car> cars = new ArrayList<>();
    public List<Contract> contracts = new ArrayList<>();
    public List<Rental_agency_car> rental_agencies_cars = new ArrayList<>();
    public List<Contract_brand_address> contract_brand_addresses = new ArrayList<>();
    public List<Sum> sums = new ArrayList<>();
    public boolean run = true;
    private static SeverConfig sign = null;
    private SeverConfig(){

    }
    public static SeverConfig getSign(){
        if(sign == null){
            sign = new SeverConfig();
        }
        return sign;
    }
}
