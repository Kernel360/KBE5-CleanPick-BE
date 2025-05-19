package com.kdev5.cleanpick.user.domain;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PreferredManagerId implements Serializable {

    private Long customerId;
    private Long managerId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PreferredManagerId that)) return false;
        return Objects.equals(customerId, that.customerId) &&
                Objects.equals(managerId, that.managerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, managerId);
    }
}
