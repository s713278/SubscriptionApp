package com.app.payloads;

import java.util.List;

import lombok.Data;

@Data
public class StoreDTO {

	private Long storeId;

	private String name;

	private List<CategoryDTO> categories;

	private String phoneName;

	private String email;

}
