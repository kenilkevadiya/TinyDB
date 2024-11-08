package Screen;

import Model.User;
import service.DatabaseService;

import java.util.Scanner;

public class HomeScreen {
    Scanner sc;

    DatabaseService databaseService;

    public HomeScreen(Scanner sc) {
        this.sc = sc;
        databaseService = new DatabaseService();
    }

    public void loadHomeScreen(String userId) throws Exception {
        boolean running = true;
        while (running) {
            System.out.println("Welcome to the Database System");
            System.out.println("1. Write Queries");
            System.out.println("2. Export Data and Structure");
            System.out.println("3. ERD");
            System.out.println("4. Exit");
            int input = 10;
            try {
                input = sc.nextInt();
            } catch (Exception e) {
                System.out.println("Please enter valid integer");
                sc.nextLine();
                continue;
            }
            switch (input) {
                case 1:
                    sc.nextLine();
                    System.out.println("Please enter the query");
                    StringBuilder query = new StringBuilder();
                    while (sc.hasNextLine()) {
                        String line = sc.nextLine();
                        query.append(line).append("\n");
                        if (line.trim().endsWith(";")) {
                            break;
                        }
                    }
                    databaseService.processQueries(query.toString(), userId);
                    System.out.println("The query entered is:" + query);
                    continue;
                case 2:
                    System.out.println("Export the Data and Structure");
                    continue;
                case 3:
                    System.out.println("Generating ERD...");
                    databaseService.generateERD("foreignkey.txt", "database.txt");
                    continue;

                case 4:
                    System.out.println("Successfully Exit");
                    running = false;
                    break;
                default:
                    System.out.println("Retry");
            }
        }
    }
}
