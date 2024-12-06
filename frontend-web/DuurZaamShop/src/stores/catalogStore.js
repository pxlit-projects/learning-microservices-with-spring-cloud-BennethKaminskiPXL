import { defineStore } from "pinia";
import axios from 'axios';
import { ref } from "vue";




export const useCatalogStore = defineStore('catalog',{
    state: () => ({
        userName : ref(localStorage.getItem('userName') || ''),
        categories: ["JACKETS",
            "SHIRTS",
            "SHOES",
            "UNDERWEAR"],
        product :  {
            name: '',
            label: '',
            description: '',
            category: '',
            price: 0,
            userName: ""
          },
          products : [],
          id: ref(localStorage.getItem('id') || '')
    }),
    actions:{
        async handleLogin(givenUserName){
            console.log("handleLogin called with: ", givenUserName);
            this.userName = givenUserName;
            localStorage.setItem('userName', givenUserName);

        },
        async addProduct(newProduct){
            newProduct.category = newProduct.category.toUpperCase();
            newProduct.userName = this.userName;
            console.log(newProduct.userName);
            console.log("username in store: ", this.userName);
            this.product = newProduct;
            console.log("Product added: ", this.product);
            const response = await axios.post('http://localhost:8089/catalog/api/catalog', this.product);
            console.log("Response: ", response);
        },
        async getProduct(productId) {
            try {
                const response = await axios.get(`http://localhost:8089/catalog/api/catalog/${productId}`);
                console.log("Product fetched: ", response.data);
                return response.data;
            } catch (error) {
                console.error("Error fetching product: ", error);
                return null;
            }
        },
        async editProduct(product) {
            try {
                product.userName = this.userName;
                const response = await axios.put(`http://localhost:8089/catalog/api/catalog/${product.id}`, product);
                console.log("Product edited: ", response.data);
                return response.data;
            } catch (error) {
                console.error("Error editing product: ", error);
                return null;
            }
        },
        //nog nakijken
        async removeProduct(productId) {
            if (!this.userName) {
                console.error("User name is not set");
                return null;
              }
            try {
                const response = await axios.delete(`http://localhost:8089/catalog/api/catalog/${this.userName}/${productId}`);
                console.log("Product deleted: ", response.data);
                return response.data;
            } catch (error) {
                console.error("Error deleting product: ", error);
                return null;
            }
        },
        //loadProducts
        async loadProducts() {
            try {
                const response = await axios.get('http://localhost:8089/catalog/api/catalog');
                console.log("Products fetched: ", response.data);
                this.products = response.data;
                console.log("Products in store: ", this.products);
                return this.products;
            } catch (error) {
                console.error("Error fetching products: ", error);
                return null;
            }
        },
       
    }

});