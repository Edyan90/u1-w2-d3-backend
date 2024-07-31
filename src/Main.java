import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) {
        Supplier<Long> randomNumberSupplier = () -> {
            Random rndm = new Random();
            return rndm.nextLong(1, 1000);
        };
        Supplier<Double> randomEuroSupplier = () -> {
            Random prezzo = new Random();
            return prezzo.nextDouble(1, 300);
        };
        Supplier<Product> prodottiSupplier = () -> {
            Faker f = new Faker(Locale.ITALY);
            return new Product(randomNumberSupplier.get(), f.book().title(), f.book().genre(), randomEuroSupplier.get());
        };
        Supplier<List<Product>> randomListProdotti = () -> {
            List<Product> prodotti = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                prodotti.add(prodottiSupplier.get());
            }
            return prodotti;
        };

        List<Product> prodottiEddy = randomListProdotti.get();
        prodottiEddy.forEach(System.out::println);
        System.out.println("-------------------ex1----------------------");
        Predicate<Product> isMoreThanOneHundred = product -> product.getPrice() > 100;
        prodottiEddy.stream()
                .filter(isMoreThanOneHundred)
                .forEach(System.out::println);
        System.out.println("-------------------ex2----------------------");
        Supplier<Customer> customerSupplier = () -> {
            Faker f = new Faker(Locale.ITALY);
            return new Customer(randomNumberSupplier.get(), "Eddy", f.number().numberBetween(1, 5));
        };
        Supplier<Product> prodottiSupplierBaby = () -> {
            Faker f = new Faker(Locale.ITALY);
            return new Product(randomNumberSupplier.get(), f.book().title(), "BABY", randomEuroSupplier.get());
        };
        Supplier<List<Product>> randomListProdottiBaby = () -> {
            List<Product> prodotti = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                prodotti.add(prodottiSupplierBaby.get());
            }
            return prodotti;
        };
        Supplier<Order> orderSupplier = () -> {
            Faker f = new Faker(Locale.ITALY);
            Long id = randomNumberSupplier.get();
            String status = "In Consegna";
            LocalDate orderDate = LocalDate.now();
            LocalDate deliveryDate = orderDate.plusDays(1);
            List<Product> productsBaby = randomListProdottiBaby.get();
            Customer customer = customerSupplier.get();
            return new Order(id, status, orderDate, deliveryDate, productsBaby, customer);
        };
        List<Order> ordini = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ordini.add(orderSupplier.get());
        }
        ordini.forEach(System.out::println);
        System.out.println("-------------------ex3----------------------");
        Supplier<Product> prodottiSupplierBoys = () -> {
            Faker f = new Faker(Locale.ITALY);
            return new Product(randomNumberSupplier.get(), f.book().title(), "Boys", (randomEuroSupplier.get() - randomEuroSupplier.get() * 0.1));
        };
        Supplier<List<Product>> randomListProdottiBoys = () -> {
            List<Product> prodotti = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                prodotti.add(prodottiSupplierBoys.get());
            }
            return prodotti;
        };
        Supplier<Order> orderSupplierBoys = () -> {
            Faker f = new Faker(Locale.ITALY);
            Long id = randomNumberSupplier.get();
            String status = "In Consegna";
            LocalDate orderDate = LocalDate.now().minusMonths(4).minusYears(3);
            LocalDate deliveryDate = orderDate.plusDays(3);
            List<Product> productsBoys = randomListProdottiBoys.get();
            Customer customer = customerSupplier.get();
            return new Order(id, status, orderDate, deliveryDate, productsBoys, customer);
        };
        List<Order> ordiniBoys = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ordiniBoys.add(orderSupplierBoys.get());
        }
        ordiniBoys.forEach(System.out::println);
        System.out.println("-------------------ex4----------------------");
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 8, 1);

        Predicate<Order> isTier2 = order ->
                order.getCustomer().getTier() == 2 &&
                        (order.getOrderDate().isEqual(startDate) || order.getOrderDate().isAfter(startDate)) &&
                        (order.getOrderDate().isEqual(endDate) || order.getOrderDate().isBefore(endDate));

        List<Product> prodottiTier2InRange = ordini.stream()
                .filter(isTier2)
                .flatMap(order -> order.getProducts().stream())
                .toList();

        prodottiTier2InRange.forEach(System.out::println);
    }
}
