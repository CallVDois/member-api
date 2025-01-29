package com.callv2.user.domain.member;

import java.util.Objects;

import com.callv2.user.domain.validation.Error;
import com.callv2.user.domain.ValueObject;
import com.callv2.user.domain.validation.ValidationHandler;

public record Quota(long ammount, QuotaUnit unit) implements ValueObject {

    public static Quota of(long ammount, QuotaUnit unit) {
        return new Quota(ammount, unit);
    }

    @Override
    public void validate(ValidationHandler aHandler) {
        if (Objects.isNull(this.ammount))
            aHandler.append(new Error("'size' should not be null"));

        if (!Objects.isNull(this.ammount) && this.ammount < 0)
            aHandler.append(new Error("'size' should be greater than 0"));

        if (Objects.isNull(this.unit))
            aHandler.append(new Error("'unit' should not be null"));
    }

}
