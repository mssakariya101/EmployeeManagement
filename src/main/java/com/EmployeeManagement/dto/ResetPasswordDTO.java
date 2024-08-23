package com.EmployeeManagement.dto;

import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResetPasswordDTO {
    String email;
    String currentPassword;
    String newPassword;
}
