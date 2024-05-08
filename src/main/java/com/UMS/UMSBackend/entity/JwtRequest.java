package com.UMS.UMSBackend.entity;

import lombok.*;


@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtRequest {
    private String email;
    private String password;

    private String username;
}
