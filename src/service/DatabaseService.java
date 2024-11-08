package service;

import Utils.DatabaseInfo;
import Utils.QueryLogger;
import entities.*;
import queries.CreateDatabaseQuery;
import queries.CreateTableQuery;
import queries.DeleteQuery;
import queries.DropTableQuery;
import queries.InsertQuery;
import queries.SelectQuery;
import queries.TransactionQuery;
import queries.UpdateQuery;
import queries.UseDatabaseQuery;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;

public class DatabaseService {
    TinyDb tinyDb;

    SQLQueryParser sqlQueryParser;

    static Database currentDatabase;

    Database databaseCopy;

    boolean isTransactionMode = false;

    public String databaseName="";

    ForeignKey foreignKey;

    public DatabaseService() {
        tinyDb = new TinyDb();
        loadDatabaseFromFile();
        sqlQueryParser = new SQLQueryParser();
        currentDatabase = null;
        databaseCopy = null;
    }

    private void loadDatabaseFromFile() {
        try {
            tinyDb.loadFromFile("database.txt");
        } catch (IOException e) {
            System.out.println("The database configurations doesn't exist.");
        }
    }

    public void saveToFile() {
        try {
            tinyDb.updateDatabase(currentDatabase);
            tinyDb.saveToFile("database.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public void processQueries(String query, String userId) throws Exception {
        long startTime = 0;
        long endTime = 0;
        long duration = 0;
        long durationInMillis = 0;

        startTime = System.nanoTime();
        Object object = sqlQueryParser.parse(query);

        if (object instanceof UseDatabaseQuery) {

            databaseName = ((UseDatabaseQuery) object).getDatabaseName();
            currentDatabase = tinyDb.getDatabaseByName(databaseName);
            currentDatabase.logDatabaseState(databaseName);
        } else if (object instanceof CreateDatabaseQuery) {

            String databaseToCreate = ((CreateDatabaseQuery) object).getDatabaseName();
            tinyDb.addNewDatabase(databaseToCreate);
            tinyDb.saveToFile("database.txt");

            // Log the event
            QueryLogger.logQuery(userId, "Create Database","Created database: " + databaseToCreate);
            currentDatabase.logDatabaseState(databaseName);
        } else if (object instanceof CreateTableQuery) {
            if (Objects.isNull(currentDatabase)) {
                System.out.println("No Database is Selected");
                QueryLogger.logQuery(userId, "NO DATABASE SELECTED",query);
            } else {
                currentDatabase.addTable(((CreateTableQuery) object).getTableName(),
                        ((CreateTableQuery) object).getColumnNames(),
                        ((CreateTableQuery) object).getColumnDataTypes(),
                        ((CreateTableQuery) object).getColumnSizes(),
                        ((CreateTableQuery) object).getPrimaryKeyColumn());
                tinyDb.saveToFile("database.txt");
                if (((CreateTableQuery) object).getForeignKey() != null) {
                    ((CreateTableQuery) object).getForeignKey().generateForeignKeyMetadata("foreignkey.txt", currentDatabase.getDatabaseName(), ((CreateTableQuery) object).getTableName());
                } else {
                    System.out.println("getForeignKey() != null");
                }
                // Log the event
                QueryLogger.logQuery(userId, "CREATE TABLE",query);
                currentDatabase.logDatabaseState(databaseName);
            }
        } else if (object instanceof InsertQuery) {
            if (Objects.isNull(currentDatabase)) {
                System.out.println("No Database is Selected");
                QueryLogger.logQuery(userId, "NO DATABASE SELECTED",query);
            } else {
                tinyDb.loadFromFile("database.txt");
                currentDatabase = tinyDb.getDatabaseByName(databaseName);
                if (!isTransactionMode) {
                    boolean isTableLock = checkTableTransactionLockInFile(((InsertQuery) object).getTableName(), userId);
                    if (isTableLock) {
                        System.out.println("Cannot access the Table");
                        QueryLogger.logEvent(userId, "tried accessing " + ((DeleteQuery) object).getTableName() + " but its already locked.");
                        return;
                    }
                    String result = currentDatabase.insertData(((InsertQuery) object).getTableName(),
                            ((InsertQuery) object).getColumnNames(),
                            ((InsertQuery) object).getValues());
                    if (result.equalsIgnoreCase("-1")) {
                        System.out.println("Invalid entry in the insert query.");
                    } else {
                        System.out.println("Successfully added entry with Id:" + result);
                    }
                    tinyDb.saveToFile("database.txt");

                    // Log the event
                    QueryLogger.logQuery(userId,"INSERT ROWS", query);
                    currentDatabase.logDatabaseState(databaseName);
                } else {
                    boolean isTableLock = checkTableTransactionLockInFile(((InsertQuery) object).getTableName(), userId);
                    if (isTableLock) {
                        System.out.println("Cannot access the Table");
                        QueryLogger.logEvent(userId, "tried accessing " + ((DeleteQuery) object).getTableName() + " but its already locked.");
                        return;
                    }
                    boolean successWrite = saveTransactionLockInFile(((InsertQuery) object).getTableName(), userId);
                    String result = databaseCopy.insertData(((InsertQuery) object).getTableName(),
                            ((InsertQuery) object).getColumnNames(),
                            ((InsertQuery) object).getValues());
                    if (result.equalsIgnoreCase("-1")) {
                        System.out.println("Invalid entry in the insert query.");
                    } else {
                        System.out.println("Successfully added entry with Id:" + result);
                    }
                }
            }
        } else if (object instanceof SelectQuery) {
            if (Objects.isNull(currentDatabase)) {
                System.out.println("No Database is Selected");
                QueryLogger.logQuery(userId, "NO DATABASE SELECTED",query);
            } else {
                tinyDb.loadFromFile("database.txt");
                currentDatabase = tinyDb.getDatabaseByName(databaseName);
                List<Row> result = currentDatabase.selectData(((SelectQuery) object).getTableName(),
                        ((SelectQuery) object).getColumns(),
                        ((SelectQuery) object).getConditionColumn(),
                        ((SelectQuery) object).getConditionValue());
                for (Row row : result) {
                    System.out.println(row.getDataValue());
                }
                // Log the event
                QueryLogger.logQuery(userId,"SELECT ROWS", query);
                currentDatabase.logDatabaseState(databaseName);
            }
        } else if (object instanceof UpdateQuery) {
            if (Objects.isNull(currentDatabase)) {
                System.out.println("No Database is Selected");
                QueryLogger.logQuery(userId, "NO DATABASE SELECTED",query);
            } else {
                tinyDb.loadFromFile("database.txt");
                currentDatabase = tinyDb.getDatabaseByName(databaseName);
                if (!isTransactionMode) {
                    boolean isTableLock = checkTableTransactionLockInFile(((UpdateQuery) object).getTableName(), userId);
                    if (isTableLock) {
                        System.out.println("Cannot access the Table");
                        QueryLogger.logEvent(userId, "tried accessing " + ((DeleteQuery) object).getTableName() + " but its already locked.");
                        return;
                    }
                    String result = currentDatabase.updateData(((UpdateQuery) object).getTableName(),
                            ((UpdateQuery) object).getSetColumn(),
                            ((UpdateQuery) object).getSetValue(),
                            ((UpdateQuery) object).getConditionColumn(),
                            ((UpdateQuery) object).getConditionValue());
                    System.out.println("Successfully updated the entry:" + result);
                    tinyDb.saveToFile("database.txt");

                    // Log the event
                    QueryLogger.logQuery(userId,"UPDATE ROWS", query);
                    currentDatabase.logDatabaseState(databaseName);
                } else {
                    boolean isTableLock = checkTableTransactionLockInFile(((UpdateQuery) object).getTableName(), userId);
                    if (isTableLock) {
                        System.out.println("Cannot access the Table");
                        QueryLogger.logEvent(userId, "tried accessing " + ((DeleteQuery) object).getTableName() + " but its already locked.");
                        return;
                    }
                    boolean successWrite = saveTransactionLockInFile(((UpdateQuery) object).getTableName(), userId);
                    String result = databaseCopy.updateData(((UpdateQuery) object).getTableName(),
                            ((UpdateQuery) object).getSetColumn(),
                            ((UpdateQuery) object).getSetValue(),
                            ((UpdateQuery) object).getConditionColumn(),
                            ((UpdateQuery) object).getConditionValue());
                    System.out.println("Successfully updated the entry:" + result);
                    currentDatabase.logDatabaseState(databaseName);
                }

            }
        } else if (object instanceof DeleteQuery) {
            if (Objects.isNull(currentDatabase)) {
                System.out.println("No Database is Selected");
                QueryLogger.logQuery(userId, "NO DATABASE SELECTED",query);
            } else {
                tinyDb.loadFromFile("database.txt");
                currentDatabase = tinyDb.getDatabaseByName(databaseName);
                if (!isTransactionMode) {
                    boolean isTableLock = checkTableTransactionLockInFile(((DeleteQuery) object).getTableName(), userId);
                    if (isTableLock) {
                        System.out.println("Cannot access the Table");
                        QueryLogger.logEvent(userId, "tried accessing " + ((DeleteQuery) object).getTableName() + " but its already locked.");
                        currentDatabase.logDatabaseState(databaseName);
                        return;
                    }
                    String result = currentDatabase.deleteData(((DeleteQuery) object).getTableName(),
                            ((DeleteQuery) object).getConditionColumn(),
                            ((DeleteQuery) object).getConditionValue());
                    tinyDb.saveToFile("database.txt");

                    // Log the event
                    QueryLogger.logQuery(userId, "DELETE ROWS",query);
                    currentDatabase.logDatabaseState(databaseName);
                } else {
                    boolean isTableLock = checkTableTransactionLockInFile(((DeleteQuery) object).getTableName(), userId);
                    if (isTableLock) {
                        System.out.println("Cannot access the Table");
                        QueryLogger.logEvent(userId, "tried accessing " + ((DeleteQuery) object).getTableName() + " but its already locked.");
                        return;
                    }
                    boolean successWrite = saveTransactionLockInFile(((DeleteQuery) object).getTableName(), userId);
                    String result = databaseCopy.deleteData(((DeleteQuery) object).getTableName(),
                            ((DeleteQuery) object).getConditionColumn(),
                            ((DeleteQuery) object).getConditionValue());
                    currentDatabase.logDatabaseState(databaseName);
                }
            }
        } else if (object instanceof DropTableQuery) {
            if (Objects.isNull(currentDatabase)) {
                System.out.println("No Database is Selected");
                QueryLogger.logQuery(userId, "NO DATABASE SELECTED",query);
            } else {
                String result = currentDatabase.dropTable(((DropTableQuery) object).getTableName());
                if (result.equalsIgnoreCase("1")) {
                    tinyDb.saveToFile("database.txt");
                    System.out.println("Successfully dropped the table with name:" + ((DropTableQuery) object).getTableName());
                } else if (result.equalsIgnoreCase("-1")) {
                    System.out.println("Table does not exists");
                } else {
                    System.out.println("Something went Wrong!");
                }
                QueryLogger.logQuery(userId, "DROP TABLE",query);
                currentDatabase.logDatabaseState(databaseName);
            }
        } else if (object instanceof TransactionQuery) {
            if (Objects.isNull(currentDatabase)) {
                System.out.println("No Database is Selected");
                QueryLogger.logQuery(userId, "NO DATABASE SELECTED",query);
            } else {
                String transactionStatement = ((TransactionQuery) object).getStatement();
                if (transactionStatement.equalsIgnoreCase("transaction")) {
                    //Create copy
                    isTransactionMode = true;
                    databaseCopy = (Database) currentDatabase.clone();
                    QueryLogger.logQuery(userId, "TRANSACTION DETECTED",query);
                    currentDatabase.logDatabaseState(databaseName);
                } else if (transactionStatement.equalsIgnoreCase("commit")) {
                    //Save the Copy
                    currentDatabase = null;
                    currentDatabase = (Database) databaseCopy.clone();
                    databaseCopy = null;
                    isTransactionMode = false;
                    tinyDb.updateDatabase(currentDatabase);
                    tinyDb.saveToFile("database.txt");
                    clearFileContent("transactionTable.txt", userId);
                    QueryLogger.logQuery(userId, "TRANSACTION COMMIT",query);
                    currentDatabase.logDatabaseState(databaseName);
                } else if (transactionStatement.equalsIgnoreCase("rollback")) {
                    //Discard the copy
                    databaseCopy = null;
                    isTransactionMode = false;
                    clearFileContent("transactionTable.txt", userId);
                    QueryLogger.logQuery(userId, "TRANSACTION ROLLBACK",query);
                    currentDatabase.logDatabaseState(databaseName);
                }
            }
        } else {
            System.out.println("Invalid Query");
//            tinyDb.loadFromFile("database.txt");
//            currentDatabase = tinyDb.getDatabaseByName(databaseName);
//            tinyDb.saveToFile("database.txt");
        }
        endTime = System.nanoTime(); // Capture end time
        duration = endTime - startTime; // Calculate duration
        double durationInMs = duration / 1000000.0;
        QueryLogger.logGeneral("Execution Time", "Query: "+query +"\n Execution Time: " + durationInMs + " ms");
    }

    private boolean saveTransactionLockInFile(String tableName, String userId) throws IOException {
        //Saving to the file in the formal as below
        // TableName:userId
        QueryLogger.logEvent(userId, tableName +" is locked to " +userId + " because of transaction.");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(tableName).append(":").append(userId).append("\n");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("transactionTable.txt"))) {
            writer.write(stringBuilder.toString());
            return true;
        }
    }

