package com.pumpprogress.api.Model;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Credential {
    @NotBlank
    private String email;
    
    @NotBlank
    private String password;
}
