<template>
  <div>
    <h1>Product Search</h1>
    <ul>
      <li v-for="product in products" :key="product.id">
        <span @click="editProduct(product.id)">{{ product.name }}</span>
        <button @click="removeProduct(product.id)" class="remove-button">
          <img src="@/assets/Bin.svg" alt="Remove" class="remove-icon" />
        </button>
      </li>
    </ul>
  </div>
</template>

<script>
import { useCatalogStore } from '@/stores/catalogStore';
import { computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';

export default {
  setup() {
    const catalogStore = useCatalogStore();
    const router = useRouter();
    const products = computed(() => catalogStore.products);

    const loadProducts = async () => {
      await catalogStore.loadProducts();
    };

    const editProduct = async (productId) => {
      await catalogStore.getProduct(productId);
      catalogStore.id = productId;
      router.push({ name: 'product-detail' });
    };

    const removeProduct = async (productId) => {
      await catalogStore.removeProduct(productId);
      await loadProducts(); // Refresh the product list
    };

    onMounted(() => {
      loadProducts();

    });

    return {
      products: catalogStore.products,
      editProduct,
      removeProduct,
      products,
      loadProducts,
    };
  },
};
</script>

<style scoped>
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

span {
  cursor: pointer;
  flex-grow: 1;
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