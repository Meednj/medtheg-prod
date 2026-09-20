package com.medthegprod.backend.catalog.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void newProductShouldBeDraft() {
        Product product = createProduct();
        assertEquals(ProductStatus.DRAFT, product.getStatus());
    }

    @Test
    void draftProductCanBePublished() {
        Product product = createProduct();
        product.addCategory(ProductCategory.BEATS);

        product.publish();

        assertEquals(ProductStatus.PUBLISHED, product.getStatus());
    }

    @Test
    void publishedProductCanBeArchived() {
        Product product = createProduct();
        product.addCategory(ProductCategory.BEATS);

        product.publish();
        product.archive();

        assertEquals(ProductStatus.ARCHIVED, product.getStatus());
    }

    @Test
    void draftProductCannotBeArchived() {
        Product product = createProduct();

        assertThrows(
                IllegalStateException.class,
                product::archive);
    }

    @Test
    void productCannotHaveNegativePrice() {
        assertThrows(
                IllegalArgumentException.class,
                () -> Money.eur(new BigDecimal("-10.00")));
    }

    @Test
    void productCannotHaveBlankTitle() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Product(
                        ProductId.generate(),
                        "   ",
                        "Test description",
                        ProductType.BEAT,
                        Money.eur(new BigDecimal("19.99"))));
    }

    @Test
    void productCanAddAsset() {
        Product product = createProduct();

        DigitalAsset asset = new DigitalAsset(
                AssetId.generate(),
                DigitalAssetType.AUDIO_PREVIEW,
                "beats/dark-trap/preview.mp3");

        product.addAsset(asset);

        assertEquals(1, product.getAssets().size());
        assertEquals(asset, product.getAssets().getFirst());
    }

    @Test
    void productCannotAddSameAssetTwice() {
        Product product = createProduct();

        DigitalAsset asset = new DigitalAsset(
                AssetId.generate(),
                DigitalAssetType.AUDIO_WAV,
                "beats/dark-trap/beat.wav");

        product.addAsset(asset);

        assertThrows(
                IllegalArgumentException.class,
                () -> product.addAsset(asset));
    }

    @Test
    void productCanRemoveAsset() {
        Product product = createProduct();

        DigitalAsset asset = new DigitalAsset(
                AssetId.generate(),
                DigitalAssetType.AUDIO_WAV,
                "beats/dark-trap/beat.wav");

        product.addAsset(asset);
        product.removeAsset(asset.getId());

        assertTrue(product.getAssets().isEmpty());
    }

    @Test
    void productCanHaveCategories() {
        Product product = createProduct();

        product.addCategory(ProductCategory.BEATS);

        assertTrue(
                product.getCategories().contains(ProductCategory.BEATS));
    }

    @Test
    void productCanHaveMultipleCategories() {
        Product product = createProduct();

        product.addCategory(ProductCategory.BEATS);
        product.addCategory(ProductCategory.BUNDLES);

        assertEquals(2, product.getCategories().size());
    }

    @Test
    void productCanRemoveCategory() {
        Product product = createProduct();

        product.addCategory(ProductCategory.BEATS);
        product.removeCategory(ProductCategory.BEATS);

        assertFalse(
                product.getCategories().contains(ProductCategory.BEATS));
    }

    @Test
    void productCannotHaveNullCategory() {
        Product product = createProduct();

        assertThrows(
                NullPointerException.class,
                () -> product.addCategory(null));
    }

    @Test
    void shouldReconstituteProductWithPersistedStatus() {

        Product product = Product.reconstitute(
                ProductId.generate(),
                "Dark Trap Beat",
                "Dark trap instrumental",
                ProductType.BEAT,
                Money.eur(new BigDecimal("19.99")),
                ProductStatus.PUBLISHED);

        assertEquals(
                ProductStatus.PUBLISHED,
                product.getStatus());
    }

    private Product createProduct() {
        return new Product(
                ProductId.generate(),
                "Dark Trap Beat",
                "Dark trap instrumental",
                ProductType.BEAT,
                Money.eur(new BigDecimal("19.99")));
    }

    @Test
    void shouldAddAsset() {

        Product product = createProduct();

        DigitalAsset asset = new DigitalAsset(
                AssetId.generate(),
                DigitalAssetType.AUDIO_WAV,
                "products/test/full.wav");

        product.addAsset(asset);

        assertEquals(1, product.getAssets().size());
        assertEquals(
                asset.getId(),
                product.getAssets().getFirst().getId());
    }

    @Test
    void shouldRejectDuplicateAsset() {

        Product product = createProduct();

        AssetId assetId = AssetId.generate();

        DigitalAsset asset = new DigitalAsset(
                assetId,
                DigitalAssetType.AUDIO_WAV,
                "products/test/full.wav");

        product.addAsset(asset);

        DigitalAsset duplicate = new DigitalAsset(
                assetId,
                DigitalAssetType.AUDIO_MP3,
                "products/test/full.mp3");

        assertThrows(
                IllegalArgumentException.class,
                () -> product.addAsset(duplicate));
    }

    @Test
    void shouldRejectRemovingUnknownAsset() {

        Product product = createProduct();

        assertThrows(
                IllegalArgumentException.class,
                () -> product.removeAsset(AssetId.generate()));
    }
}
