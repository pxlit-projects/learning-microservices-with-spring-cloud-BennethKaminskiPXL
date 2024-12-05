<template>
    <div>
      <ProductNavBar />
      <h1>Edit Product</h1>
        <form @submit.prevent="giveId">
            <div>
                <label for="id">Product ID:</label>
                <input type="number" v-model="id" id="id" required />
            </div>
            <button type="submit">Search</button>
        </form>

      <form @submit.prevent="submitForm" v-if="product && product.name">
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
        <button type="submit">Edit</button>
      </form>
    </div>
  </template>
  
  <script>
  import { useCatalogStore } from '@/stores/catalogStore';
  import { computed, ref } from 'vue';
  import ProductNavBar from '@/components/ProductNavBar.vue';
  import { storeToRefs } from 'pinia';
  
  export default {
      components: {
          ProductNavBar
      },
    setup() {
      const catalogStore = useCatalogStore();
      //const id = ref(0);
      const id = computed(() => catalogStore.id);
      const {product} = storeToRefs(catalogStore); 
  
      const categories = computed(() => catalogStore.categories);
  
      const submitForm = async () => {
        await catalogStore.editProduct(product.value);
      };

      const giveId = async () => {
       const targetProduct =  await catalogStore.getProduct(id.value);
       // product.value.category = capitalizeFirstLetter(product.value.category);
       if (targetProduct) {
        product.value.id = id.value;
        product.value.name = targetProduct.name;
        product.value.label = targetProduct.label;
        product.value.description = targetProduct.description;
        product.value.category = targetProduct.category;
        product.value.price = targetProduct.price;
        product.value.userName = targetProduct.userName;
        console.log("Updated product after merging:", product.value);
        console.log("Product category:", product.value.category);
        console.log("Available categories:", categories.value);
      }
      };
      const capitalizeFirstLetter = (str) => {
        return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();
        };
      
  
      return {
        id,
        product,
        categories,
        submitForm,
        giveId,
      };
    }
  };
  </script>
  
  <style scoped>
  /* Add your styles here */
  </style>