    private boolean checkTableTransactionLockInFile(String tableName, String userId) throws IOException {
        //Reading the transaction lock table entries
        try (BufferedReader reader = new BufferedReader(new FileReader("transactionTable.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(":");
                if (data[0].equalsIgnoreCase(tableName)) {
                    if (!data[1].equalsIgnoreCase(userId)) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    private boolean clearFileContent(String fileName, String userId) throws IOException {
        try {
            FileWriter fw = new FileWriter(fileName, false);
            PrintWriter pw = new PrintWriter(fw, false);
            pw.flush();
            pw.close();
            fw.close();
            QueryLogger.logEvent(userId, "Releasing all locks from the tables.");
        } catch (Exception exception) {
            System.out.println("Exception occurred: " + exception.getMessage());
            return false;
        }
        return true;
    }

    public void generateERD(String foreignKeyFile, String databaseFile) {
        try {
            DatabaseInfo databaseInfo = parseDatabaseFile(databaseFile);
            //databaseInfo.printPrimaryKeys();

            try (BufferedReader reader = new BufferedReader(new FileReader(foreignKeyFile))) {
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                String[] entries = sb.toString().split("\n\n");

                for (String entry : entries) {
                    String[] lines = entry.split("\n");

                    String databaseName = null;
                    String tableName = null;
                    String foreignKey = null;
                    String referenceTable = null;
                    String referenceTableColumn = null;

                    for (String currentLine : lines) {
                        if (currentLine.trim().startsWith("Database: ")) {
                            databaseName = currentLine.trim().substring(10);
                        } else if (currentLine.trim().startsWith("Table: ")) {
                            tableName = currentLine.trim().substring(7);
                        } else if (currentLine.trim().startsWith("ForeignKeys: ")) {
                            foreignKey = currentLine.trim().substring(13);
                        } else if (currentLine.trim().startsWith("ReferenceTables: ")) {
                            referenceTable = currentLine.trim().substring(17);
                        } else if (currentLine.trim().startsWith("ReferenceTableColumns: ")) {
                            referenceTableColumn = currentLine.trim().substring(22);
                        }
                    }

                    //System.out.println("Check " + referenceTable + " (" + referenceTableColumn + ") is primary key: " + databaseInfo.isPrimaryKey(referenceTable, referenceTableColumn));
                    //System.out.println("Check " + tableName + " (" + foreignKey + ") is primary key: " + databaseInfo.isPrimaryKey(tableName, foreignKey));

                    String referenceTableCardinality = databaseInfo.isPrimaryKey(referenceTable, referenceTableColumn) ? "1" : "N";
                    String foreignKeyCardinality = databaseInfo.isPrimaryKey(tableName, foreignKey) ? "1" : "N";
                    String cardinality = referenceTableCardinality + " to " + foreignKeyCardinality;

                    String erdFileName = databaseName + "_erd.txt";
                    try (PrintWriter writer = new PrintWriter(new FileWriter(erdFileName, true))) {
                        writer.println(referenceTable + " (" + referenceTableColumn + ") is related to " + tableName + " (" + foreignKey + ") [" + cardinality + "]");
                        System.out.println("Successfully generated ERD in file: " + erdFileName);
                    } catch (IOException e) {
                        System.out.println("Failed to write ERD file: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
        }
    }

    private DatabaseInfo parseDatabaseFile(String databaseFile) {
        DatabaseInfo databaseInfo = new DatabaseInfo();
        try (BufferedReader reader = new BufferedReader(new FileReader(databaseFile))) {
            String line;
            String currentTable = null;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("- Table: ")) {
                    currentTable = line.substring(9);
                } else if (line.startsWith("PrimaryKey: ")) {
                    String primaryKey = line.substring(12).trim();
                    if (!primaryKey.equals("null") && currentTable != null) {
                        databaseInfo.addPrimaryKey(currentTable, primaryKey);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading database file: " + e.getMessage());
        }
        return databaseInfo;
    }
}
