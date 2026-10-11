import { createBrowserRouter, createRoutesFromElements, Route } from "react-router-dom";
import { AppShell } from "../layout/AppShell";
import { HomePage } from "../pages/HomePage";
import { CatalogPage } from "../pages/CatalogPage";
import { AuthPage } from "../pages/AuthPage";
import { ProductPage } from "../pages/ProductPage";
import { RequireAuth } from "./RequireAuth";
import { NotFoundPage } from "../pages/NotFoundPage";
import { CartPage } from "../pages/CartPage";
import { CheckoutPage } from "../pages/CheckoutPage";
import { PaymentReturnPage } from "../pages/PaymentReturnPage";
import { OrdersPage } from "../pages/OrdersPage";
import { LibraryPage } from "../pages/LibraryPage";
import { AdminHomePage } from "../pages/AdminHomePage";
import { AdminProductsPage } from "../pages/AdminProductsPage";
import { AdminNewProductPage } from "../pages/AdminNewProductPage";
import { AdminProductPage } from "../pages/AdminProductPage";
import { AdminEditProductPage } from "../pages/AdminEditProductPage";
import { OrderDetailPage } from "../pages/OrderDetailPage";
import { LibraryProductPage } from "../pages/LibraryProductPage";

export const appRouter = createBrowserRouter(
  createRoutesFromElements(
      <Route element={<AppShell />}>
        <Route index element={<HomePage />} />
        <Route path="beats" element={<CatalogPage category="BEATS" />} />
        <Route path="kits" element={<CatalogPage category="KITS" />} />
        <Route path="courses" element={<CatalogPage category="COURSES" />} />
        <Route path="bundles" element={<CatalogPage category="BUNDLES" />} />
        <Route path="products/:id" element={<ProductPage />} />
        <Route path="login" element={<AuthPage mode="login" />} />
        <Route path="register" element={<AuthPage mode="register" />} />
        <Route
          path="payment/success"
          element={
            <RequireAuth>
              <PaymentReturnPage />
            </RequireAuth>
          }
        />
        <Route
          path="payment/cancel"
          element={
            <RequireAuth>
              <PaymentReturnPage cancelled />
            </RequireAuth>
          }
        />
        <Route path="cart" element={<CartPage />} />
        <Route
          path="checkout"
          element={
            <RequireAuth>
              <CheckoutPage />
            </RequireAuth>
          }
        />
        <Route
          path="orders"
          element={
            <RequireAuth>
              <OrdersPage />
            </RequireAuth>
          }
        />
        <Route
          path="orders/:orderId"
          element={
            <RequireAuth>
              <OrderDetailPage />
            </RequireAuth>
          }
        />
        <Route
          path="library"
          element={
            <RequireAuth>
              <LibraryPage />
            </RequireAuth>
          }
        />
        <Route
          path="library/:productId"
          element={
            <RequireAuth>
              <LibraryProductPage />
            </RequireAuth>
          }
        />
        <Route
          path="admin"
          element={
            <RequireAuth role="ADMIN">
              <AdminHomePage />
            </RequireAuth>
          }
        />
        <Route
          path="admin/products"
          element={
            <RequireAuth role="ADMIN">
              <AdminProductsPage />
            </RequireAuth>
          }
        />
        <Route
          path="admin/products/new"
          element={
            <RequireAuth role="ADMIN">
              <AdminNewProductPage />
            </RequireAuth>
          }
        />
        <Route
          path="admin/products/:id/edit"
          element={
            <RequireAuth role="ADMIN">
              <AdminEditProductPage />
            </RequireAuth>
          }
        />
        <Route
          path="admin/products/:id"
          element={
            <RequireAuth role="ADMIN">
              <AdminProductPage />
            </RequireAuth>
          }
        />
        <Route path="*" element={<NotFoundPage />} />
      </Route>
  ),
);
