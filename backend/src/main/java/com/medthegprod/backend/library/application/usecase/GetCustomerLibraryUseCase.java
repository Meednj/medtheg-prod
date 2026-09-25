package com.medthegprod.backend.library.application.usecase;

import java.util.List;
import java.util.UUID;

public interface GetCustomerLibraryUseCase {

    List<LibraryItem> execute(UUID customerId);
}