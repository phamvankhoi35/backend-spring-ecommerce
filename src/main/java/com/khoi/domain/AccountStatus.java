package com.khoi.domain;

// trạng thái tài khoản
public enum AccountStatus {
    PENDING_VERIFICATION,   // Tài khoản đang chờ xem xét
    ACTIVE,                 // Tài khoản đang hoạt động
    SUSPENDED,              // Tài khoản bị tạm ngưng
    DEACTIVATED,            // Tài khoản bị vô hiệu hóa
    BANNED,                 // Tài khoản bị cấm, bị ban do vi phạm.
    CLOSED                  // Tài khoản đóng vĩnh viễn(có thể đóng theo yêu cầu của người dùng)
}
