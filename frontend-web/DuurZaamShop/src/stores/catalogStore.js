import { defineStore } from "pinia";
import axios from 'axios';

const JACKETS = "Jackets";
const SHIRTS = "Shirts";
const SHOES = "Shoes";
const UNDERWEAR = "Underwear";


export const useCatalogStore = defineStore('catalog',{
    state: () => ({
        userName : "",
        categories: [JACKETS,
            SHIRTS,
            SHOES,
            UNDERWEAR],
        product :  {
            name: '',
            label: '',
            description: '',
            category: '',
            price: 0,
            userName: ""
          }
    }),
    actions:{
        async handleLogin(givenUserName){
            console.log("handleLogin called with: ", givenUserName);
            this.userName = givenUserName;
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
        }
       
    }

})