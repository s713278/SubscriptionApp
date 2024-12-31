package com.app.payloads.request;

import com.app.controllers.validator.ValidateSkuCreateRequest;
import com.app.payloads.AbstractSkuDTO;
import java.io.Serializable;
import lombok.NoArgsConstructor;

@ValidateSkuCreateRequest
@NoArgsConstructor
public class SkuCreateRequest extends AbstractSkuDTO implements Serializable {}
