package com.medthegprod.backend.catalog.infrastructure.persistence;

import com.medthegprod.backend.catalog.application.query.ProductSearchQuery;
import com.medthegprod.backend.catalog.domain.model.Product;
import com.medthegprod.backend.catalog.domain.model.ProductId;
import com.medthegprod.backend.catalog.domain.repository.ProductPage;
import com.medthegprod.backend.catalog.domain.repository.ProductRepository;
import com.medthegprod.backend.catalog.infrastructure.persistence.entity.ProductCategoryEntity;
import com.medthegprod.backend.catalog.infrastructure.persistence.entity.ProductEntity;
import com.medthegprod.backend.catalog.infrastructure.persistence.entity.ProductTypeEntity;
import com.medthegprod.backend.catalog.infrastructure.persistence.mapper.ProductMapper;
import com.medthegprod.backend.catalog.infrastructure.persistence.repository.ProductJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.medthegprod.backend.catalog.infrastructure.persistence.repository.ProductSpecifications;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class ProductPersistenceAdapter implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;

    public ProductPersistenceAdapter(
            ProductJpaRepository productJpaRepository) {
        this.productJpaRepository = productJpaRepository;
    }

    @Override
    @Transactional
    public Product save(Product product) {

        ProductEntity entity = ProductMapper.toEntity(product);

        ProductEntity savedEntity = productJpaRepository.save(entity);

        return ProductMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Product> findById(ProductId productId) {
        return productJpaRepository
                .findByIdWithCategories(productId.value())
                .map(ProductMapper::toDomain);
    }

    @Override
    public ProductPage findAll(ProductSearchQuery query) {

        Sort sort = buildSort(query);

        Pageable pageable = PageRequest.of(
                query.page(),
                query.size(),
                sort);

        Specification<ProductEntity> specification = null;

        if (query.type() != null) {
            Specification<ProductEntity> typeSpecification = ProductSpecifications.hasType(
                    ProductTypeEntity.valueOf(query.type().name()));

            specification = typeSpecification;
        }

        if (query.category() != null) {
            Specification<ProductEntity> categorySpecification = ProductSpecifications.hasCategory(
                    ProductCategoryEntity.valueOf(query.category().name()));

            specification = specification == null
                    ? categorySpecification
                    : specification.and(categorySpecification);
        }

        if (query.search() != null && !query.search().isBlank()) {
            Specification<ProductEntity> searchSpecification = ProductSpecifications.containsText(query.search());

            specification = specification == null
                    ? searchSpecification
                    : specification.and(searchSpecification);
        }

        Page<ProductEntity> result = productJpaRepository.findAll(
                specification,
                pageable);

        return new ProductPage(
                result.getContent()
                        .stream()
                        .map(ProductMapper::toDomain)
                        .toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages());
    }

    private Sort buildSort(ProductSearchQuery query) {

        String property = switch (query.sortBy()) {
            case "price" -> "price";
            case "title" -> "title";
            case "createdAt" -> "createdAt";
            default -> "createdAt";
        };

        Sort.Direction direction = "desc".equalsIgnoreCase(query.direction())
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        return Sort.by(direction, property);
    }
}