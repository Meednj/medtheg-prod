import { Route, Routes } from 'react-router-dom'
import { AppShell } from '../layout/AppShell'
import { HomePage } from '../pages/HomePage'
import { NotFoundPage } from '../pages/NotFoundPage'
import { PlaceholderPage } from '../pages/PlaceholderPage'

export function AppRoutes() {
  return (
    <Routes>
      <Route element={<AppShell />}>
        <Route index element={<HomePage />} />
        <Route path="beats" element={<PlaceholderPage rhythm="beats" eyebrow="Catalogue / 01" title="Beats" detail="An index of independent instrumentals is being prepared." />} />
        <Route path="kits" element={<PlaceholderPage rhythm="kits" eyebrow="Catalogue / 02" title="Kits" detail="A considered collection of production tools is on its way." />} />
        <Route path="courses" element={<PlaceholderPage rhythm="courses" eyebrow="Catalogue / 03" title="Courses" detail="Producer-led learning will take its place in this index." />} />
        <Route path="bundles" element={<PlaceholderPage rhythm="bundles" eyebrow="Catalogue / 04" title="Bundles" detail="Selected production goods, gathered together." />} />
        <Route path="products/:id" element={<PlaceholderPage eyebrow="Release / Product" title="Product detail" detail="The artwork, listening preview, and release information will live here." />} />
        <Route path="login" element={<PlaceholderPage eyebrow="Customer / 01" title="Sign in" detail="A secure customer sign-in experience will be added in the next phase." />} />
        <Route path="register" element={<PlaceholderPage eyebrow="Customer / 02" title="Create account" detail="Customer registration will be added in the next phase." />} />
        <Route path="payment/success" element={<PlaceholderPage eyebrow="Checkout / Return" title="Payment received?" detail="Payment confirmation is asynchronous and will be shown only from order status." />} />
        <Route path="payment/cancel" element={<PlaceholderPage eyebrow="Checkout / Return" title="Checkout closed" detail="Your cart will be available when you return." />} />
        <Route path="cart" element={<PlaceholderPage eyebrow="Customer / 03" title="Your cart" detail="Your selected releases will be gathered here." />} />
        <Route path="checkout" element={<PlaceholderPage eyebrow="Customer / 04" title="Checkout" detail="Checkout will hand off to the secure payment session." />} />
        <Route path="orders" element={<PlaceholderPage eyebrow="Customer / 05" title="Order archive" detail="A private record of your purchases." />} />
        <Route path="library" element={<PlaceholderPage eyebrow="Customer / 06" title="Private library" detail="Your acquired production goods, held in a private digital vault." />} />
        <Route path="library/:productId" element={<PlaceholderPage eyebrow="Customer / 06" title="Library release" detail="Authorized files and product details will be gathered here." />} />
        <Route path="admin" element={<PlaceholderPage eyebrow="Studio / Admin" title="Production desk" detail="A professional product workbench, grounded in the available admin API." />} />
        <Route path="admin/products" element={<PlaceholderPage eyebrow="Studio / Admin" title="Product index" detail="Create a draft or look up a product by ID. The API has no all-products endpoint." />} />
        <Route path="admin/products/new" element={<PlaceholderPage eyebrow="Studio / Admin" title="New draft" detail="Create a product draft and add supported assets." />} />
        <Route path="admin/products/:id/edit" element={<PlaceholderPage eyebrow="Studio / Admin" title="Edit release" detail="Edit product details and categories supported by the backend." />} />
        <Route path="admin/products/:id" element={<PlaceholderPage eyebrow="Studio / Admin" title="Product workbench" detail="Review a product lifecycle state, details, and supported asset operations." />} />
        <Route path="*" element={<NotFoundPage />} />
      </Route>
    </Routes>
  )
}
