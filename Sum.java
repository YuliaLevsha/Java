public class Sum implements Comparable{
    int id;
    String address_agency;
    int sum;
    public Sum(int id, String address_agency, int sum) {
        this.id = id;
        this.address_agency = address_agency;
        this.sum = sum;
    }
    public int getId() {
        return id;
    }
    public int getSum() {
        return sum;
    }
    public String getAddress_agency() {
        return address_agency;
    }
    @Override
    public String toString(){
        return address_agency + " " + sum + "BYN";
    }
    @Override
    public int compareTo(Object o) {
        int compareage = ((Sum) o).getSum();
        return this.sum - compareage;
    }
}
