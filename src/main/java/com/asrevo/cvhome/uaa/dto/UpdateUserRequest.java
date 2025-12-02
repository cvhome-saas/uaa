package com.asrevo.cvhome.uaa.dto;

import jakarta.validation.constraints.Email;

public record UpdateUserRequest(@Email String email, String password, String status) {
}
