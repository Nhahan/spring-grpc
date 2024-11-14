package org.example.client.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;

import java.util.List;

@Getter
@Setter
@Builder
public class UserRequest {
    private String id;
    private UserDetails user;
    @Singular
    private List<Item> items;

    @Getter
    @Setter
    @Builder
    public static class UserDetails {
        private String username;
        private String email;
        private Address address;
    }

    @Getter
    @Setter
    @Builder
    public static class Address {
        private String street;
        private String city;
        private String state;
        private String zip;
    }

    @Getter
    @Setter
    @Builder
    public static class Item {
        private String itemId;
        private String name;
        private String description;
        private float price;
        private int quantity;
    }

    @Getter
    @Setter
    @Builder
    public static class UserResponse {
        private String message;
        private List<ItemSummary> itemSummaries;
    }

    @Getter
    @Setter
    @Builder
    public static class ItemSummary {
        private String itemId;
        private String name;
        private float totalPrice;
    }
}
