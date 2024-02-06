package com.app.payloads;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AddressDTO {

    @Schema(description="Address1",example = "73 Owen Lane")
    private String address1;
    
    private String address2;
    
    @Schema(description="City",example = "Fort Myers")
    private String city;
    
    @Schema(description="State",example = "Florida")
    private String state;
    
    @Schema(description="Country",example = "United States")
    private String country;
    
    @Schema(description="Zipcode",example = "33901")
    private String pincode;
}
