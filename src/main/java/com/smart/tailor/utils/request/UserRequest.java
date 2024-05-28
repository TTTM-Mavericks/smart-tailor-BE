package com.smart.tailor.utils.request;


import com.smart.tailor.enums.Provider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    private String email;

    private String password;

    private String language;

    private String phoneNumber;

    private String fullName;

    private String address;

    @JsonIgnore
    private Provider provider;
}
