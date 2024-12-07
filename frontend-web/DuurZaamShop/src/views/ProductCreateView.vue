<template>
  <div>
    <ProductNavBar />
    <h1>Create Product</h1>
    <form @submit.prevent="submitForm">
      <div>
        <label for="name">Name:</label>
        <input type="text" v-model="product.name" id="name" required />
      </div>
      <div>
        <label for="label">Label:</label>
        <input type="text" v-model="product.label" id="label" required />
      </div>
      <div>
        <label for="description">Description:</label>
        <textarea v-model="product.description" id="description" required></textarea>
      </div>
      <div>
        <label for="category">Category:</label>
        <select v-model="product.category" id="category" required>
          <option v-for="category in categories" :key="category" :value="category">{{ category }}</option>
        </select>
      </div>
      <div>
        <label for="price">Price:</label>
        <input type="number" v-model="product.price" id="price" required />
      </div>
      <button type="submit">Submit</button>
    </form>
    <div v-if="showMessage" class="message">
      <p>Product added successfully!</p>
    </div>
  </div>
</template>

<script>
import { useCatalogStore } from '@/stores/catalogStore';
import { computed, ref } from 'vue';
import ProductNavBar from '@/components/ProductNavBar.vue';

export default {
    components: {
        ProductNavBar
    },
  setup() {
    const catalogStore = useCatalogStore();
    const product = ref({
      name: '',
      label: '',
      description: '',
      category: '',
      price: 0,
      userName: ""
    });

    const showMessage = ref(false);

    const categories = computed(() => catalogStore.categories);

    const submitForm = () => {
      catalogStore.addProduct(product.value);
      showMessage.value = true;
      setTimeout(() => {
        showMessage.value = false;
      }, 3000);
    };

    return {
      product,
      categories,
      submitForm,
      showMessage
    };
  }
};
</script>

<style scoped>
/* Add your styles here */
</style>