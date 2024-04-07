package com.booking.specification;

import com.booking.entity.Booking;
import com.booking.model.BookingFilter;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    private UserSpecification() {
    }

    public static Specification<Booking> filterBy(BookingFilter filter) {
        return Specification
                .where(withEmail(filter.getEmail()))
                .and(withSection(filter.getSectionId()));
    }

    private static Specification<Booking> withEmail(String email) {
        return ((root, query, cb) -> email == null || email.isEmpty() ? cb.conjunction() : cb.equal(root.get("email"), email));
    }

    private static Specification<Booking> withSection(String section) {
        return ((root, query, cb) -> section == null || section.isEmpty() ? cb.conjunction() : cb.equal(root.get("sectionAllocated"), section));
    }

}
