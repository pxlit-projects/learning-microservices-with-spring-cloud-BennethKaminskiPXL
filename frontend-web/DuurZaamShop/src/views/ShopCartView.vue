<template>
  <div class="shop-cart">
    <h1>Shopping Cart<img src="@/assets/smiley.png" alt="smiley" width="50" height="50" /> </h1>
    
    <ul>
      <li v-for="item in cartItems" :key="item.id">
        <span>{{ item.name }} - {{ item.quantity }} x {{ item.price }}€ </span>
        <button @click="removeProduct(item.id)" class="remove-button">
          <img src="@/assets/Bin.svg" alt="Remove" class="remove-icon" />
        </button>
      </li>
    </ul>
    <br>
    <button @click="order">Naar gratis Checkout!</button>
  </div>
  
</template>

<script>
import { useShopStore } from '@/stores/shopStore';
import { computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';

export default {
  setup() {
    const shopStore = useShopStore();
    const router = useRouter();
    const cartItems = computed(() => shopStore.cartApiItems);
    const cartId = computed(() => shopStore.cartId);

    const order = async () => {
      router.push({ name: 'checkout' });
    };

    const removeProduct = async (productId) => {
      await shopStore.removeProductFromCart(productId);
      console.log("productId: ", productId);
      shopStore.getProductsInCart(cartId);
    };

    onMounted(async () => {
      await shopStore.getProductsInCart(cartId);
    });

    return {
      cartItems,
      order,
      removeProduct
    };
  }
};
</script>

<style scoped>
.shop-cart {
  padding: 20px;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  padding: 10px;
  border-bottom: 1px solid #ccc;
}

.remove-button {
  background: none;
  border: none;
  cursor: pointer;
  padding: 0;
}

.remove-icon {
  width: 24px;
  height: 24px;
}
</style>