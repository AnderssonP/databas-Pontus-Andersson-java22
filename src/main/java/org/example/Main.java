package org.example;
import java.sql.*;
import java.util.Random;
import java.util.Scanner;

import static org.example.account.*;
import static org.example.friends.makeFriends;
import static org.example.friends.showFriends;
import static org.example.transactions.*;
import static org.example.users.*;


public class Main {
    public static void main(String[] args) throws SQLException {

        Model.initializeDataSource();
        Model.getConnection();
        tables.makeTable();
        Scanner scanner = new Scanner(System.in);
       while (true){
        System.out.println("1: Skapa Konto \n2: Logga in");
        String choose = scanner.nextLine();
        if (choose.equals("1")) {
            users user = new users();
            System.out.println("Vad är ditt förnamn?");
            String fname = scanner.nextLine();
            System.out.println("Vad är ditt efternamn?");
            String lname = scanner.nextLine();
            System.out.println("Ange din e-mail");
            String email = scanner.nextLine();
            System.out.println("Skriv in ett lösenord!");
            String password = scanner.nextLine();
            System.out.println("Vart bort du?");
            String address = scanner.nextLine();
            System.out.println("Hur gammal är du?");
            int age = Integer.parseInt(scanner.nextLine());
            System.out.println("Ange ditt personnummer, första 6");
            String cNumber = scanner.nextLine();
            user.createUser(fname, lname, email, password, address, age, cNumber,false);
        } else if (choose.equals("2")) {
            System.out.println("ange personnummer");
            String cNumber = scanner.nextLine();
            System.out.println("Skriv ditt lösenord");
            String password = scanner.nextLine();
            Boolean loggedIn = users.login(cNumber, password);
            if (loggedIn){
                displayOnlineUsers();
                boolean in = true;
                while (in){
                    System.out.println("Vad önskar du att göra? \n" +
                            "1: Kolla skicka eller mottagna pengar\n" +
                            "2: Skicka över pengar\n" +
                            "3: Mitt konto\n" +
                            "4: Skapa ett konto\n" +
                            "5: Ta bort konto\n" +
                            "6: Uppdatera mina personuppgifter\n" +
                            "7: Lägga till vän\n" +
                            "8: Logga ut\n" +
                            "9: Ta bort bankkontot");
                    String handleChoose = scanner.nextLine();
                    switch (handleChoose){
                        case "1":
                            System.out.println("Vill du se skickade eller mottagna transaktioner?");
                            String check = scanner.nextLine();
                            if (check.equals("skickade")) {
                                System.out.println("Skriv in första datumet (yyyy-MM-dd)");
                                Date firstDate = Date.valueOf(scanner.nextLine());
                                System.out.println("Skriv in andra datumet (yyyy-MM-dd)");
                                Date secondDate = Date.valueOf(scanner.nextLine());
                                showTransactionsSent(cNumber,firstDate,secondDate);
                            } else if (check.equals("mottagna")) {
                                System.out.println("Skriv in första datumet (yyyy-MM-dd)");
                                Date firstDate = Date.valueOf(scanner.nextLine());
                                System.out.println("Skriv in andra datumet (yyyy-MM-dd)");
                                Date secondDate = Date.valueOf(scanner.nextLine());
                                showTransactionsReceived(cNumber,firstDate,secondDate);
                            }break;
                        case "2":
                            account checkOne = new account();
                            checkOne.getAccounts();
                            System.out.println("Från vilket konto vill du skicka ifrån?");
                            int send = Integer.parseInt(scanner.nextLine());
                            System.out.println("Hur mycket?");
                            int sendAmount = Integer.parseInt(scanner.nextLine());
                            System.out.println("Vem vill du skicka till? Välj vän nedan.");
                            showFriends(cNumber);
                            int to = Integer.parseInt(scanner.nextLine());
                            System.out.println("Till vilket konto?");
                            getFriendsAccount(to);
                            String toAccount = scanner.nextLine();
                            sendMoney(send,sendAmount,toAccount);
                            break;
                        case "3":
                            displayAccount(cNumber);
                            break;
                        case "4":
                            System.out.println("Hur mycket vill du lägga in?");
                            int amount = Integer.parseInt(scanner.nextLine());
                            Random random = new Random();
                            String randomNUmber = "";
                            for (int i = 0;i<9;i++){
                                int accNum = random.nextInt(10);
                                randomNUmber += accNum;
                            }
                            System.out.println("Vad vill du kontot ska heta?");
                            String nameOfAccount = scanner.nextLine();
                            createAccount(amount,randomNUmber,nameOfAccount);
                            break;
                        case "5":
                            account get = new account();
                            get.getAccounts();
                            System.out.println("Vilket konto vill du ta bort?");
                            int account = Integer.parseInt(scanner.nextLine());
                            deleteAccount(account);
                            break;
                        case "6":
                            System.out.println("Vad vill du uppdatera?\n" +
                                    "1: Förnamn\n" +
                                    "2: Efternamn\n" +
                                    "3: E-mail\n" +
                                    "4: Lösenord\n" +
                                    "5: Adress");
                            String changeChoose = scanner.nextLine();
                            if (changeChoose.equals("1")){
                                System.out.println("Vad vill du ändra det till?");
                                String one = scanner.nextLine();
                                updateUser("firstname",one);
                            } else if (changeChoose.equals("2")) {
                                System.out.println("Vad vill du ändra det till?");
                                String one = scanner.nextLine();
                                updateUser("lastname",one);
                            }else if (changeChoose.equals("3")) {
                                System.out.println("Vad vill du ändra det till?");
                                String one = scanner.nextLine();
                                updateUser("email",one);
                            }else if (changeChoose.equals("4")) {
                                System.out.println("Vad vill du ändra det till?");
                                String one = scanner.nextLine();
                                updateUser("password",one);
                            }else if (changeChoose.equals("5")) {
                                System.out.println("Vad vill du ändra det till?");
                                String one = scanner.nextLine();
                                updateUser("address",one);
                            }
                            break;
                        case "7":
                            System.out.println("Vem vill du lägga till? Ange personnummer");
                            String friend = scanner.nextLine();
                            makeFriends(friend);
                            break;
                        case "8":
                            logout(cNumber);
                            in = false;
                            break;
                        case "9":
                            System.out.println("Bekräfta personnummer");
                            String pNumber = scanner.nextLine();
                            deleteUser(pNumber);
                            in = false;
                            break;
                    }
                }
            }
        }
           }
       }
    }
