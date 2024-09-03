package br.com.wishlist.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetails {

    private String timestamp;
    private int code;
    private String status;
    private List<String> errors;

}
