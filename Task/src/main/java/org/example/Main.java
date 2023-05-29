import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the file path: ");
        String filePath = scanner.nextLine();

        List<String> csvFiles = listCSVFiles(filePath);
        System.out.println("CSV files in the directory:");
        for (int i = 0; i < csvFiles.size(); i++) {
            System.out.println((i + 1) + ". " + csvFiles.get(i));
        }

        System.out.print("Choose a file to read (enter the number): ");
        int fileChoice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        String chosenFile = csvFiles.get(fileChoice - 1);
        List<String[]> table = readCSVFile(chosenFile);

        System.out.println("Table:");
        printTable(table);

        System.out.print("Enter the column number to print: ");
        int columnChoice = scanner.nextInt();
        scanner.close();

        System.out.println("Printing the first column and column " + columnChoice + ":");
        printColumns(table, columnChoice);
    }

    private static List<String> listCSVFiles(String filePath) {
        List<String> csvFiles = new ArrayList<>();

        File directory = new File(filePath);
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().toLowerCase().endsWith(".csv")) {
                        csvFiles.add(file.getAbsolutePath());
                    }
                }
            }
        }

        return csvFiles;
    }

    private static List<String[]> readCSVFile(String filePath) {
        List<String[]> table = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true; // Flag to skip the first line
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip the first line (header)
                }
                String[] row = line.split(",");
                table.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return table;
    }

    private static void printTable(List<String[]> table) {
        for (String[] row : table) {
            for (String cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }

    private static void printColumns(List<String[]> table, int columnChoice) {
        List<Boolean> results = new ArrayList<>();

        for (int i = 0; i < table.size(); i++) {
            String[] row = table.get(i);
            if (columnChoice < row.length) {
                String cellValue = row[columnChoice].trim();
                boolean conditionMet = checkConditions(cellValue, columnChoice);
                results.add(conditionMet);
                System.out.print(row[0] + " " + row[columnChoice]);
                System.out.println();
            }
        }

        boolean allConditionsMet = results.stream().allMatch(Boolean::booleanValue);

        if (allConditionsMet) {
            System.out.println("Day is suitable");
        } else {
            System.out.println("Day is not suitable");
        }
    }

    private static boolean checkConditions(String cellValue, int columnIndex) {
        switch (columnIndex) {
            case 1: // Temperature (C)
                try {
                    int value = Integer.parseInt(cellValue);
                    return value >= 28 && value <= 31;
                } catch (NumberFormatException e) {
                    return false;
                }
            case 2: // Wind (m/s)
                try {
                    int value = Integer.parseInt(cellValue);
                    return value >= 5 && value <= 10;
                } catch (NumberFormatException e) {
                    return false;
                }
            case 3: // Humidity (%)
                try {
                    int value = Integer.parseInt(cellValue);
                    return value < 60;
                } catch (NumberFormatException e) {
                    return false;
                }
            case 4: // Precipitation (%)
                if (cellValue.equals("0") || cellValue.equals("0.0")) {
                    return true;
                } else {
                    return false;
                }
            case 5: // Lightning
                return !cellValue.equals("No");
            case 6: // Clouds
                return !cellValue.equals("Cumulus") && !cellValue.equals("Nimbus");
            default:
                return true;
        }
    }
}
