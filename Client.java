import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.*;
import java.util.Date;
import java.util.stream.Collectors;

public class Client {
    int id;
    String fio;
    String address;
    String phone_number;
    String serial_number;
    String passport_number;
    String password;
    String status;
    public Client(){}
    public Client(int id, String fio, String address, String phone_number, String serial_number, String passport_number, String password, String status){
        this.id = id;
        this.fio = fio;
        this.address = address;
        this.phone_number = phone_number;
        this.serial_number = serial_number;
        this.passport_number = passport_number;
        this.password = password;
        this.status = status;
    }
    @Override
    public String toString() {
        return "ФИО: " + fio + " " + "Адрес: " + address + " " + "Телефон: " + phone_number + " " + "Серия паспорта: " + serial_number + " " + "Номер паспорта: " + passport_number + " " + "Пароль: " + password + " " + "Статус доступа: " + status;
    }
    public static void main(String args[]) throws IOException {
        int client_current_index = 0;
        String client_current_fio = "";
        Socket clientSocket = null;
        PrintStream out = null;
        BufferedReader in = null;
        BufferedReader stdin = null;
        try{
            clientSocket = new Socket("127.0.0.1", 1234);
            out = new PrintStream(clientSocket.getOutputStream()); //отправляем серверу
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); //получаем от сервера
            stdin = new BufferedReader(new InputStreamReader((System.in))); //ввод с клавиатуры в консоль
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sever", "root", "27042003mnb123qwe123qwe");
            Class.forName("com.mysql.cj.jdbc.Driver");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM client");
            while (resultSet.next()){
                Client client = new Client(resultSet.getInt("id"), resultSet.getString("fio"), resultSet.getString("address"), resultSet.getString("phone_number"), resultSet.getString("serial_number"), resultSet.getString("passport_number"), resultSet.getString("password"), resultSet.getString("status"));
                SeverConfig.getSign().clients.add(client);
            }

            statement = connection.createStatement();
            resultSet = statement.executeQuery("select  * from contract");
            while (resultSet.next()) {
                Contract contract = new Contract(resultSet.getInt("id"), resultSet.getInt("id_client"), resultSet.getInt("id_car"), resultSet.getDate("date_issue"), resultSet.getDate("date_return"), resultSet.getInt("sum"), resultSet.getInt("id_rentalagency"));
                SeverConfig.getSign().contracts.add(contract);
            }

            statement = connection.createStatement();
            resultSet = statement.executeQuery("select  * from car");
            while (resultSet.next()) {
                Car car = new Car(resultSet.getInt("id"), resultSet.getString("brand"), resultSet.getInt("cost"), resultSet.getString("type"), resultSet.getInt("rent_cost"));
                SeverConfig.getSign().cars.add(car);
            }

            statement = connection.createStatement();
            resultSet = statement.executeQuery("select  * from rental_agency\n" +
                    "INNER JOIN car c on rental_agency.id_car = c.id");
            while (resultSet.next()) {
                Rental_agency_car rentalAgency_car = new Rental_agency_car(resultSet.getInt("id"), resultSet.getString("address_agency"), resultSet.getInt("id_car"), resultSet.getString("brand"), resultSet.getInt("cost"), resultSet.getString("type"), resultSet.getInt("rent_cost"));
                SeverConfig.getSign().rental_agencies_cars.add(rentalAgency_car);
            }

            statement = connection.createStatement();
            resultSet = statement.executeQuery("select contract.id, date_issue, date_return, sum, address_agency, brand, fio from contract\n" +
                    "INNER JOIN car c on contract.id_car = c.id\n" +
                    "INNER JOIN client c2 on contract.id_client = c2.id\n" +
                    "INNER JOIN rental_agency ra on c.id = ra.id_car");
            while (resultSet.next()) {
                Contract_brand_address contract_brand_address = new Contract_brand_address(resultSet.getInt("id"), resultSet.getDate("date_issue"), resultSet.getDate("date_return"), resultSet.getInt("sum"), resultSet.getString("address_agency"), resultSet.getString("brand"), resultSet.getString("fio"));
                SeverConfig.getSign().contract_brand_addresses.add(contract_brand_address);
            }

            String sql = "SELECT rental_agency.address_agency,sum, contract.id from contract, rental_agency\n" +
                    "where id_client = 2 AND id_rentalagency = rental_agency.id";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                Sum sum = new Sum(resultSet.getInt("id"), resultSet.getString("address_agency"), resultSet.getInt("sum"));
                SeverConfig.getSign().sums.add(sum);
            }

