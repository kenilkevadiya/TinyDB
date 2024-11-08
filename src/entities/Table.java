package entities;

import java.util.*;

public class Table implements Cloneable {

    String tableName;

    List<Column> columns;

    List<Row> data;

    String primaryKeyColumnName;

    static int id = 1;

    ForeignKey foreignKey;

    public Table(String tableName) {
        this.tableName = tableName;
        this.columns = new ArrayList<>();
        this.data = new ArrayList<>();
        primaryKeyColumnName = "";
        Table.id = 1;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public List<Row> getData() {
        return data;
    }

    public void setData(List<Row> data) {
        this.data = data;
    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        Table.id = id;
    }

//    public void addColumns(String columnName){
//        Column column = new Column(columnName);
//        columns.add(column);
//    }

    public void setPrimaryKeyColumnName(String primaryKeyColumnName) {
        this.primaryKeyColumnName = primaryKeyColumnName;
    }

    public ForeignKey getForeignKey() {
        return foreignKey;
    }

    public void setForeignKey(ForeignKey foreignKey) {
        this.foreignKey = foreignKey;
    }

    public ForeignKey getForeignKeyDetails() {
        if (foreignKey != null) {
            return foreignKey;
        }
        return null;
    }

    public List<String> getColumnNames() {
        List<String> columnNames = new ArrayList<>();
        for (Column column : columns) {
            columnNames.add(column.getColumnName());
        }
        return columnNames;
    }

    public List<String> getColumnDataTypes() {
        List<String> columnDataTypes = new ArrayList<>();
        for (Column column : columns) {
            columnDataTypes.add(column.getColumnDataType());
        }
        return columnDataTypes;
    }

    public List<Integer> getColumnSizes() {
        List<Integer> columnSizes = new ArrayList<>();
        for (Column column : columns) {
            columnSizes.add(column.getDataTypeSize());
        }
        return columnSizes;
    }

    public String getPrimaryKeyColumnName() {
        return primaryKeyColumnName;
    }

    public void addColumn(String columnName, String columnDataType, int dataTypeSize, String primaryKeyColumnName) {
        //TODO: Whether to keep this here or one hierarchy above
        this.primaryKeyColumnName = primaryKeyColumnName;
        if (columnDataType.equalsIgnoreCase("int")) {
            dataTypeSize = Integer.MAX_VALUE;
        }
        if (columnDataType.equalsIgnoreCase("float")) {
            dataTypeSize = (int) Float.MAX_VALUE;
        }
        if (columnDataType.equalsIgnoreCase("double")) {
            dataTypeSize = (int) Double.MAX_VALUE;
        }
        Column column = new Column(columnName, columnDataType, dataTypeSize);
        columns.add(column);
    }

    public String insertData(List<String> columnNames, List<String> values) {
        //TODO: Validation needs to be done
        boolean areAllEntriesValid = validateDataTypes(columns, columnNames, values);
        boolean isRecordPresent = isRecordPresent(columnNames,values);
        if (!areAllEntriesValid) {
            return "-1";
        }
        if(isRecordPresent){
            System.out.println("The record with the primary key already exists in the table");
            return "-1";
        }
        Row row = new Row();
        String result = row.addDataRow(columnNames, values, primaryKeyColumnName, id);
        data.add(row);
        id++;
        return result;
    }

    private boolean validateDataTypes(List<Column> columnsInDatabase, List<String> columnNames, List<String> values) {
        boolean allValid = true;
        for (int i = 0; i < columnNames.size(); i++) {
            String columnNameToBeInsertedWithData = columnNames.get(i);
            String valueToBeInserted = values.get(i);
            for (Column column : columnsInDatabase) {
                if (column.getColumnName().equalsIgnoreCase(columnNameToBeInsertedWithData)) {
                    if (column.getColumnDataType().equalsIgnoreCase("int")) {
                        if (!valueToBeInserted.matches("\\d+")) {
                            allValid = false;
                        }
                        break;
                    } else if (column.getColumnDataType().equalsIgnoreCase("float")) {
                        if (!valueToBeInserted.matches("\\d+")) {
                            allValid = false;
                        }
                        break;
                    } else if (column.getColumnDataType().equalsIgnoreCase("double")) {
                        if (!valueToBeInserted.matches("\\d+")) {
                            allValid = false;
                        }
                        break;
                    } else {
                        //For Others
                    }
                }
            }

        }
        return allValid;
    }

    public List<Row> getDataFromTable(List<String> columnNames, String columnName, String value) {
        //TODO: Validation Starts
//        for(Column column:columns){
//            if(!column.getColumnName().equalsIgnoreCase(columnName)){
//                return null;
//            }
//        }
        //TODO: Validation Ends
        List<Row> result = new ArrayList<>();
        Map<String, String> rowData = new LinkedHashMap<>();
        if (Objects.isNull(columnName) && Objects.isNull(value)) {
            return filterData(columnNames, data);
        }
        for (Row row : data) {
            rowData = row.getDataValue();
            for (Map.Entry<String, String> entry : rowData.entrySet()) {
                if (entry.getKey().equalsIgnoreCase(columnName) && entry.getValue().equalsIgnoreCase(value)) {
                    result.add(row);
                }
            }
        }
        if (Objects.isNull(columnNames) || columnNames.isEmpty()) {
            return result;
        }
        return filterData(columnNames, result);
    }

    public String updateData(List<String> columnNames, List<String> values, String whereColumn, String whereValue) {
        List<Row> rowsToUpdate = getDataFromTable(null, whereColumn, whereValue);
        if (rowsToUpdate.isEmpty()) {
            return "The given criteria Data is not present in the database";
        }
        for (Row row : rowsToUpdate) {
            String updateResult = row.updateDataRow(columnNames, values);
        }
        return "Updated the data successfully";
    }

    public String deleteData(String whereColumn, String whereValue) {
        List<Row> rowsToDelete = getDataFromTable(null, whereColumn, whereValue);
        if (rowsToDelete.isEmpty()) {
            return "The given criteria Data is not present in the database";
        }
        List<Row> updatedData = new ArrayList<>();
        for (Row row : data) {
            for (Row rowToDelete : rowsToDelete) {
                if (!row.equals(rowToDelete)) {
                    updatedData.add(row);
                }
            }
        }
        data.clear();
        data.addAll(updatedData);
        return "Successfully delete the required entries";
    }


    // Serialize the Table
    public String serialize(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("- Table: ").append(tableName).append("\n");
        sb.append(indent).append("  PrimaryKey: ").append(primaryKeyColumnName).append("\n");
        sb.append(indent).append("  Columns:\n");
        for (Column column : columns) {
            sb.append(column.serialize(indent + "    "));
        }
        sb.append(indent).append("  Data:\n");
        for (Row row : data) {
            sb.append(row.serialize(indent + "    "));
        }
        return sb.toString();
    }

    // Deserialize the Table
    public void deserialize(String str) {
        String[] parts = str.split(": ");
        if (parts.length > 1) {
            if (parts[0].trim().equals("- Table")) {
                tableName = parts[1].trim();
            } else if (parts[0].trim().equals("PrimaryKey")) {
                primaryKeyColumnName = parts[1].trim();
            } else if (parts[0].trim().equals("  Columns")) {
                columns.clear();
            } else if (parts[0].trim().equals("  Data")) {
                data.clear();
            } else if (!columns.isEmpty() || !data.isEmpty()) {
                if (!columns.isEmpty()) {
                    columns.get(columns.size() - 1).deserialize(str);
                } else {
                    Row row = new Row();
                    row.deserialize(str);
                    data.add(row);
                }
            } else {
                Column column = new Column("", "", 0);
                column.deserialize(str);
                columns.add(column);
            }
        }
    }

    private List<Row> filterData(List<String> columnNames, List<Row> rows) {
        if (Objects.isNull(columnNames)) {
            return rows;
        }
        List<Row> finalRows = new ArrayList<>();
        for (Row row : rows) {
            Map<String, String> data = row.getDataValue();
            Map<String, String> modifiedData = new LinkedHashMap<>();
            for (String column : columnNames) {
                if (data.containsKey(column)) {
                    modifiedData.put(column, data.get(column));
                }
            }
            Row row1 = new Row();
            row1.setDataValue(modifiedData);
            finalRows.add(row1);
        }
        return finalRows;
    }

    boolean isRecordPresent(List<String> columnNames, List<String> values){
        int indexOfPrimaryKeyValue= columnNames.indexOf(primaryKeyColumnName);
        String inputValue = values.get(indexOfPrimaryKeyValue);
        List<Row> data = getDataFromTable(columnNames,primaryKeyColumnName,inputValue);
        if(!data.isEmpty()){
            return true;
        }
        return false;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Table cloned = (Table) super.clone();
        cloned.columns = new ArrayList<>(this.columns.size());
        for (Column column : this.columns) {
            cloned.columns.add((Column) column.clone()); // Assuming Column implements Cloneable
        }
        cloned.data = new ArrayList<>(this.data.size());
        for (Row row : this.data) {
            cloned.data.add((Row) row.clone()); // Assuming Row implements Cloneable
        }
        return cloned;
    }
}
