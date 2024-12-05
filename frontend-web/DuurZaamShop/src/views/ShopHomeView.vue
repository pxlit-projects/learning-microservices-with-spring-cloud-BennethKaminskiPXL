<template>
  <div>
    <div class="top-bar">
      <input type="text" v-model="searchQuery" placeholder="Search products..." @input="filterProducts" />
      <button @click="toggleAdvancedSearch">Advanced Search</button>
      <div class="cart" @click="goToCart">
        <span>Cart ({{ totalCartQuantity }})</span>
      </div>
    </div>
    <div v-if="showAdvancedSearch" class="advanced-search">
      <label for="category">Category:</label>
      <select v-model="filters.category" @change="filterProducts">
        <option value="">All</option>
        <option v-for="category in categories" :key="category" :value="category">{{ category }}</option>
      </select>
      <label for="price">Max Price:</label>
      <input type="number" v-model="filters.price" @input="filterProducts" />
      <label for="label">Label:</label>
      <input type="text" v-model="filters.label" @input="filterProducts" />
    </div>
    <ul>
      <li v-for="product in filteredProducts" :key="product.id">
        <span>{{ product.name }} {{ product.label }} {{ product.category }} {{ product.price }}€ </span>
        <input type="number" v-model.number="quantities[product.id]" min="1" />
        <button @click="addToCart(product, quantities[product.id])">+</button>
      </li>
    </ul>
  </div>
</template>

<script>
import { useCatalogStore } from '@/stores/catalogStore';
import { useShopStore } from '@/stores/shopStore';
import { computed, ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';

export default {
  setup() {
    const productStore = useCatalogStore();
    const shopStore = useShopStore();
    const router = useRouter();

    const searchQuery = ref('');
    const showAdvancedSearch = ref(false);
    const filters = ref({
      category: '',
      price: null,
      label: ''
    });
    const quantities = ref({});

    const categories = computed(() => productStore.categories);
    const filteredProducts = computed(() => {
      return shopStore.filterProducts(searchQuery.value, filters.value);
    });
    const cartItems = computed(() => shopStore.cartItems);

    const loadProducts = async () => {
      await productStore.loadProducts();
      productStore.products.forEach(product => {
        quantities.value[product.id] = 1;
      });
    };

    const totalCartQuantity = computed(() => {
      return cartItems.value.reduce((total, item) => total + item.quantity, 0);
    });

    const filterProducts = () => {
      shopStore.filterProducts(searchQuery.value, filters.value);
    };

    const toggleAdvancedSearch = () => {
      showAdvancedSearch.value = !showAdvancedSearch.value;
    };

    const addToCart = (product, quantity) => {
      shopStore.addToCart(product, quantity);
      quantities.value[product.id] = 1;
    };

    const goToCart = () => {
      router.push({ name: 'cart'});
    };

    const createCart = () => {
      shopStore.createCart();
    };

    onMounted(() => {
      loadProducts();
      createCart();
    });

    return {
      searchQuery,
      showAdvancedSearch,
      filters,
      quantities,
      categories,
      filteredProducts,
      cartItems,
      filterProducts,
      toggleAdvancedSearch,
      addToCart,
      goToCart,
      createCart,
      totalCartQuantity
    };
  }
};
</script>

<style scoped>
.top-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  background-color: #f8f8f8;
  border-bottom: 1px solid #ccc;
}

.advanced-search {
  padding: 10px;
  background-color: #f8f8f8;
  border-bottom: 1px solid #ccc;
}

.cart {
  cursor: pointer;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px;
  border-bottom: 1px solid #ccc;
}

input[type="number"] {
  width: 50px;
}
</style>