package nu.eats.domain;

public record Vendor(String id, String imageUri, String name) {
    public Vendor(String id, String imageUri, String name) {
        this.id = id;
        this.imageUri = imageUri == null ? "Place Holder" : imageUri;
        this.name = name;
    }
}
