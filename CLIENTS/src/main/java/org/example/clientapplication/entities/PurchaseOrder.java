package org.example.clientapplication.entities;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JacksonXmlRootElement(localName = "PurchaseOrder")
public class PurchaseOrder {

    @JacksonXmlProperty(isAttribute = true, localName = "PurchaseOrderNumber")
    private String purchaseOrderNumber;

    @JacksonXmlProperty(isAttribute = true, localName = "OrderDate")
    private String orderDate;

    @JacksonXmlProperty(isAttribute = true, localName = "Account")
    private Account account;
    @JacksonXmlElementWrapper(localName = "Items")
    @JacksonXmlProperty(localName = "Item")
    private List<Item> items = new ArrayList<>();

    @Data
    public static class Account {
        @JacksonXmlProperty(isAttribute = true, localName = "Type")
        private String type;
        @JacksonXmlProperty
        private String id;
    }

    @Data
    public static class Item {
        @JacksonXmlProperty(localName = "ProductNumber", isAttribute = true)
        private String productNumber;
        @JacksonXmlProperty(localName = "ProductName" )
        private String productName;
        @JacksonXmlProperty(localName = "Quantity")
        private int quantity;
        @JacksonXmlProperty(localName = "SpecialInstruction")
        private String specialInstruction;
    }
}
