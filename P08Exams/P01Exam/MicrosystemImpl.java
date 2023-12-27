package P08Exams.P01Exam;

import P08Exams.P01Exam.Brand;
import P08Exams.P01Exam.Computer;
import P08Exams.P01Exam.Microsystem;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MicrosystemImpl implements Microsystem {

    private Map<Integer, Computer> computerByNumber;

    public MicrosystemImpl() {
        this.computerByNumber = new HashMap<>();
    }

    @Override
    public void createComputer(Computer computer) {
        if (computerByNumber.containsKey(computer.getNumber())) {
            throw new IllegalArgumentException();
        }
        computerByNumber.putIfAbsent(computer.getNumber(), computer);
    }

    @Override
    public boolean contains(int number) {
        return computerByNumber.containsKey(number);
    }

    @Override
    public int count() {
        return computerByNumber.size();
    }

    @Override
    public Computer getComputer(int number) {
        Computer computer = computerByNumber.get(number);
        if (computer == null) {
            throw new IllegalArgumentException();
        }
        return computer;
    }

    @Override
    public void remove(int number) {
        Computer computer = computerByNumber.get(number);
        if (computer == null) {
            throw new IllegalArgumentException();
        }
        computerByNumber.remove(computer.getNumber());
    }

    @Override
    public void removeWithBrand(Brand brand) {
        List<Computer> computersToRemove = computerByNumber.values()
                .stream()
                .filter(computer -> {
                    Brand computerBrand = computer.getBrand();
                    return computerBrand != null && computerBrand.equals(brand);
                })
                .collect(Collectors.toList());

        if (computersToRemove.isEmpty()) {
            throw new IllegalArgumentException();
        }

        computersToRemove.forEach(computer -> {
            computerByNumber.remove(computer.getNumber());
        });


    }

    @Override
    public void upgradeRam(int ram, int number) {
        Computer computer = computerByNumber.get(number);
        if (computer == null) {
            throw new IllegalArgumentException();
        }
        int ramToFix = computerByNumber.get(computer.getNumber()).getRAM();
        if (ram > ramToFix) {
            computerByNumber.get(computer.getNumber()).setRAM(ram);
        }
    }

    @Override
    public Iterable<Computer> getAllFromBrand(Brand brand) {
        List<Computer> computers = computerByNumber.values()
                .stream()
                .filter(computer -> {
                    Brand computerBrand = computer.getBrand();
                    return computerBrand != null && computerBrand.equals(brand);
                }).sorted(Comparator.comparing(Computer::getPrice).reversed())
                .collect(Collectors.toList());

        return computers;
    }

    @Override
    public Iterable<Computer> getAllWithScreenSize(double screenSize) {
        List<Computer> computers = computerByNumber.values()
                .stream()
                .filter(computer -> {
                    double computerScreenSize = computer.getScreenSize();
                    return computerScreenSize == screenSize;
                }).sorted(Comparator.comparing(Computer::getNumber).reversed())
                .collect(Collectors.toList());

        return computers;
    }

    @Override
    public Iterable<Computer> getAllWithColor(String color) {
        List<Computer> computers = computerByNumber.values()
                .stream()
                .filter(computer -> {
                    String computerColor = computer.getColor();
                    return computerColor.equals(color);
                }).sorted(Comparator.comparing(Computer::getPrice).reversed())
                .collect(Collectors.toList());

        return computers;
    }

    @Override
    public Iterable<Computer> getInRangePrice(double minPrice, double maxPrice) {
        List<Computer> computers = computerByNumber.values()
                .stream()
                .filter(computer -> {
                    double computerPrice = computer.getPrice();
                    return computerPrice >= minPrice && computerPrice <= maxPrice;
                }).sorted(Comparator.comparing(Computer::getPrice).reversed())
                .collect(Collectors.toList());

        return computers;
    }
}
