import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { useCatalogStore } from '@/stores/catalogStore';
import axios from 'axios';

export const useShopStore = defineStore('shop', () => {
    //deze is voor UI, de echte cart wordt bijgehouden met api, database
  const cartItems = ref([]);
  const cartApiItems = ref([]);
  const productStore = useCatalogStore();
  const cart = ref(localStorage.getItem("cartNumber") || null);
  const cartCheckoutItems = ref([]);
  const totalPrice = ref(0);

  const addToCart = async (product, quantity) => {
    //puur voor UI
    const existingItem = cartItems.value.find(item => item.prodId === product.id);
     if (existingItem) {
      existingItem.quantity += quantity;
    } else {
      cartItems.value.push({
        id: Date.now(),
        prodId: product.id,
        name: product.name,
        price: product.price,
        quantity
      });
    } 
    //puur voor UI
try {
        const response = await axios.post(`http://localhost:8089/cart/api/cart/${cart.value}/addProduct/${product.id}/${quantity}`);
        console.log("Product added to cart: ", response.data);
} catch (error) {
    console.error("Error adding product to cart: ", error);
}
  };

  const filterProducts = (searchQuery, filters) => {
    return productStore.products.filter(product => {
      return (
        (!searchQuery || product.name.toLowerCase().includes(searchQuery.toLowerCase())) &&
        (!filters.category || product.category === filters.category) &&
        (!filters.price || product.price <= filters.price) &&
        (!filters.label || product.label.toLowerCase().includes(filters.label.toLowerCase()))
      );
    });
  };

  const createCart = async () => {
    try {
      const response = await axios.post('http://localhost:8089/cart/api/cart');
      cart.value = response.data;
      localStorage.setItem("cartNumber", cart.value);
      console.log("Cart created:", cart.value);
    } catch (error) {
      console.error("Error creating cart:", error);
    }
  };

  const getProductsInCart = async () => {
    try {
      const response = await axios.get(`http://localhost:8089/cart/api/cart/${cart.value}/products`);
      cartApiItems.value = response.data;
      console.log("Products in cart:", cartApiItems.value);
    } catch (error) {
      console.error("Error fetching products in cart:", error);
    }
  };

  const removeProductFromCart = async (productId) => {
    try {
      const response = await axios.delete(`http://localhost:8089/cart/api/cart/${cart.value}/removeProduct/${productId}`);
      console.log("productId: ", productId);
      console.log("Product removed from cart:", response.data);
    } catch (error) {
      console.error("Error removing product from cart:", error);
    }
  }

  const checkout = async () => {
    try {
      const response = await axios.post(`http://localhost:8089/cart/api/cart/${cart.value}/checkout`);
      console.log("Checkout successful:", response.data);
      cartCheckoutItems.value = response.data.products;
      totalPrice.value = response.data.totalPrice;
      console.log("Total price:", totalPrice.value);
      console.log("Checkout items:", cartCheckoutItems.value);
      
    } catch (error) {
      console.error("Error checking out:", error);

  }
  };
  

  return {
    cartItems,
    addToCart,
    filterProducts,
    createCart,
    cart,
    cartApiItems,
    getProductsInCart,
    removeProductFromCart,
    checkout,
    cartCheckoutItems,
    totalPrice
  };
});