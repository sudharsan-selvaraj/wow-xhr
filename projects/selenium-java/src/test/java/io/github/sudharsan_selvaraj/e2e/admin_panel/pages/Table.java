package io.github.sudharsan_selvaraj.e2e.admin_panel.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Table {

    private WebElement parent;
    private List<String> headers;
    private static By tableLocator = By.cssSelector("table.table");

    public Table(WebElement parent) {
        this.parent = parent;
        this.headers = new ArrayList<>();
    }

    public List<String> getHeaders() {
        if (headers.isEmpty()) {
            this.headers = getTable()
                    .findElements(By.tagName("th"))
                    .stream()
                    .map(WebElement::getText)
                    .collect(Collectors.toList());
        }
        return headers;
    }

    public String getColumnValue(String columnName, Integer rowIndex) {
        return getTable()
                .findElement(By.xpath(".//tbody/tr[" + (rowIndex) + "]/descendant::td[" + (getHeaders().indexOf(columnName) + 1) + "]"))
                .getText();
    }

    private WebElement getTable() {
        return parent.findElement(tableLocator);
    }
}
