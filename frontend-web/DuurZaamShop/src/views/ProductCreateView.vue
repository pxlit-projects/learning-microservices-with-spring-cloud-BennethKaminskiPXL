<template>
  <div>
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
  </div>
</template>

<script>
import { useCatalogStore } from '@/stores/catalogStore';
import { computed, ref } from 'vue';
export default {
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

    const categories = computed(() => catalogStore.categories);

    const submitForm = () => {
      catalogStore.addProduct(product.value);
    };

    return {
      product,
      categories,
      submitForm,
    };
  }
};
</script>

<style scoped>
/* Add your styles here */
</style>