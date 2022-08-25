package com.herren.project.employees.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.herren.project.shop.domain.Shop;
import com.herren.project.shop.domain.ShopStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class EmployeeTest {

    @ParameterizedTest(name = "직원의 재직 상태가 {0}으로 변경된다")
    @EnumSource(value = EmployeeStatus.class, names = {"NORMAL", "DELETE"})
    void changeStaffStatus(EmployeeStatus expected) {
        Shop shop = new Shop("헤어샵", "12345", "010-1234-1234", "kakaoid", ShopStatus.WAITING);
        Employee employee = new Employee("직원A", "010-1234-1234", "kakao@kakao.com", EmployeeStatus.NORMAL, shop);

        employee.changeStaffStatus(expected);

        assertThat(employee.getStaffStatus()).isEqualTo(expected);
    }

    @Test
    @DisplayName("직원의 상태가 삭제로 변경되면 해당 직원의 이름, 연락처, 카카오톡 아이디를 삭제한다")
    void changeStaffStatusThenChangeStaffInfo() {
        Shop shop = new Shop("헤어샵", "12345", "010-1234-1234", "kakaoid", ShopStatus.WAITING);
        Employee employee = new Employee("직원A", "010-1234-1234", "kakao@kakao.com", EmployeeStatus.NORMAL, shop);

        employee.changeStaffStatus(EmployeeStatus.DELETE);

        assertAll(
                () -> assertThat(employee.getName()).isNull(),
                () -> assertThat(employee.getPhoneNumber()).isNull(),
                () -> assertThat(employee.getKakaoId()).isNull()
        );
    }

    @Test
    @DisplayName("직원의 상태가 삭제로 변경되면 해당 직원이 속해있던 샵 정보에서 직원 정보를 삭제한다")
    void changeStaffStatusThenDeleteStaffInfoToShop() {
        Shop shop = new Shop("헤어샵", "12345", "010-1234-1234", "kakaoid", ShopStatus.WAITING);
        Employee employee = new Employee("직원A", "010-1234-1234", "kakao@kakao.com", EmployeeStatus.NORMAL, shop);
        shop.updateStaff(employee);
        int actual = shop.getStaff().getStaff().size();

        employee.changeStaffStatus(EmployeeStatus.DELETE);

        assertThat(actual).isNotEqualTo(shop.getStaff().getStaff().size());
    }
}