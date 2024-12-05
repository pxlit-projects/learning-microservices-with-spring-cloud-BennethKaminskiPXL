<template>
  <div class="shop-checkout">
    <h1>Checkout</h1>
    <ul>
      <li v-for="item in cartItems" :key="item.prodId">
        <span>{{ item.name }} - {{ item.quantity }} x {{ item.price }}€ </span>
      </li>
    </ul>
    <h2>Totale prijs: {{ totalPrice }}€</h2>
    <button @click="pay">Betalen</button>
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
    const cartItems = computed(() => shopStore.cartcheckoutItems);
    const totalPrice = computed(() => shopStore.totalPrice);

    const pay = async () => {
      // Implement payment logic here
      console.log("Proceeding to payment...");
    };

    onMounted(async () => {
      await shopStore.checkout();
    });

    return {
      cartItems,
      totalPrice,
      pay
    };
  }
};
</script>

<style scoped>
.shop-checkout {
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

button {
  margin-top: 20px;
  padding: 10px 20px;
  font-size: 16px;
  cursor: pointer;
}
</style>