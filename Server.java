import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Server extends Thread{
    ServerSocket serverSocket;
    public Server(){
        try{
            serverSocket = new ServerSocket(1234);
            System.out.println(serverSocket.toString());
        }
        catch (IOException e){
            fail(e, "Couldn't start server.");
        }
        System.out.println("Server is running...");
        this.start();
    }
    public static void fail(Exception e, String str){
        System.out.println(str + "." + e);
    }
    @Override
    public void run(){
        try{
            while (true && SeverConfig.getSign().run){
                Socket client = serverSocket.accept(); //ожидание запроса на соединение
                Connection connection = new Connection(client);
                SeverConfig.getSign().list.add(client);
            }
        }
        catch (IOException e){
            fail(e, "Not listening.");
        }
        finally {
            try{
                serverSocket.close();//закрываем сокет клиента
            }
            catch (IOException e){

            }
        }
    }
    public static void main(String args[]){
        new Server();
    }
}
class Connection extends Thread{
    protected Socket netClient;
    protected BufferedReader fromClient;
    protected PrintStream toClient;
    public Connection(Socket client){
        netClient = client;
        try{
            fromClient = new BufferedReader(new InputStreamReader(netClient.getInputStream()));//поток ввода
            toClient = new PrintStream(netClient.getOutputStream());//поток вывода
        }
        catch (IOException e){
            try{
                netClient.close();
            }
            catch (IOException e1){
                System.err.println("Unable to set up streams" + e1);
                return;
            }
        }
        this.start();
    }
    @Override
    public void run(){
        String fio, address, phone_number, serial_number, password, passport_number, status;
        try{
            java.sql.Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sever", "root", "27042003mnb123qwe123qwe");
            Class.forName("com.mysql.cj.jdbc.Driver");

            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM client";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                fio = resultSet.getString("fio");
                address = resultSet.getString("address");
                phone_number = resultSet.getString("phone_number");
                serial_number = resultSet.getString("serial_number");
                passport_number = resultSet.getString("passport_number");
                password = resultSet.getString("password");
                status = resultSet.getString("status");
                Client client = new Client(id, fio, address, phone_number, serial_number, passport_number, password, status);
                SeverConfig.getSign().clients.add(client);
            }
            while (SeverConfig.getSign().run) {
                boolean a = false;
                toClient.println("ФИО: ");//запись в входной поток     передаем клиенту
                fio = fromClient.readLine();//чтение из входного потока
                for (Client listclient : SeverConfig.getSign().clients) {
                    if (listclient.fio.compareTo(fio) == 0) {
                        toClient.println("Этот пользователь уже зарегестрирован!");
                        a = true;
                        break;
                    }
                }
                if (a != true) {
                    toClient.println("Адрес: ");
                    address = fromClient.readLine();
                    toClient.println("Телефон: ");
                    phone_number = fromClient.readLine();
                    toClient.println("Серия паспорта: ");
                    serial_number = fromClient.readLine();
                    toClient.println("Номер паспорта: ");
                    passport_number = fromClient.readLine();
                    toClient.println("Пароль: ");
                    password = fromClient.readLine();
                    toClient.println("Статус: ");
                    status = fromClient.readLine();
                    System.out.println("successfully registered in");
                }
                String pass = null;
                toClient.println("Авторизация");
                toClient.println("ФИО: ");//запись в входной поток     передаем клиенту
                fio = fromClient.readLine();//чтение из входного потока
                for (Client listclient : SeverConfig.getSign().clients) {
                    if (listclient.fio.compareTo(fio) == 0) {
                        toClient.println("Пароль: ");
                        password = fromClient.readLine();
                        if (listclient.fio.compareTo(fio) == 0 && listclient.password.compareTo(password) == 0) {
                            System.out.println(fio + " " + "logged in");
                            pass = password;
                        } else {
                            toClient.println("Не верный пароль!");
                            break;
                        }
                    }
                }
                for (Client listclient1 : SeverConfig.getSign().clients) {
                    if (listclient1.fio.compareTo(fio) == 0 && listclient1.status.compareTo("Пользователь") == 0 && listclient1.password.compareTo(pass) == 0) {
                        while (SeverConfig.getSign().run) {
                            String number1 = fromClient.readLine();
                            if(number1.compareTo("1") == 0) {
                                System.out.println("Просмотр данных из бд пункты проката и автомобили, которые доступны у них.");
                            }
                            else if(number1.compareTo("2") == 0){
                                    toClient.println("Номер машины, которой хотите арендовать: ");
                                    String id_car = fromClient.readLine();
                                    toClient.println("Дата аренды машины: ");
                                    String date_issue = fromClient.readLine();
                                    toClient.println("Дата возврата машины: ");
                                    String date_return = fromClient.readLine();
                                System.out.println("Аренда машины.");
                            }
                            else if(number1.compareTo("3") == 0){
                                toClient.println("Номер договора, который хотите поменять: ");
                                String id = fromClient.readLine();
                                toClient.println("Номер машины, которую хотите поменять: ");
                                String id_car = fromClient.readLine();
                                toClient.println("Дата аренды машины, которую хотите поменять: ");
                                String date_issue = fromClient.readLine();
                                toClient.println("Дата возврата машины, которую хотите поменять: ");
                                String date_return = fromClient.readLine();
                                System.out.println("Редактирование договора.");
                            }
                            else if(number1.compareTo("4") == 0){
                                toClient.println("Индекс контракта, который хотите удалить: ");//запись в входной поток     передаем клиенту
                                String contract_index = fromClient.readLine();//чтение из входного потока
                                System.out.println("Удаление договора.");
                            }
                            else if(number1.compareTo("5") == 0){
                                System.out.println("Просмотр данных из бд контракты, которые доступны текущему пользователю.");
                            }
                            else if(number1.compareTo("6") == 0){
                                System.out.println("Просмотр данных из бд клиенты, который действителен сейчас.");
                            }
                            else if(number1.compareTo("7") == 0){
                                toClient.println("ФИО: ");//запись в входной поток     передаем клиенту
                                fio = fromClient.readLine();//чтение из входного потока
                                toClient.println("Адрес: ");
                                address = fromClient.readLine();
                                toClient.println("Телефон: ");
                                phone_number = fromClient.readLine();
                                toClient.println("Серия паспорта: ");
                                serial_number = fromClient.readLine();
                                toClient.println("Номер паспорта: ");
                                passport_number = fromClient.readLine();
                                toClient.println("Пароль: ");
                                password = fromClient.readLine();
                                toClient.println("Статус: ");
                                status = fromClient.readLine();
                                System.out.println("Редактирование текущего пользователя.");
                            }
                            else if(number1.compareTo("8") == 0){
                                System.out.println("Удаление пользователя.");
                            }
                            else if(number1.compareTo("9") == 0){
                                toClient.println("Марка машины, которую хотите найти: ");//запись в входной поток     передаем клиенту
                                String brand = fromClient.readLine();//чтение из входного потока
                                System.out.println("Поиск машины по марке.");
                            }
                            else {
                                System.out.println("Exit!");
                            }
                        }
                    }
                }
                for (Client listclient : SeverConfig.getSign().clients) {
                    if (listclient.status.compareTo("Админ") == 0) {
                        while (true) {
                            String number2 = fromClient.readLine();
                            if(number2.compareTo("1") == 0) {
                                toClient.println("Марка: ");//запись в входной поток     передаем клиенту
                                String brand = fromClient.readLine();//чтение из входного потока
                                toClient.println("Стоимость: ");//запись в входной поток     передаем клиенту
                                String cost = fromClient.readLine();//чтение из входного потока
                                toClient.println("Тип: ");//запись в входной поток     передаем клиенту
                                String type = fromClient.readLine();//чтение из входного потока
                                toClient.println("Стоимость аренды за 1 день: ");//запись в входной поток     передаем клиенту
                                String rent_cost = fromClient.readLine();//чтение из входного потока
                                System.out.println("Машина.");
                            }
                            else if (number2.compareTo("2") == 0) {
                                toClient.println("Индекс машины, который хотите поменять: ");//запись в входной поток     передаем клиенту
                                String id = fromClient.readLine();//чтение из входного потока
                                toClient.println("Марка: ");//запись в входной поток     передаем клиенту
                                String brand = fromClient.readLine();//чтение из входного потока
                                toClient.println("Стоимость: ");//запись в входной поток     передаем клиенту
                                String cost = fromClient.readLine();//чтение из входного потока
                                toClient.println("Тип: ");//запись в входной поток     передаем клиенту
                                String type = fromClient.readLine();//чтение из входного потока
                                toClient.println("Стоимость аренды за 1 день: ");//запись в входной поток     передаем клиенту
                                String rent_cost = fromClient.readLine();//чтение из входного потока
                                System.out.println("Машина изменение.");
                            }
                            else if (number2.compareTo("3") == 0) {
                                toClient.println("Индекс машины, которую хотите удалить: ");//запись в входной поток     передаем клиенту
                                String car_index = fromClient.readLine();//чтение из входного потока
                                System.out.println("Удаление машины.");
                            }
                            else if (number2.compareTo("4") == 0) {
                                toClient.println("Адрес: ");//запись в входной поток     передаем клиенту
                                String address_agency = fromClient.readLine();//чтение из входного потока
                                toClient.println("Индекс машины: ");//запись в входной поток     передаем клиенту
                                String id_car = fromClient.readLine();//чтение из входного потока
                                System.out.println("Пункт проката.");
                            }
                            else if (number2.compareTo("5") == 0) {
                                toClient.println("Индекс пункта проката, который хотите поменять: ");//запись в входной поток     передаем клиенту
                                String id = fromClient.readLine();//чтение из входного потока
                                toClient.println("Адрес: ");//запись в входной поток     передаем клиенту
                                String address_agency = fromClient.readLine();//чтение из входного потока
                                toClient.println("Индекс машины: ");//запись в входной поток     передаем клиенту
                                String id_car = fromClient.readLine();//чтение из входного потока
                                System.out.println("Пункт проката изменение.");
                            }
                            else if (number2.compareTo("6") == 0) {
                                toClient.println("Адрес пункта проката, который хотите удалить: ");//запись в входной поток     передаем клиенту
                                String rental_agency_address = fromClient.readLine();//чтение из входного потока
                                System.out.println("Удаление пункта проката.");
                            }
                            else if (number2.compareTo("7") == 0) {
                                System.out.println("Просмотр показателей прибыли.");
                            }
                            else {
                                System.out.println("Exit!");
                            }
                        }
                    }
                }
            }
           connection.close();
        }
        catch (IOException e){

        } catch (NullPointerException e){

        } catch (Exception ex){
            System.out.println("Connection failed..." + ex);
        }
        finally {
            try{
                fromClient.close();//закрываем поток ввода
                toClient.close();//поток вывода
                netClient.close();//сокет клиента
            }
            catch (IOException e){

            }
        }
    }
}
