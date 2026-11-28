package com.zone.boot.domain.valueobject;

import java.util.Objects;

/**
 * 金额值对象（示例链路演示用）
 * 以「分」为单位存 long，杜绝浮点误差；不可变、按值判等
 *
 * @param amountFen 金额（分），不允许为负
 */
public record Money(long amountFen) {

    public Money {
        if (amountFen < 0) {
            throw new IllegalArgumentException("金额不允许为负：amountFen=" + amountFen);
        }
    }

    /** 静态工厂：按分构造 */
    public static Money ofFen(long amountFen) {
        return new Money(amountFen);
    }

    /** 是否大于另一金额（实体不变式判断用） */
    public boolean isGreaterThan(Money other) {
        return this.amountFen > other.amountFen;
    }

    /** 乘法：单价 × 数量（数量为正整数） */
    public Money multiply(int factor) {
        if (factor <= 0) {
            throw new IllegalArgumentException("乘数必须为正整数：factor=" + factor);
        }
        return new Money(amountFen * factor);
    }

    /** 减法：总额 - 优惠（结果为负即非法，由紧凑构造器统一守卫） */
    public Money subtract(Money other) {
        Objects.requireNonNull(other, "被减金额不能为空");
        return new Money(this.amountFen - other.amountFen);
    }

    /** 元展示文本（两位小数） */
    public String toYuanDisplay() {
        return String.format("%.2f", amountFen / 100.0);
    }
}
