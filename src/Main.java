import Screen.PrintScreen;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
//        System.out.printf("Hello and welcome!");
        PrintScreen screen = new PrintScreen();
        screen.MainScreen(new Scanner(System.in));
            //TODO: Below is just a sample driver program to understand how the functions works
//        //TODO: Currently toString method is not implemented, after it's implementation you can print exact values of object
//        //TODO: FOr now please use debugger to verify the values in below objects
//        TinyDb tinyDb = new TinyDb();
//        //Database database = new Database("tinyDb");
//        tinyDb.addNewDatabase("myDb");
//        Database database = tinyDb.getDatabaseByName("myDb");
//        List<String> columnNames = List.of("id", "name", "email", "phone", "address");
//        List<String> columnDataTypes = List.of("int", "varchar", "varchar", "varchar", "varchar");
//        List<Integer> columnSizes = List.of(99, 255, 128, 20, 255);
//
//        database.addTable("user", columnNames, columnDataTypes, columnSizes, "id");
//        String insertRes1 = database.insertData("user", List.of("id", "name"), List.of("1", "John"));
//        String insertRes2 = database.insertData("user", List.of("id", "name", "email"), List.of("2", "Mary", "mary@hfx.com"));
//        String insertRes3 = database.insertData("user", List.of("id", "name", "email", "address"), List.of("", "Quill", "quill@hfx.com", "Barrington Street"));
//
//        System.out.println(database);
//        List<Row> result = database.selectData("user", "email", "mary@hfx.com");
//        System.out.println(result);
//        List<Row> result2 = database.selectData("user", "email", "mary@hfx.com");
//
//
//        List<String> columnNamesForInventory = List.of("item_id", "item_name", "available_quantity");
//        List<String> columnDataTypesForInventory = List.of("int", "varchar", "int");
//        List<Integer> columnSizesForInventory = List.of(99, 255, 128, 20, 255);
//        database.addTable("inventory", columnNamesForInventory, columnDataTypesForInventory, columnSizesForInventory, "item_id");
//        String resultItem1 = database.insertData("inventory", List.of("item_id", "item_name", "available_quantity"), List.of("", "Shoe", "10"));
//        String resultItem2 = database.insertData("inventory", List.of("item_id", "item_name", "available_quantity"), List.of("", "Pen", "20"));
//        String resultItem3 = database.insertData("inventory", List.of("item_id", "item_name", "available_quantity"), List.of("", "Pencil", "20"));
//
//        List<Row> resultOfItemWith20Items = database.selectData("inventory", "available_quantity", "20");
//        System.out.println(resultOfItemWith20Items);
//
//        List<Row> resultOfItemWithNameShoe = database.selectData("inventory", "item_name", "Shoe");
//        System.out.println(resultOfItemWithNameShoe);
//
//        String outputOfUpdate = database.updateData2("inventory", List.of("item_name"), List.of("Sports Shoes"), "item_name", "Shoe");
//
//        String outputOfUpdate2 = database.updateData2("inventory", List.of("item_name", "available_quantity"), List.of("Normal Shoes", "25"), "item_name", "Sports Shoes");
//        System.out.println(outputOfUpdate);
//
//        String outputOfUpdate3 = database.updateData2("inventory", List.of("available_quantity"), List.of("30"), "available_quantity", "20");
//        System.out.println(outputOfUpdate3);
//
//        String deleteOutput = database.deleteData("inventory", "item_name", "Pencil");
//        System.out.println(deleteOutput);
//
//
//        tinyDb.addNewDatabase("CustomDb2");
//        Database database1 = tinyDb.getDatabaseByName("CustomDb2");
//        database1.addTable("city", List.of("city_id", "city_name"), List.of("int", "varchar"), List.of(255, 128), "city_id");
//        database1.insertData("city", List.of("city_id", "city_name"), List.of("", "Torronto"));
//
//
//        database.addTable("employee", List.of("employee_id", "employee_name", "salary"), List.of("int", "vachar", "int"), List.of(255, 256, 256), "employee_id");
////        File file = new File("database.txt");
////        try {
////            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
////            objectOutputStream.writeObject(tinyDb);
////            objectOutputStream.close();
////        } catch (IOException e) {
////            throw new RuntimeException(e);
////        }
//
////        File file2 = new File("database.txt");
////
////        try {
////            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file2));
////            TinyDb tinyDb2 = (TinyDb) objectInputStream.readObject();
////            System.out.println(tinyDb2);
////        } catch (IOException e) {
////            throw new RuntimeException(e);
////        } catch (ClassNotFoundException e) {
////            throw new RuntimeException(e);
////        }
//        try {
//            tinyDb.saveToFile("database2.txt");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        TinyDb newTinyDb = new TinyDb();
//        try {
//            newTinyDb.loadFromFile("database2.txt");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        Database newTinyCurrentDb = newTinyDb.getDatabaseByName("myDb");
//        newTinyCurrentDb.insertData("employee", List.of("employee_id", "employee_name", "salary"), List.of("", "Wick", "100000"));
//
//
//        try {
//            newTinyDb.saveToFile("database2.txt");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        try {
//            newTinyDb.loadFromFile("database2.txt");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
////        }
//        Scanner sc = new Scanner(System.in);
//        HomeScreen homeScreen = new HomeScreen(sc);
//        try {
//            homeScreen.loadHomeScreen();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }
}