package nu.eats.domain.store;

import java.math.BigDecimal;

public record Product(String id, String imageUri, String name, BigDecimal price) {
}
