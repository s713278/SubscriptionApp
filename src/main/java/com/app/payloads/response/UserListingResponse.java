package com.app.payloads.response;

import com.app.payloads.request.UpdateUserRequest;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListingResponse {

  private List<UpdateUserRequest> content;
  private Integer pageNumber;
  private Integer pageSize;
  private Long totalElements;
  private Integer totalPages;
  private boolean lastPage;
}
