import { create } from "zustand";
import { createJSONStorage, persist } from "zustand/middleware";

export type CartItem = {
  productId: string;
  title: string;
  price: number;
  currency: string;
  quantity: number;
};

type CartState = {
  items: CartItem[];
  addItem: (item: Omit<CartItem, "quantity">) => void;
  setQuantity: (productId: string, quantity: number) => void;
  removeItem: (productId: string) => void;
  clear: () => void;
};

export const useCartStore = create<CartState>()(
  persist(
    (set) => ({
      items: [],
      addItem: (item) =>
        set((state) => {
          const existing = state.items.find(
            (entry) => entry.productId === item.productId,
          );
          if (existing) {
            return {
              items: state.items.map((entry) =>
                entry.productId === item.productId
                  ? { ...entry, quantity: entry.quantity + 1 }
                  : entry,
              ),
            };
          }
          return { items: [...state.items, { ...item, quantity: 1 }] };
        }),
      setQuantity: (productId, quantity) =>
        set((state) => ({
          items: state.items.map((item) =>
            item.productId === productId
              ? { ...item, quantity: Math.max(1, Math.floor(quantity)) }
              : item,
          ),
        })),
      removeItem: (productId) =>
        set((state) => ({
          items: state.items.filter((item) => item.productId !== productId),
        })),
      clear: () => set({ items: [] }),
    }),
    {
      name: "medtheg.cart",
      storage: createJSONStorage(() => localStorage),
      partialize: (state) => ({ items: state.items }),
    },
  ),
);
