package entities;

import queries.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLQueryParser {

    public Object parse(String query) throws Exception {
        query = query.trim();

        if (query.matches("(?i)CREATE DATABASE .*")) {
            return parseCreateDatabaseQuery(query);
        } else if (query.matches("(?i)USE .*")) {
            return parseUseDatabaseQuery(query);
        } else if (query.matches("(?i)CREATE TABLE [\\s\\S]*")) {
            // } else if (query.matches("(?i)CREATE TABLE (\\w+) \\(\\s*([\\w\\s,()]+)\\s*\\);")) {
            return parseCreateTableQuery(query);
        } else if (query.matches("(?i)INSERT INTO .*")) {
            return parseInsertQuery(query);
        } else if (query.matches("(?i)SELECT .* FROM .*")) {
            return parseSelectQuery(query);
        } else if (query.matches("(?i)UPDATE .* SET .* WHERE .*")) {
            return parseUpdateQuery(query);
        } else if (query.matches("(?i)DELETE FROM .* WHERE .*")) {
            return parseDeleteQuery(query);
        } else if (query.matches("(?i)DROP TABLE .*")) {
            return parseDropTableQuery(query);
        } else if (query.matches("(?i)START TRANSACTION;")) {
            return parseTransactionQuery(query);
        } else if (query.matches("(?i)COMMIT;")) {
            return parseTransactionQuery(query);
        } else if (query.matches("(?i)ROLLBACK;")) {
            return parseTransactionQuery(query);
        } else {
            //throw new Exception("Invalid query!");
            System.out.println("Invalid Query");
            return null;
        }
    }

    private CreateDatabaseQuery parseCreateDatabaseQuery(String query) {
        Pattern pattern = Pattern.compile("CREATE DATABASE (\\w+);?", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query);

        if (matcher.matches()) {
            String databaseName = matcher.group(1);
            return new CreateDatabaseQuery(databaseName);
        }

        return null;
    }

    private UseDatabaseQuery parseUseDatabaseQuery(String query) {
        Pattern pattern = Pattern.compile("USE (\\w+);?", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query);

        if (matcher.matches()) {
            String databaseName = matcher.group(1);
            return new UseDatabaseQuery(databaseName);
        }

        return null;
    }

    private static CreateTableQuery parseCreateTableQuery(String query) {
        Pattern pattern = Pattern.compile("CREATE TABLE (\\w+) \\((.+)\\);?", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(query);

        if (matcher.matches()) {
            String tableName = matcher.group(1);
            String columnsPart = matcher.group(2).trim();
            String[] columns = columnsPart.split(",");

            List<String> columnNames = new ArrayList<>();
            List<String> columnDataTypes = new ArrayList<>();
            List<Integer> columnSizes = new ArrayList<>();
            String primaryKeyColumn = null;
            List<String> foreignKeys = new ArrayList<>();
            List<String> referenceTables = new ArrayList<>();
            List<String> referenceTableColumns = new ArrayList<>();

            for (String column : columns) {
                column = column.trim();

                if (column.toUpperCase().contains("PRIMARY KEY")) {
                    String[] pkParts = column.split("\\s+");
                    if (pkParts.length > 2 && pkParts[2].equalsIgnoreCase("PRIMARY") && pkParts[3].equalsIgnoreCase("KEY")) {
                        primaryKeyColumn = pkParts[0].replaceAll("[()]", "");
                    }
                    String columnName = pkParts[0];
                    columnNames.add(columnName);
                    String dataType = pkParts[1];
                    if (dataType.contains("(") && dataType.contains(")")) {
                        int sizeStartIndex = dataType.indexOf("(") + 1;
                        int sizeEndIndex = dataType.indexOf(")");
                        String sizeString = dataType.substring(sizeStartIndex, sizeEndIndex);
                        int size = Integer.parseInt(sizeString);
                        columnSizes.add(size);
                        dataType = dataType.substring(0, sizeStartIndex - 1);
                    } else {
                        columnSizes.add(0);
                    }
                    columnDataTypes.add(dataType);
                    continue;
                }

                if (column.toUpperCase().startsWith("FOREIGN KEY")) {
                    Pattern fkPattern = Pattern.compile("FOREIGN KEY (\\w+) REFERENCES (\\w+)\\((\\w+)\\)", Pattern.CASE_INSENSITIVE);
                    Matcher fkMatcher = fkPattern.matcher(column);
                    if (fkMatcher.find()) {
                        String fkColumn = fkMatcher.group(1);
                        String referencedTable = fkMatcher.group(2);
                        String referencedColumn = fkMatcher.group(3);

                        foreignKeys.add(fkColumn);
                        referenceTables.add(referencedTable);
                        referenceTableColumns.add(referencedColumn);
                    }
                    continue;
                }

                String[] nameTypeSize = column.split("\\s+");
                String columnName = nameTypeSize[0];
                columnNames.add(columnName);
                String dataType = nameTypeSize[1];

                if (dataType.contains("(") && dataType.contains(")")) {
                    int sizeStartIndex = dataType.indexOf("(") + 1;
                    int sizeEndIndex = dataType.indexOf(")");
                    String sizeString = dataType.substring(sizeStartIndex, sizeEndIndex);
                    int size = Integer.parseInt(sizeString);
                    columnSizes.add(size);
                    dataType = dataType.substring(0, sizeStartIndex - 1);
                } else {
                    columnSizes.add(0);
                }
                columnDataTypes.add(dataType);
            }

            ForeignKey foreignKey = new ForeignKey(tableName, foreignKeys, referenceTables, referenceTableColumns);
            return new CreateTableQuery(tableName, columnNames, columnDataTypes, columnSizes, primaryKeyColumn, foreignKey);
        }

        return null;
    }


    private InsertQuery parseInsertQuery(String query) {
        Pattern pattern = Pattern.compile("INSERT INTO (\\w+) \\(([^)]+)\\) VALUES \\(([^)]+)\\);?", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query);

        if (matcher.matches()) {
            String tableName = matcher.group(1);
            String allColumns = matcher.group(2).trim();
            String allValues = matcher.group(3).trim();

            List<String> columnNames = Arrays.asList(allColumns.split("\\s*,\\s*"));
            List<String> values = Arrays.asList(allValues.split("\\s*,\\s*"));

            if (columnNames.size() != values.size()) {
                System.out.println("Column count does not match value count");
                return null;
            }

            return new InsertQuery(tableName, columnNames, values);
        }
        return null;
    }

    private SelectQuery parseSelectQuery(String query) {
        Pattern pattern = Pattern.compile("SELECT (.+) FROM (\\w+)(?: WHERE (.+))?;?", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query);

        if (matcher.matches()) {
            String columnsPart = matcher.group(1).trim();
            String tableName = matcher.group(2).trim();

            List<String> columns;
            if (columnsPart.equals("*")) {
                columns = null;
            } else {
                columns = Arrays.asList(columnsPart.split("\\s*,\\s*"));
            }

            String condition = matcher.group(3);
            if (condition != null) {
                Pattern conditionPattern = Pattern.compile("(.+)\\s*(=|>|<|>=|<=|!=|<>)\\s*(.+)");
                Matcher conditionMatcher = conditionPattern.matcher(condition.trim());

                if (conditionMatcher.matches()) {
                    String conditionColumn = conditionMatcher.group(1).trim();
                    String conditionOperator = conditionMatcher.group(2).trim();
                    String conditionValue = conditionMatcher.group(3).trim().replace(";", "");
                    return new SelectQuery(tableName, columns, conditionColumn, conditionOperator, conditionValue);
                }
            } else {
                return new SelectQuery(tableName, columns, null, null, null);
            }
        }

        return null;
    }


    private UpdateQuery parseUpdateQuery(String query) {
        Pattern pattern = Pattern.compile("UPDATE (\\w+) SET (.+) WHERE (.+);?", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query);

        if (matcher.matches()) {
            String tableName = matcher.group(1);
            String setClause = matcher.group(2).trim();

            String condition = matcher.group(3).trim();

            String[] setParts = setClause.split("\\s*=\\s*");
            List<String> setColumn = Arrays.asList(setParts[0].trim());
            List<String> setValue = Arrays.asList(setParts[1].trim());

            Pattern conditionPattern = Pattern.compile("(.+)\\s*(=|>|<|>=|<=|!=|<>)\\s*(.+)");
            Matcher conditionMatcher = conditionPattern.matcher(condition);

            if (conditionMatcher.matches()) {
                String conditionColumn = conditionMatcher.group(1).trim();
                String conditionOperator = conditionMatcher.group(2).trim();
                String conditionValue = conditionMatcher.group(3).trim().replace(";","");

                return new UpdateQuery(tableName, setColumn, setValue, conditionColumn, conditionOperator, conditionValue);
            }
        }

        return null;
    }

    private DeleteQuery parseDeleteQuery(String query) {
        Pattern pattern = Pattern.compile("DELETE FROM (\\w+) WHERE (.+);?", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query);

        if (matcher.matches()) {
            String tableName = matcher.group(1);
            String condition = matcher.group(2).trim();

            Pattern conditionPattern = Pattern.compile("(.+)\\s*(=|>|<|>=|<=|!=|<>)\\s*(.+)");
            Matcher conditionMatcher = conditionPattern.matcher(condition);

            if (conditionMatcher.matches()) {
                String conditionColumn = conditionMatcher.group(1).trim();
                String conditionOperator = conditionMatcher.group(2).trim();
                String conditionValue = conditionMatcher.group(3).trim().replace(";","");

                return new DeleteQuery(tableName, conditionColumn, conditionValue, conditionOperator);
            }
        }

        return null;
    }

    private DropTableQuery parseDropTableQuery(String query) {
        Pattern pattern = Pattern.compile("DROP TABLE (\\w+);?", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query);

        if (matcher.find()) {
            String tableName = matcher.group(1);
            return new DropTableQuery(tableName);
        }

        return null;
    }

    private TransactionQuery parseTransactionQuery(String query) {
        if (query.contains("transaction")) {
            return new TransactionQuery("transaction");
        } else if (query.contains("commit")) {
            return new TransactionQuery("commit");
        } else if (query.contains("rollback")) {
            return new TransactionQuery("rollback");
        }
        return null;
    }
}