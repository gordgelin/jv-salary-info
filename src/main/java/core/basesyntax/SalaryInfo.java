package core.basesyntax;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class SalaryInfo {
    private final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final Pattern dataEntryPattern =
            Pattern.compile("^(\\d{2}.\\d{2}.\\d{4}) (\\w+) (\\d+) (\\d+)$");
    private final String DATAITEMS_SEPARATOR = " ";
    private final String REPORTDATA_SEPARATOR = " - ";

    public String getSalaryInfo(String[] names, String[] data, String dateFrom, String dateTo) {
        StringBuilder reportBuilder = new StringBuilder();
        LocalDate localDateFrom;
        LocalDate localDateTo;

        if (names == null || data == null || names.length == 0 || data.length == 0) {
            throw new IllegalArgumentException("Data and employees list should not be empty!");
        }

        try {
            localDateFrom = LocalDate.parse(dateFrom, DATETIME_FORMATTER);
            localDateTo = LocalDate.parse(dateTo, DATETIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Dates should be valid dates in dd.MM.yyyy format!");
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Range dates should not be null!");
        }

        reportBuilder.append("Report for period ").append(dateFrom)
                .append(REPORTDATA_SEPARATOR).append(dateTo);

        for (String name : names) {
            int salary = 0;
            LocalDate date;
            if (name == null || name.isBlank()) {
                continue;
            }
            for (String dataEntry : data) {
                if (!dataEntryPattern.matcher(dataEntry).matches()) {
                    continue;
                }
                String[] dataItems = dataEntry.split(DATAITEMS_SEPARATOR);

                try {
                    date = LocalDate.parse(dataItems[0], DATETIME_FORMATTER);
                } catch (DateTimeParseException e) {
                    continue;
                }

                if (name.equals(dataItems[1]) && date.isAfter(localDateFrom.minusDays(1))
                        && date.isBefore(localDateTo.plusDays(1))) {
                    salary += Integer.parseInt(dataItems[2]) * Integer.parseInt(dataItems[3]);
                }
            }
            reportBuilder.append(System.lineSeparator()).append(name)
                    .append(REPORTDATA_SEPARATOR).append(salary);
        }
        return reportBuilder.toString();
    }
}
