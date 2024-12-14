package com.app.entites.type;

import java.util.Arrays;

import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;

public enum VendorStatus {

    ACTIVE, INACTIVE, SUSPENDED;

    public static VendorStatus valueFromString(String status){
        try{
            return valueOf(status);
        }catch (Exception e){
            throw new APIException(APIErrorCode.API_400, "Invalid status.Status should be in onf of the :"+ Arrays.toString(VendorStatus.values()));
        }
    }
}
