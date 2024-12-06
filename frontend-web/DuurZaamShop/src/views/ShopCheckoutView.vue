<template>
  <div class="shop-checkout">
    <h1>Checkout</h1>
    <ul>
      <li v-for="item in cartItems" :key="item.id">
        <span>{{ item.name }} - {{ item.quantity }} x {{ item.price }}€ </span>
      </li>
    </ul>
    <h2>Totale prijs: {{ totalPrice }}€</h2>
    <button @click="pay">Betalen</button>
    <div v-if="showMessage" class="winner-message">
      <p>U hebt gewonnen! Ik ben eigenlijk een rijke sjeik die miljarden heeft geërfd. Ik kan jammer genoeg niet bij mijn geld. Ik heb iemand nodig
        om mij 10.000 euro te storten zodat ik mijn geld kan vrijmaken. Als beloning geef ik u 1 miljoen euro.
         Stuur het in bitcoins door naar benneth@student.pxl.be !</p>
         <img src="@/assets/sjeik.jpg" alt="sjeik" height="250" width="250" />
    </div>
  </div>
</template>

<script>
import { useShopStore } from '@/stores/shopStore';
import { computed, onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

export default {
  setup() {
    const shopStore = useShopStore();
    const router = useRouter();
    const cartItems = computed(() => shopStore.cartCheckoutItems);
    const totalPrice = computed(() => shopStore.totalPrice);
    const showMessage = ref(false);

    const pay = async () => {
      shopStore.totalPrice = 0;
      showMessage.value = true; 
      console.log("Proceeding to payment...");
    };

    onMounted(async () => {
      await shopStore.checkout();
    });

    return {
      cartItems,
      totalPrice,
      pay,
      showMessage
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