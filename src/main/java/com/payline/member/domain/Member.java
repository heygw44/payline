package com.payline.member.domain;

import com.payline.global.entity.BaseEntity;
import com.payline.global.error.BusinessException;
import com.payline.global.error.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    private static final int EMAIL_MAX_LENGTH = 100;
    private static final int PASSWORD_MAX_LENGTH = 255;
    private static final int NAME_MAX_LENGTH = 20;
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = EMAIL_MAX_LENGTH)
    private String email;

    @Column(nullable = false, length = PASSWORD_MAX_LENGTH)
    private String password;

    @Column(nullable = false, length = NAME_MAX_LENGTH)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private MemberRole role;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public Member(String email, String password, String name, MemberRole role) {
        this.email = normalizeAndValidateEmail(email);
        this.password = validatePassword(password);
        this.name = normalizeAndValidateName(name);
        this.role = validateRole(role);
    }

    public void changeRole(MemberRole newRole) {
        assertNotDeleted();
        this.role = validateRole(newRole);
    }

    public void updateInfo(String name, String encodedPassword) {
        assertNotDeleted();
        this.name = normalizeAndValidateName(name);
        this.password = validatePassword(encodedPassword);
    }

    public void softDelete() {
        if (this.deletedAt == null) {
            this.deletedAt = LocalDateTime.now();
        }
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }

    private String normalizeAndValidateEmail(String email) {
        String normalizedEmail = normalizeAndValidateNotBlank(email).toLowerCase(Locale.ROOT);
        validateMaxLength(normalizedEmail, EMAIL_MAX_LENGTH);
        if (!EMAIL_PATTERN.matcher(normalizedEmail).matches()) {
            throw new BusinessException(ErrorCode.MEMBER_EMAIL_INVALID_FORMAT);
        }
        return normalizedEmail;
    }

    private String validatePassword(String password) {
        String validPassword = normalizeAndValidateNotBlank(password);
        validateMaxLength(validPassword, PASSWORD_MAX_LENGTH);
        return validPassword;
    }

    private String normalizeAndValidateName(String name) {
        String normalizedName = normalizeAndValidateNotBlank(name);
        validateMaxLength(normalizedName, NAME_MAX_LENGTH);
        return normalizedName;
    }

    private MemberRole validateRole(MemberRole role) {
        if (role == null) {
            throw new BusinessException(ErrorCode.MEMBER_ROLE_REQUIRED);
        }
        return role;
    }

    private String normalizeAndValidateNotBlank(String value) {
        if (value == null || value.isBlank()) {
            throw new BusinessException(ErrorCode.MEMBER_FIELD_REQUIRED);
        }
        return value.trim();
    }

    private void validateMaxLength(String value, int maxLength) {
        if (value.length() > maxLength) {
            throw new BusinessException(ErrorCode.MEMBER_FIELD_LENGTH_EXCEEDED);
        }
    }

    private void assertNotDeleted() {
        if (deletedAt != null) {
            throw new BusinessException(ErrorCode.MEMBER_DELETED);
        }
    }

}
