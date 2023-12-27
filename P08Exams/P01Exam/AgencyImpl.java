package P08Exams.P01Exam;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class AgencyImpl implements Agency {

    private ConcurrentMap<String, Invoice> all;
    private Set<Invoice> payed;
    private ConcurrentMap<LocalDate, ConcurrentMap<String, Invoice>> dueDates;

    public AgencyImpl() {
        this.all = new ConcurrentHashMap<>();
        this.payed = new HashSet<>();
        this.dueDates = new ConcurrentHashMap<>();
    }

    @Override
    public void create(Invoice invoice) {
        if (this.contains(invoice.getNumber())) {
            throw new IllegalArgumentException();
        }

        this.all.put(invoice.getNumber(), invoice);

        ConcurrentMap<String, Invoice> currentByDate = this.dueDates.computeIfAbsent(invoice.getDueDate(),
                k -> new ConcurrentHashMap<>());
        currentByDate.put(invoice.getNumber(), invoice);
    }

    @Override
    public boolean contains(String number) {
        return this.all.containsKey(number);
    }

    @Override
    public int count() {
        return this.all.size();
    }

    @Override
    public void payInvoice(LocalDate dueDate) {
        if (!this.dueDates.containsKey(dueDate)) {
            throw new IllegalArgumentException();
        }

        ConcurrentMap<String, Invoice> atDate = this.dueDates.get(dueDate);
        atDate.forEach((k, v) -> {
            v.setSubtotal(0);
            this.payed.add(v);
            this.all.put(k, v);
        });

        this.dueDates.put(dueDate, atDate);
    }

    @Override
    public void throwInvoice(String number) {
        Invoice invoice = this.all.get(number);

        if (invoice == null) {
            throw new IllegalArgumentException();
        }

        this.dueDates.computeIfPresent(invoice.getDueDate(), (k, v) -> {
            v.remove(invoice.getNumber());
            return v;
        });
        this.all.remove(number);
    }

    @Override
    public void throwPayed() {
        this.payed.forEach(invoice -> {
            this.dueDates.computeIfPresent(invoice.getDueDate(), (k, v) -> {
                v.remove(invoice.getNumber());
                return v;
            });
            this.all.remove(invoice.getNumber());
        });

        this.payed.clear();
    }

    @Override
    public Iterable<Invoice> getAllInvoiceInPeriod(LocalDate startDate, LocalDate endDate) {
        return this.all.values().stream()
                .filter(i -> (i.getIssueDate().isEqual(startDate) || i.getIssueDate().isAfter(startDate))
                        && (i.getIssueDate().isEqual(endDate) || i.getIssueDate().isBefore(endDate)))
                .sorted(Comparator.comparing(Invoice::getIssueDate).thenComparing(Invoice::getDueDate))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Iterable<Invoice> searchByNumber(String number) {
        List<Invoice> result = this.all.values().stream()
                .filter(invoice -> invoice.getNumber().contains(number))
                .collect(Collectors.toUnmodifiableList());

        if (result.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return result;
    }

    @Override
    public Iterable<Invoice> throwInvoiceInPeriod(LocalDate startDate, LocalDate endDate) {
        List<Invoice> result = this.all.values().stream()
                .filter(i -> i.getDueDate().isAfter(startDate) && i.getDueDate().isBefore(endDate))
                .collect(Collectors.toUnmodifiableList());

        if (result.isEmpty()) {
            throw new IllegalArgumentException();
        }

        result.forEach(invoice -> {
            this.dueDates.computeIfPresent(invoice.getDueDate(), (k, v) -> {
                v.remove(invoice.getNumber());
                return v;
            });
            this.all.remove(invoice.getNumber());
        });

        return result;
    }

    @Override
    public Iterable<Invoice> getAllFromDepartment(Department department) {
        return this.all.values().stream()
                .filter(i -> i.getDepartment().equals(department))
                .sorted(Comparator.comparingDouble(Invoice::getSubtotal).reversed()
                        .thenComparing(Invoice::getIssueDate))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Iterable<Invoice> getAllByCompany(String companyName) {
        return this.all.values().stream()
                .filter(i -> i.getCompanyName().equals(companyName))
                .sorted(Comparator.comparing(Invoice::getNumber).reversed())
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public void extendDeadline(LocalDate endDate, int days) {
        if (!this.dueDates.containsKey(endDate)) {
            throw new IllegalArgumentException();
        }

        this.dueDates.get(endDate).forEach((k, v) -> v.setDueDate(v.getDueDate().plusDays(days)));
    }
}