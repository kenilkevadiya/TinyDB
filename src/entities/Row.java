package entities;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Row implements Cloneable {
    Map<String, String> dataValue;

    public Row() {
        this.dataValue = new LinkedHashMap<>();
    }

    public void setDataValue(Map<String, String> dataValue) {
        this.dataValue = dataValue;
    }

    public Map<String, String> getDataValue() {
        return dataValue;
    }

    public String addDataRow(List<String> columnNames, List<String> values, String primaryKey, int currentIdCounter) {
        //Map<String,String> dataRow = new HashMap<>();
        int i = 0;
        int sizeOfValues = columnNames.size();
        String primaryKeyData = "";
        for (String columnName : columnNames) {
            if (i < sizeOfValues) {
                if (columnName.equalsIgnoreCase(primaryKey)) {
                    primaryKeyData = values.get(i);
                    if (Objects.isNull(primaryKeyData) || primaryKeyData.isEmpty()) {
                        String newId = String.valueOf(currentIdCounter);
                        primaryKeyData = newId;
                        dataValue.putIfAbsent(columnName, newId);
                    } else {
                        dataValue.putIfAbsent(columnName, primaryKeyData);
                    }
                } else {
                    dataValue.putIfAbsent(columnName, values.get(i));
                }
                i++;
            }
        }
        return primaryKeyData;
    }

    public String updateDataRow(List<String> columnNames, List<String> values) {
        System.out.println("The existing data is:" + dataValue);
        for (Map.Entry<String, String> data : dataValue.entrySet()) {
            String key = data.getKey();
            System.out.println("The current Column Name:" + key);
            System.out.println("The current value of the columns is:" + data.getValue());
            if (columnNames.contains(key)) {
                int indexOfColumn = columnNames.indexOf(data.getKey());
                System.out.println("The index of the value in the Values is:" + indexOfColumn);
                String newValue = values.get(indexOfColumn);
                System.out.println("The new Value to be setted is:" + newValue);
                dataValue.put(key, newValue);
            }
        }
        System.out.println("The updated Map is:" + dataValue);
        return "Successfully Updated the required rows";
    }

    public boolean equals(Row rowToCompare) {
        if (rowToCompare.dataValue.equals(dataValue)) {
            return true;
        } else {
            return false;
        }
    }

    // Serialize the Row
    public String serialize(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("- Row:\n");
        for (Map.Entry<String, String> entry : dataValue.entrySet()) {
            sb.append(indent).append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }

    // Deserialize the Row
    public void deserialize(String str) {
        String[] parts = str.split(": ");
        if (parts.length > 1) {
            dataValue.put(parts[0].trim(), parts[1].trim());
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Row cloned = (Row) super.clone();
        cloned.dataValue = new LinkedHashMap<>(this.dataValue);
        return cloned;
    }
}
