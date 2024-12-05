<template>
    <div class="login-container">
        <h1>Login</h1>
        <form @submit.prevent="handleLogin">
            <div class="form-group">
                <label for="username">Username:</label>
                <input type="text" id="username" v-model="username" required />
            </div>
            <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" id="password" v-model="password" required />
            </div>
            <button type="submit" >Login</button>
        </form>
    </div>
</template>

<script>
import { useCatalogStore } from '@/stores/catalogStore';
import {ref} from 'vue';
 import { useRouter } from 'vue-router';
export default {
    setup() {
        const catalogStore = useCatalogStore();
        const router = useRouter();
        const username = ref('');
        const password = ref('');
        const handleLogin = () => {
            catalogStore.handleLogin(username.value);
            console.log(username.value);
            router.push('/catalog');
           
        };
        
        return {
            username,
            password,
            handleLogin
        }
    },
    
};
</script>

<style scoped>
.login-container {
    max-width: 400px;
    margin: 0 auto;
    padding: 20px;
    border: 1px solid #ccc;
    border-radius: 5px;
}

.form-group {
    margin-bottom: 15px;
}

label {
    display: block;
    margin-bottom: 5px;
}

input {
    width: 100%;
    padding: 8px;
    box-sizing: border-box;
}

button {
    width: 100%;
    padding: 10px;
    background-color: #007BFF;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
}

button:hover {
    background-color: #0056b3;
}
</style>