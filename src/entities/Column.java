package entities;

public class Column implements Cloneable {
    String columnName;

    String columnDataType;

    int dataTypeSize;

    Column(String columnName, String columnDataType, int dataTypeSize) {
        this.columnName = columnName;
        this.columnDataType = columnDataType;
        this.dataTypeSize = dataTypeSize;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnDataType() {
        return columnDataType;
    }

    public int getDataTypeSize() {
        return dataTypeSize;
    }

    public void setDataTypeSize(int dataTypeSize) {
        this.dataTypeSize = dataTypeSize;
    }

    // Serialize the Column
    public String serialize(String indent) {
        return indent + "- Column: " + columnName + "\n" +
                indent + "  DataType: " + columnDataType + "\n" +
                indent + "  Size: " + dataTypeSize + "\n";
    }

    // Deserialize the Column
    public void deserialize(String str) {
        String[] parts = str.split(": ");
        if (parts.length > 1) {
            if (parts[0].trim().equals("- Column")) {
                columnName = parts[1].trim();
            } else if (parts[0].trim().equals("DataType")) {
                columnDataType = parts[1].trim();
            } else if (parts[0].trim().equals("Size")) {
                dataTypeSize = Integer.parseInt(parts[1].trim());
            }
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return (Column) super.clone();
    }
}
