package com.app.payloads;

import java.util.List;

public record CategoryWithProductsDTO(Long id, String name, List<Data> products) {
    public record Data(Long id, String name) {
    }
}
