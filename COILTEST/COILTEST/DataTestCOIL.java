import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;

public class DataTestCOIL {
    public static void main(String[] args) {
        String csvFile = "COILTEST/datatest.csv";
        String line;
        String delimiter = ",";

        // HashMaps for storing the total units sold and revenue by product
        HashMap<String, Integer> totalUnitsSold = new HashMap<>();
        HashMap<String, Double> totalRevenue = new HashMap<>();

        // HashMaps for storing total sales and transaction count per city
        HashMap<String, Double> totalSalesPerCity = new HashMap<>();
        HashMap<String, Integer> transactionCountPerCity = new HashMap<>();

        DecimalFormat df = new DecimalFormat("#.00");

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                String[] values = line.split(delimiter);

                String products = values[5];
                int units = Integer.parseInt(values[7]);
                double price = Double.parseDouble(values[6]);
                double totalSale = Double.parseDouble(values[9]);

                String city = values[2];

                // Update product-wise totals
                totalUnitsSold.put(products, totalUnitsSold.getOrDefault(products, 0) + units);
                totalRevenue.put(products, totalRevenue.getOrDefault(products, 0.0) + price);

                // Update city-wise total sales and transaction count
                totalSalesPerCity.put(city, totalSalesPerCity.getOrDefault(city, 0.0) + totalSale);
                transactionCountPerCity.put(city, transactionCountPerCity.getOrDefault(city, 0) + 1);
            }

            // Print product-wise total units sold and revenue
            System.out.println("Product-wise Total Units Sold and Revenue:");
            for (String products : totalUnitsSold.keySet()) {
                int units = totalUnitsSold.get(products);
                double revenue = totalRevenue.get(products);
                System.out.println(products + ", " + units + ", " + df.format(revenue));
            }

            // Print city-wise total sales and average sales
            System.out.println("\nCity-wise Total Sales and Average Sales:");
            for (String city : totalSalesPerCity.keySet()) {
                double totalSales = totalSalesPerCity.get(city);
                int transactionCount = transactionCountPerCity.get(city);
                double averageSales = totalSales / transactionCount;
                System.out.println(city + ", Total Sales: " + df.format(totalSales) + ", Average Sales: " + df.format(averageSales));
            }

            // Write the output to a CSV file
            String outputCsvFile = "DataOUT.csv";
            try (FileWriter writer = new FileWriter(outputCsvFile)) {
                // Write product-wise totals
                writer.append("Product,Total Units Sold,Total Revenue\n");
                for (String products : totalUnitsSold.keySet()) {
                    int units = totalUnitsSold.get(products);
                    double revenue = totalRevenue.get(products);
                    writer.append(products + "," + units + "," + df.format(revenue) + "\n");
                }

                // Write city-wise totals and averages
                writer.append("\nCity,Total Sales,Average Sales\n");
                for (String city : totalSalesPerCity.keySet()) {
                    double totalSales = totalSalesPerCity.get(city);
                    int transactionCount = transactionCountPerCity.get(city);
                    double averageSales = totalSales / transactionCount;
                    writer.append(city + "," + df.format(totalSales) + "," + df.format(averageSales) + "\n");
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }
}
