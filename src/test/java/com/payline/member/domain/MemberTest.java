package com.payline.member.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class MemberTest {

    private static final String VALID_EMAIL = "test@email.com";
    private static final String VALID_PASSWORD = "encodedPassword";
    private static final String VALID_NAME = "홍길동";

    @Nested
    @DisplayName("생성자")
    class Constructor {

        @Test
        @DisplayName("정상적인 값으로 Member를 생성하면 입력값이 정규화되고 deleted=false가 설정된다")
        void shouldCreateMemberWithNormalizedValuesAndDeletedFalse() {
            Member member = new Member("  TEST@Email.com  ", VALID_PASSWORD, "  홍길동  ", MemberRole.USER);

            assertThat(member.getEmail()).isEqualTo(VALID_EMAIL);
            assertThat(member.getPassword()).isEqualTo(VALID_PASSWORD);
            assertThat(member.getName()).isEqualTo(VALID_NAME);
            assertThat(member.getRole()).isEqualTo(MemberRole.USER);
            assertThat(member.isDeleted()).isFalse();
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"  ", "\t"})
        @DisplayName("email이 null이거나 blank이면 IllegalArgumentException이 발생한다")
        void shouldThrowWhenEmailIsNullOrBlank(String email) {
            assertThatThrownBy(() -> new Member(email, VALID_PASSWORD, VALID_NAME, MemberRole.USER))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @ValueSource(strings = {"plain-text", "invalid-email@", "@domain.com", "a@b", "a b@email.com"})
        @DisplayName("email 형식이 올바르지 않으면 IllegalArgumentException이 발생한다")
        void shouldThrowWhenEmailFormatIsInvalid(String email) {
            assertThatThrownBy(() -> new Member(email, VALID_PASSWORD, VALID_NAME, MemberRole.USER))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("format");
        }

        @Test
        @DisplayName("email 길이가 100자를 초과하면 IllegalArgumentException이 발생한다")
        void shouldThrowWhenEmailLengthExceedsLimit() {
            String tooLongEmail = "a".repeat(92) + "@test.com";

            assertThatThrownBy(() -> new Member(tooLongEmail, VALID_PASSWORD, VALID_NAME, MemberRole.USER))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("length");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"  ", "\t"})
        @DisplayName("password가 null이거나 blank이면 IllegalArgumentException이 발생한다")
        void shouldThrowWhenPasswordIsNullOrBlank(String password) {
            assertThatThrownBy(() -> new Member(VALID_EMAIL, password, VALID_NAME, MemberRole.USER))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"  ", "\t"})
        @DisplayName("name이 null이거나 blank이면 IllegalArgumentException이 발생한다")
        void shouldThrowWhenNameIsNullOrBlank(String name) {
            assertThatThrownBy(() -> new Member(VALID_EMAIL, VALID_PASSWORD, name, MemberRole.USER))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("role이 null이면 IllegalArgumentException이 발생한다")
        void shouldThrowWhenRoleIsNull() {
            assertThatThrownBy(() -> new Member(VALID_EMAIL, VALID_PASSWORD, VALID_NAME, null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("name 길이가 20자를 초과하면 IllegalArgumentException이 발생한다")
        void shouldThrowWhenNameLengthExceedsLimit() {
            assertThatThrownBy(() -> new Member(VALID_EMAIL, VALID_PASSWORD, "가나다라마바사아자차카타파하가나다라마바사아", MemberRole.USER))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("length");
        }

        @Test
        @DisplayName("password 길이가 255자를 초과하면 IllegalArgumentException이 발생한다")
        void shouldThrowWhenPasswordLengthExceedsLimit() {
            String tooLongPassword = "a".repeat(256);

            assertThatThrownBy(() -> new Member(VALID_EMAIL, tooLongPassword, VALID_NAME, MemberRole.USER))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("length");
        }
    }

    @Nested
    @DisplayName("changeRole")
    class ChangeRole {

        @Test
        @DisplayName("역할을 변경할 수 있다")
        void shouldChangeRole() {
            Member member = new Member(VALID_EMAIL, VALID_PASSWORD, VALID_NAME, MemberRole.USER);

            member.changeRole(MemberRole.ADMIN);

            assertThat(member.getRole()).isEqualTo(MemberRole.ADMIN);
        }

        @Test
        @DisplayName("null 역할로 변경하면 IllegalArgumentException이 발생한다")
        void shouldThrowWhenNewRoleIsNull() {
            Member member = new Member(VALID_EMAIL, VALID_PASSWORD, VALID_NAME, MemberRole.USER);

            assertThatThrownBy(() -> member.changeRole(null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("삭제된 회원의 역할을 변경하려 하면 IllegalStateException이 발생한다")
        void shouldThrowWhenChangeRoleOfDeletedMember() {
            Member member = new Member(VALID_EMAIL, VALID_PASSWORD, VALID_NAME, MemberRole.USER);
            member.softDelete();

            assertThatThrownBy(() -> member.changeRole(MemberRole.ADMIN))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("deleted");
        }
    }

    @Nested
    @DisplayName("updateInfo")
    class UpdateInfo {

        @Test
        @DisplayName("이름과 비밀번호를 수정할 수 있다")
        void shouldUpdateNameAndPassword() {
            Member member = new Member(VALID_EMAIL, "oldPassword", VALID_NAME, MemberRole.USER);

            member.updateInfo("  김철수  ", "newPassword");

            assertThat(member.getName()).isEqualTo("김철수");
            assertThat(member.getPassword()).isEqualTo("newPassword");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"  ", "\t"})
        @DisplayName("name이 blank이면 IllegalArgumentException이 발생한다")
        void shouldThrowWhenNameIsBlank(String name) {
            Member member = new Member(VALID_EMAIL, VALID_PASSWORD, VALID_NAME, MemberRole.USER);

            assertThatThrownBy(() -> member.updateInfo(name, "newPassword"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"  ", "\t"})
        @DisplayName("password가 blank이면 IllegalArgumentException이 발생한다")
        void shouldThrowWhenPasswordIsBlank(String password) {
            Member member = new Member(VALID_EMAIL, VALID_PASSWORD, VALID_NAME, MemberRole.USER);

            assertThatThrownBy(() -> member.updateInfo("김철수", password))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("삭제된 회원 정보를 수정하려 하면 IllegalStateException이 발생한다")
        void shouldThrowWhenUpdateDeletedMember() {
            Member member = new Member(VALID_EMAIL, VALID_PASSWORD, VALID_NAME, MemberRole.USER);
            member.softDelete();

            assertThatThrownBy(() -> member.updateInfo("김철수", "newPassword"))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("deleted");
        }
    }

    @Nested
    @DisplayName("softDelete")
    class SoftDelete {

        @Test
        @DisplayName("softDelete 호출 시 deleted가 true로 변경된다")
        void shouldSetDeletedToTrue() {
            Member member = new Member(VALID_EMAIL, VALID_PASSWORD, VALID_NAME, MemberRole.USER);

            member.softDelete();

            assertThat(member.isDeleted()).isTrue();
        }
    }
}
