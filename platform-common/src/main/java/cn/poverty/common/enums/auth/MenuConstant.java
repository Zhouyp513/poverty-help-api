package cn.poverty.common.enums.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author
 * @packageName cn.poverty.common.enums
 * @Description: 菜单常量
 * @date 2021-02-16
 */
@Getter
@AllArgsConstructor
public class MenuConstant {

    /**
     * 菜单类型
     */
    public enum MenuType {

        /**
         * 目录
         */
        CATALOG(0),
        /**
         * 菜单
         */
        MENU(1),
        /**
         * 按钮
         */
        BUTTON(2);

        private int value;

        MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

}
