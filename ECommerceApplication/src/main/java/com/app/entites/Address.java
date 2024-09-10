package com.app.entites;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Table(name = "tb_addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @NotBlank
    @Size(min = 10, message = "Address1 must contain atleast 10 characters")
    private String address1;

    private String address2;

    @NotBlank
    @Size(min = 4, message = "City name must contain atleast 4 characters")
    private String city;

    @NotBlank
    @Size(min = 2, message = "State name must contain atleast 2 characters")
    private String state;

    @NotBlank
    @Size(min = 2, message = "Country name must contain atleast 2 characters")
    private String country;

    @NotBlank
    @Size(min = 5, max=6 ,message = "Pincode must contain atleast 5 characters")
    private String pincode;

    /*
    @ManyToMany(mappedBy = "addresses")
    private List<Customer> users = new ArrayList<>();*/

    public Address(
            @NotBlank @Size(min = 10, message = "Address1 must contain atleast 10 characters") String address1,
            String address2,
            @NotBlank @Size(min = 4, message = "City name must contain atleast 4 characters") String city,
            @NotBlank @Size(min = 2, message = "State name must contain atleast 2 characters") String state,
            @NotBlank @Size(min = 2, message = "Country name must contain atleast 2 characters") String country,
            @NotBlank @Size(min = 5, max=6, message = "Pincode must contain atleast 6 characters") String pincode) {
        super();
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pincode = pincode;
    }
}