            statement = connection.createStatement();
            resultSet = statement.executeQuery("select  * from rental_agency");
            while (resultSet.next()) {
                Rental_agency rental_agency = new Rental_agency(resultSet.getInt("id"), resultSet.getString("address_agency"), resultSet.getInt("id_car"));
                SeverConfig.getSign().rental_agencies.add(rental_agency);
            }

            System.out.println("Добро пожаловать в сетевое приложение Прокат автомобилей!");
            System.out.println("Регистрация");
            boolean a = false;
            String fio = in.readLine();
            System.out.println(fio);
            String fio_stdin = stdin.readLine();
            out.println(fio_stdin);
            for (Client listclient : SeverConfig.getSign().clients) {
                if (listclient.fio.compareTo(fio_stdin) == 0) {
                    String result = in.readLine();
                    System.out.println(result);
                    a = true;
                }
            }
            if(a != true){
                String address = in.readLine();
                System.out.println(address);
                String address_stdin = stdin.readLine();
                out.println(address_stdin);

                String phone_number = in.readLine();
                System.out.println(phone_number);
                String phone_number_stdin = stdin.readLine();
                out.println(phone_number_stdin);

                String serial_number = in.readLine();
                System.out.println(serial_number);
                String serial_number_stdin = stdin.readLine();
                out.println(serial_number_stdin);

                String passport_number = in.readLine();
                System.out.println(passport_number);
                String passport_number_stdin = stdin.readLine();
                out.println(passport_number_stdin);

                String password = in.readLine();
                System.out.println(password);
                String password_stdin = stdin.readLine();
                out.println(password_stdin);

                String status = in.readLine();
                System.out.println(status);
                String status_stdin = stdin.readLine();
                out.println(status_stdin);
                sql = "INSERT INTO client (id, fio, address, phone_number, serial_number, passport_number, password, status) values (?, ?, ?, ?, ?, ?, ?, ?)";

                int i = SeverConfig.getSign().clients.size() + 1;
                try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                    preparedStatement.setInt(1, i);
                    preparedStatement.setString(2, fio_stdin);
                    preparedStatement.setString(3, address_stdin);
                    preparedStatement.setString(4, phone_number_stdin);
                    preparedStatement.setString(5, serial_number_stdin);
                    preparedStatement.setString(6, passport_number_stdin);
                    preparedStatement.setString(7, password_stdin);
                    preparedStatement.setString(8, status_stdin);
                    preparedStatement.execute();
                }
                catch (SQLException e){
                    System.out.println(e);
                }
            }
            String autorization = in.readLine();
            System.out.println(autorization);
            //System.out.println("Авторизация");
            fio = in.readLine();
            System.out.println(fio);
            fio_stdin = stdin.readLine();
            out.println(fio_stdin);
            String pass = null;
            for(Client listclient: SeverConfig.getSign().clients){
                if(listclient.fio.compareTo(fio_stdin) == 0){
                    String password = in.readLine();
                    System.out.println(password);
                    String password_stdin = stdin.readLine();
                    out.println(password_stdin);
                    if(listclient.fio.compareTo(fio_stdin) == 0 && listclient.password.compareTo(password_stdin) == 0) {
                        pass = password_stdin;
                    }
                    else {
                        String result = in.readLine();
                        System.out.println(result);
                    }
                }
            }
            for(Client listclient: SeverConfig.getSign().clients) {
                if (listclient.fio.compareTo(fio_stdin) == 0 && listclient.status.compareTo("Пользователь") == 0 && listclient.password.compareTo(pass) == 0) {
                    client_current_index = SeverConfig.getSign().clients.indexOf(listclient);
                    client_current_fio = SeverConfig.getSign().clients.get(client_current_index).fio;
                    while (true) {
                        System.out.println("1 - Просмотр пунктов проката и автомобилей, которые есть в них.");
                        System.out.println("2 - Арендовать машину.");
                        System.out.println("3 - Изменить договор.");
                        System.out.println("4 - Отменить договор.");
                        System.out.println("5 - Просмотреть свои договора.");
                        System.out.println("6 - Просмотреть данные пользователя.");
                        System.out.println("7 - Редактировать пользователя.");
                        System.out.println("8 - Удалить пользователя.");
                        System.out.println("9 - Поиск машины.");
                        System.out.println("10 - Выход.");
                        String number1 = stdin.readLine();
                        switch (number1) {
                            case "1":
                                out.println(number1);
                                Map<String, List<Rental_agency_car>> rental = SeverConfig.getSign().rental_agencies_cars.stream().collect(Collectors.groupingBy(Rental_agency_car::getAddress_agency));
                                for (Map.Entry<String, List<Rental_agency_car>> item : rental.entrySet()) {
                                    System.out.println(item.getKey());
                                    System.out.printf("%57s%n", "-----------------------------------------------------------------------------------------");
                                    System.out.printf("%1s%2s%1s%32s%1s%10s%1s%12s%1s%3s%1s%n", "|","id","|","Марка","|", "Цена машины","|", "Тип","|", "Стоимость аренды за 1 день","|");
                                    System.out.printf("%57s%n", "-----------------------------------------------------------------------------------------");
                                    for (Rental_agency_car rental_agency_car : item.getValue()) {
                                        System.out.printf("%1s%2s%1s%32s%1s%11d%1s%12s%1s%26d%1s%n", "|",rental_agency_car.getId_car(),"|",rental_agency_car.getBrand(),"|", rental_agency_car.getCost(),"|", rental_agency_car.getType(),"|", rental_agency_car.getRent_cost(),"|");
                                        System.out.printf("%57s%n", "-----------------------------------------------------------------------------------------");
                                    }
                                    System.out.println();
                                }
                                break;
                            case "2":
                                out.println(number1);
                                String id_car = in.readLine();
                                System.out.println(id_car);
                                String id_car_stdin = stdin.readLine();
                                out.println(id_car_stdin);
                                int id_car_int = Integer.parseInt(id_car_stdin);

                                String date_issue = in.readLine();
                                System.out.println(date_issue);
                                String date_issue_str_stdin = stdin.readLine();
                                out.println(date_issue_str_stdin);
                                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                Date date_issue_date = dateFormat.parse(date_issue_str_stdin);
                                java.sql.Date sqlDate = new java.sql.Date(date_issue_date.getTime());

                                String date_return = in.readLine();
                                System.out.println(date_return);
                                String date_return_str_stdin = stdin.readLine();
                                out.println(date_return_str_stdin);
                                Date date_return_date = dateFormat.parse(date_return_str_stdin);
                                java.sql.Date sqlDate1 = new java.sql.Date(date_return_date.getTime());

                                String sql1 = "UPDATE contract\n" +
                                        "set sum = DATEDIFF(date_return, date_issue) * (SELECT rent_cost from car WHERE car.id = id_car),\n" +
                                        "    id_rentalagency = (SELECT id from rental_agency WHERE contract.id_car = rental_agency.id_car)\n" +
                                        "where sum > 0";
                                PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
                                sql = "INSERT INTO contract (id, id_client, id_car, date_issue, date_return, sum, id_rentalagency) values (?, ?, ?, ?, ?, ?, ?)";
                                int i = SeverConfig.getSign().contracts.size() + 1;
                                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                                    preparedStatement.setInt(1, i);
                                    preparedStatement.setInt(2, client_current_index + 1);
                                    preparedStatement.setInt(3, id_car_int);
                                    preparedStatement.setDate(4, sqlDate);
                                    preparedStatement.setDate(5, sqlDate1);
                                    preparedStatement.setInt(6, 1);
                                    preparedStatement.setInt(7, 1);
                                    preparedStatement.execute();
                                    preparedStatement1.execute();
                                } catch (SQLException e) {
                                    System.out.println(e);
                                }
                                break;
                            case "3":
                                out.println(number1);

                                String id = in.readLine();
                                System.out.println(id);
                                String id_stdin = stdin.readLine();
                                out.println(id_stdin);
                                int id_int = Integer.parseInt(id_stdin);

                                id_car = in.readLine();
                                System.out.println(id_car);
                                id_car_stdin = stdin.readLine();
                                out.println(id_car_stdin);
                                id_car_int = Integer.parseInt(id_car_stdin);

                                date_issue = in.readLine();
                                System.out.println(date_issue);
                                date_issue_str_stdin = stdin.readLine();
                                out.println(date_issue_str_stdin);
                                dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                date_issue_date = dateFormat.parse(date_issue_str_stdin);
                                sqlDate = new java.sql.Date(date_issue_date.getTime());

                                date_return = in.readLine();
                                System.out.println(date_return);
                                date_return_str_stdin = stdin.readLine();
                                out.println(date_return_str_stdin);
                                date_return_date = dateFormat.parse(date_return_str_stdin);
                                sqlDate1 = new java.sql.Date(date_return_date.getTime());
                                sql1 = "UPDATE contract\n" +
                                        "set sum = DATEDIFF(date_return, date_issue) * (SELECT rent_cost from car WHERE car.id = id_car),\n" +
                                        "    id_rentalagency = (SELECT id from rental_agency WHERE contract.id_car = rental_agency.id_car)\n" +
                                        "where sum > 0";
                                preparedStatement1 = connection.prepareStatement(sql1);
                                String sql2 = "UPDATE contract SET id = ?, id_car = ?, date_issue = ?, date_return = ?, sum = ?, id_rentalagency = ? WHERE id_client = ? AND id = ?";
                                try (PreparedStatement preparedStatement2 = connection.prepareStatement(sql2)) {
                                    preparedStatement2.setInt(1, id_int);
                                    preparedStatement2.setInt(2, id_car_int);
                                    preparedStatement2.setDate(3, sqlDate);
                                    preparedStatement2.setDate(4, sqlDate1);
                                    preparedStatement2.setInt(5, 1);
                                    preparedStatement2.setInt(6, 1);
                                    preparedStatement2.setInt(7, client_current_index + 1);
                                    preparedStatement2.setInt(8, id_int);
                                    preparedStatement2.execute();
                                } catch (SQLException e) {
                                    System.out.println(e);
                                }
                                preparedStatement1.execute();
                                break;
                            case "4":
                                out.println(number1);
                                String contract_index = in.readLine();
                                System.out.println(contract_index);
                                String contract_index_stdin = stdin.readLine();
                                int contract_index_int = Integer.parseInt(contract_index_stdin);
                                out.println(contract_index_stdin);
                                for (Contract contract: SeverConfig.getSign().contracts){
                                    if(contract.id_client == client_current_index && contract.id == contract_index_int){
                                        sql = "DELETE from contract WHERE id = ?";
                                        PreparedStatement preparedStatement = connection.prepareStatement(sql);
                                        preparedStatement.setInt(1, contract_index_int);
                                        preparedStatement.execute();
                                    }
                                }
                                break;
                            case "5":
                                out.println(number1);
                                System.out.printf("%40s%n", "------------------------------------------------------------------------------------------------------------------------------");
                                System.out.printf("%1s%30s%1s%30s%1s%30s%1s%15s%1s%15s%1s%n","|","Пункт проката","|","Марка машины","|","Дата получения","|","Дата возврата","|","Сумма заказа","|");
                                System.out.printf("%40s%n", "------------------------------------------------------------------------------------------------------------------------------");
                                for(Contract_brand_address contract_brand_address: SeverConfig.getSign().contract_brand_addresses){
                                    if(client_current_fio.compareTo(contract_brand_address.fio) == 0) {
                                        System.out.printf("%1s%30s%1s%30s%1s%30s%1s%15s%1s%15s%1s%n","|",contract_brand_address.address_agency,"|",contract_brand_address.brand,"|",contract_brand_address.date_issue,"|",contract_brand_address.date_return,"|",contract_brand_address.sum,"|");
                                        System.out.printf("%40s%n", "------------------------------------------------------------------------------------------------------------------------------");
                                    }
                                }
                                break;
                            case "6":
                                out.println(number1);
                                System.out.printf("%40s%n", "-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                                System.out.printf("%1s%30s%1s%30s%1s%30s%1s%15s%1s%15s%1s%30s%1s%15s%1s%n","|","ФИО","|","Адрес","|","Телефон","|","Серия паспорта","|","Номер паспорта","|","Пароль","|","Статус доступа","|");
                                System.out.printf("%40s%n", "-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                                for(Client listclient1: SeverConfig.getSign().clients) {
                                    if(listclient1.id == client_current_index + 1){
                                        System.out.printf("%1s%30s%1s%30s%1s%30s%1s%15s%1s%15s%1s%30s%1s%15s%1s%n","|",listclient1.fio,"|",listclient1.address,"|",listclient1.phone_number,"|",listclient1.serial_number,"|",listclient1.passport_number,"|",listclient1.password,"|",listclient1.status,"|");
                                        System.out.printf("%40s%n", "-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                                    }
                                }
                                break;
                            case "7":
                                out.println(number1);
                                fio = in.readLine();
                                System.out.println(fio);
                                fio_stdin = stdin.readLine();
                                out.println(fio_stdin);

                                String address = in.readLine();
                                System.out.println(address);
                                String address_stdin = stdin.readLine();
                                out.println(address_stdin);

                                String phone_number = in.readLine();
                                System.out.println(phone_number);
                                String phone_number_stdin = stdin.readLine();
                                out.println(phone_number_stdin);

                                String serial_number = in.readLine();
                                System.out.println(serial_number);
                                String serial_number_stdin = stdin.readLine();
                                out.println(serial_number_stdin);

                                String passport_number = in.readLine();
                                System.out.println(passport_number);
                                String passport_number_stdin = stdin.readLine();
                                out.println(passport_number_stdin);

                                String password = in.readLine();
                                System.out.println(password);
                                String password_stdin = stdin.readLine();
                                out.println(password_stdin);

                                String status = in.readLine();
                                System.out.println(status);
                                String status_stdin = stdin.readLine();
                                out.println(status_stdin);

                                sql2 = "UPDATE client SET fio = ?, address = ?, phone_number = ?, serial_number = ?, passport_number = ?, password = ?, status = ? WHERE id = ?";
                                try (PreparedStatement preparedStatement2 = connection.prepareStatement(sql2)) {
                                    preparedStatement2.setString(1, fio_stdin);
                                    preparedStatement2.setString(2, address_stdin);
                                    preparedStatement2.setString(3, phone_number_stdin);
                                    preparedStatement2.setString(4, serial_number_stdin);
                                    preparedStatement2.setString(5, passport_number_stdin);
                                    preparedStatement2.setString(6, password_stdin);
                                    preparedStatement2.setString(7, status_stdin);
                                    preparedStatement2.setInt(8, client_current_index + 1);
                                    preparedStatement2.execute();
                                } catch (SQLException e) {
                                    System.out.println(e);
                                }
                                break;
                            case "8":
                                out.println(number1);
                                for (Client client: SeverConfig.getSign().clients){
                                    if(client.id == client_current_index + 1){
                                        sql = "DELETE from client WHERE id = ?";
                                        PreparedStatement preparedStatement = connection.prepareStatement(sql);
                                        preparedStatement.setInt(1, client_current_index + 1);
                                        preparedStatement.execute();
                                    }
                                }
                                System.out.println("Exit");
                                return;
                            case "9":
                                out.println(number1);

                                String brand = in.readLine();
                                System.out.println(brand);
                                String brand_stdin = stdin.readLine();
                                out.println(brand_stdin);
                                for(Rental_agency_car rental_agency_car: SeverConfig.getSign().rental_agencies_cars){
                                    if(rental_agency_car.brand.compareTo(brand_stdin) == 0){
                                        System.out.println(rental_agency_car);
                                        break;
                                    }
                                }
                                break;
                            case "10":
                                out.println(number1);
                                System.out.println("Exit");
                                return;
                        }
                    }
                }
                else {continue;}
            }
            for(Client listclient: SeverConfig.getSign().clients) {
                if(listclient.fio.compareTo(fio_stdin) == 0 && listclient.status.compareTo("Админ") == 0){
                    while (true){
                        System.out.println("1 - Добавить новый автомобиль.");
                        System.out.println("2 - Изменить автомобиль.");
                        System.out.println("3 - Удалить автомобиль.");
                        System.out.println("4 - Добавить новый пункт проката.");
                        System.out.println("5 - Изменить пункт проката.");
                        System.out.println("6 - Удалить пункт проката.");
                        System.out.println("7 - Отображение прибыли фирмы.");
                        System.out.println("8 - Выход.");
                        String  number2 = stdin.readLine();
                        switch (number2){
                            case "1":
                                out.println(number2);
                                String brand = in.readLine();
                                System.out.println(brand);
                                String brand_stdin = stdin.readLine();
                                out.println(brand_stdin);

                                String cost = in.readLine();
                                System.out.println(cost);
                                String cost_stdin = stdin.readLine();
                                out.println(cost_stdin);
                                int cost_int = Integer.parseInt(cost_stdin);

                                String type = in.readLine();
                                System.out.println(type);
                                String type_stdin = stdin.readLine();
                                out.println(type_stdin);

                                String rent_cost = in.readLine();
                                System.out.println(rent_cost);
                                String rent_cost_stdin = stdin.readLine();
                                out.println(rent_cost_stdin);
                                int rent_cost_int = Integer.parseInt(rent_cost_stdin);

                                sql = "INSERT INTO car (id, brand, cost, type, rent_cost) values (?, ?, ?, ?, ?)";
                                int i = SeverConfig.getSign().cars.size() + 1;
                                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                                    preparedStatement.setInt(1, i);
                                    preparedStatement.setString(2, brand_stdin);
                                    preparedStatement.setInt(3, cost_int);
                                    preparedStatement.setString(4, type_stdin);
                                    preparedStatement.setInt(5, rent_cost_int);
                                    preparedStatement.execute();
                                } catch (SQLException e) {
                                    System.out.println(e);
                                }
                                break;
                            case "2":
                                out.println(number2);

                                String id = in.readLine();
                                System.out.println(id);
                                String id_stdin = stdin.readLine();
                                out.println(id_stdin);
                                int id_int = Integer.parseInt(id_stdin);

                                brand = in.readLine();
                                System.out.println(brand);
                                brand_stdin = stdin.readLine();
                                out.println(brand_stdin);

                                cost = in.readLine();
                                System.out.println(cost);
                                cost_stdin = stdin.readLine();
                                out.println(cost_stdin);
                                cost_int = Integer.parseInt(cost_stdin);

                                type = in.readLine();
                                System.out.println(type);
                                type_stdin = stdin.readLine();
                                out.println(type_stdin);

                                rent_cost = in.readLine();
                                System.out.println(rent_cost);
                                rent_cost_stdin = stdin.readLine();
                                out.println(rent_cost_stdin);
                                rent_cost_int = Integer.parseInt(rent_cost_stdin);

                                String sql2 = "UPDATE car SET brand = ?, cost = ?, type = ?, rent_cost = ? WHERE id = ?";
                                try (PreparedStatement preparedStatement2 = connection.prepareStatement(sql2)) {
                                    preparedStatement2.setString(1, brand_stdin);
                                    preparedStatement2.setInt(2, cost_int);
                                    preparedStatement2.setString(3, type_stdin);
                                    preparedStatement2.setInt(4, rent_cost_int);
                                    preparedStatement2.setInt(5, id_int);
                                    preparedStatement2.execute();
                                } catch (SQLException e) {
                                    System.out.println(e);
                                }
                                break;
                            case "3":
                                out.println(number2);
                                String car_index = in.readLine();
                                System.out.println(car_index);
                                String car_index_stdin = stdin.readLine();
                                int car_index_int = Integer.parseInt(car_index_stdin);
                                out.println(car_index_stdin);
                                for (Car car: SeverConfig.getSign().cars){
                                    if(car.id == car_index_int){
                                        sql = "DELETE from car WHERE id = ?";
                                        PreparedStatement preparedStatement = connection.prepareStatement(sql);
                                        preparedStatement.setInt(1, car_index_int);
                                        preparedStatement.execute();
                                    }
                                }
                                break;
                            case "4":
                                out.println(number2);
                                String address_agency = in.readLine();
                                System.out.println(address_agency);
                                String address_agency_stdin = stdin.readLine();
                                out.println(address_agency_stdin);

                                String id_car = in.readLine();
                                System.out.println(id_car);
                                String id_car_stdin = stdin.readLine();
                                out.println(id_car_stdin);
                                int id_car_int = Integer.parseInt(id_car_stdin);

                                sql = "INSERT INTO rental_agency (id, address_agency, id_car) values (?, ?, ?)";
                                i = SeverConfig.getSign().rental_agencies.size() + 1;
                                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                                    preparedStatement.setInt(1, i);
                                    preparedStatement.setString(2, address_agency_stdin);
                                    preparedStatement.setInt(3, id_car_int);
                                    preparedStatement.execute();
                                } catch (SQLException e) {
                                    System.out.println(e);
                                }
                                break;
                            case "5":
                                out.println(number2);

                                id = in.readLine();
                                System.out.println(id);
                                id_stdin = stdin.readLine();
                                out.println(id_stdin);
                                id_int = Integer.parseInt(id_stdin);

                                address_agency = in.readLine();
                                System.out.println(address_agency);
                                address_agency_stdin = stdin.readLine();
                                out.println(address_agency_stdin);

                                id_car = in.readLine();
                                System.out.println(id_car);
                                id_car_stdin = stdin.readLine();
                                out.println(id_car_stdin);
                                id_car_int = Integer.parseInt(id_car_stdin);
                                sql2 = "UPDATE rental_agency SET address_agency = ?, id_car = ? WHERE id = ?";
                                try (PreparedStatement preparedStatement2 = connection.prepareStatement(sql2)) {
                                    preparedStatement2.setString(1, address_agency_stdin);
                                    preparedStatement2.setInt(2, id_car_int);
                                    preparedStatement2.setInt(3, id_int);
                                    preparedStatement2.execute();
                                } catch (SQLException e) {
                                    System.out.println(e);
                                }
                                break;
                            case "6":
                                out.println(number2);

                                String rental_agency_address = in.readLine();
                                System.out.println(rental_agency_address);
                                String rental_agency_address_stdin = stdin.readLine();
                                out.println(rental_agency_address_stdin);
                                for (Rental_agency rental_agency: SeverConfig.getSign().rental_agencies){
                                    if(rental_agency.address_agency.compareTo(rental_agency_address_stdin) == 0){
                                        sql = "DELETE from rental_agency WHERE address_agency = ?";
                                        PreparedStatement preparedStatement = connection.prepareStatement(sql);
                                        preparedStatement.setString(1, rental_agency_address_stdin);
                                        preparedStatement.execute();
                                    }
                                }
                                break;
                            case "7":
                                out.println(number2);
                                Collections.sort(SeverConfig.getSign().sums);
                                Map<String, List<Sum>> sum = SeverConfig.getSign().sums.stream().collect(Collectors.groupingBy(Sum::getAddress_agency));
                                for (Map.Entry<String, List<Sum>> item : sum.entrySet()) {
                                    System.out.println(item.getKey());
                                    System.out.printf("%37s%n", "-------------------------------------");
                                    System.out.printf("%1s%2s%1s%32s%1s%n", "|","id","|","Стоимость заказа","|");
                                    System.out.printf("%37s%n", "-------------------------------------");
                                    for (Sum sum1 : item.getValue()) {
                                        System.out.printf("%1s%2s%1s%32s%1s%n", "|", sum1.getId(), "|", sum1.getSum(), "|");
                                        System.out.printf("%37s%n", "-------------------------------------");
                                    }
                                    System.out.println();
                                }
                                int sum_finally = 0;
                                sql = "SELECT sum(sum) from contract";
                                statement = connection.createStatement();
                                resultSet = statement.executeQuery(sql);
                                while (resultSet.next()){
                                    sum_finally = resultSet.getInt("sum(sum)");
                                }
                                System.out.printf("%10s%n", "---------------");
                                System.out.printf("%1s%10s%1s%n","|","Прибыль общая","|");
                                System.out.printf("%10s%n", "---------------");
                                System.out.printf("%1s%10s%3s%1s%n","|",sum_finally,"BYN","|");
                                System.out.printf("%10s%n", "---------------");
                                break;
                            case "8":
                                out.println(number2);
                                System.out.println("Exit");
                                return;
                        }
                    }
                }
            }
            connection.close();
        }
        catch (UnknownHostException e){
            System.err.println("Undefined hostname ");
            System.exit(1);
        }
        catch (Exception ex){
            System.out.println("Connection failed..." + ex);
        }
        finally {
            try{
                out.close();
                in.close();
                stdin.close();
                clientSocket.close();
            }
            catch (IOException e){
                System.err.println("Couldn't get I/O ");
            }
        }
    }
}
