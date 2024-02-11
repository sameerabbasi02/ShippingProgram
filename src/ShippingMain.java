import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class ShippingMain {
    /**
     * Self Explanation
     * Loops through fileScanner, looking  at each line. If the line contains any of the keywords of the Product object
     * which must also be created, the line is added to the object.
     *
     */
    public static Product createProduct(Scanner fileScanner) {
        String name = "";
        int sku = -1;
        double price = -1;
        double weight = -1;
        int destination = -1;
        int quantity = -1;
        String line = "";
        String lastChar = "";

        Product productObj = new Product(name, sku, price, weight, destination, quantity);

        while (fileScanner.hasNextLine()) {
            line = fileScanner.nextLine();
            //lastChar = String.valueOf(line.charAt(-1));
            if (line.contains("NAME")) {
                productObj.name = line.substring(line.indexOf(":") + 2).concat(lastChar);
            }
            else if (line.contains("SKU")) {
                productObj.sku = Integer.parseInt(line.substring(line.indexOf(":") + 2).concat(lastChar));
            }
            else if (line.contains("PRICE")) {
                productObj.price = Double.parseDouble(line.substring(line.indexOf(":") + 2).concat(lastChar));
            }
            else if (line.contains("WEIGHT")) {
                productObj.weight = Double.parseDouble(line.substring(line.indexOf(":") + 2).concat(lastChar));
            }
            else if (line.contains("DESTINATION")) {
                productObj.destination = Integer.parseInt(line.substring(line.indexOf(":") + 2).concat(lastChar));
            }
            else if (line.contains("QUANTITY")) {
                productObj.quantity = Integer.parseInt(line.substring(line.indexOf(":") + 2).concat(lastChar));
            }
        }

        return productObj;
    }

    public static ShippingManifest createManifest(ArrayList<File> fileList) {
        FileHelper fileHelper = new FileHelper();
        ShippingManifest shipManifest = new ShippingManifest();

        for(File file: fileList) {
            Scanner fileScanner = fileHelper.getFileScanner(file);

            Product newProd = createProduct(fileScanner);
            shipManifest.addProduct(newProd);

            fileScanner.close();
        }

        return shipManifest;
    }

    public static void printSplash() {
        System.out.println("Please type:");
        System.out.println("\"X\" to exit program.");
        System.out.println("\"D\" to distribute products to addresses.");
        System.out.println("\"F-ZIPCODE\" to forwards products to destinations.");
        System.out.println("\"P\" to print current manifest.");
    }
    /**
     * Self Explanation
     * Prints splash screen. While user input is not "X", depending on input of user, ShippingManifest is accessed
     * and manipulated.
     *
     */
    public static void go(Scanner scnr, ShippingManifest shipManifest) {
        printSplash();

        String input = scnr.nextLine();

        while (!input.startsWith("X")) {
            if (input.startsWith("D")) {
                shipManifest.distributeProducts();
            }
            else if (input.startsWith("F")) {
                int zipcode = Integer.parseInt(input.substring(2));
                shipManifest.forwardProducts(zipcode);
            }
            else if(input.startsWith("P")) {
                shipManifest.printManifest();
            }
            else {
                System.out.println("unrecognized command");
            }
            input = scnr.nextLine();
        }
    }

    public static void main(String[] args) {
        String directoryPath = "ShipmentFolder";
        FileHelper fileHelper = new FileHelper();
        ArrayList<File> fileList = fileHelper.getFileDirectory(directoryPath);

        ShippingManifest shipManifest = createManifest(fileList);
        System.out.println("Manifest created from " + directoryPath + "!");

        shipManifest.printManifest();

        Scanner scnr = new Scanner(System.in);
        go(scnr, shipManifest);
    }
}