import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from '../views/LoginView.vue'
import ProductHomeView from '@/views/ProductHomeView.vue'
import ProductCreateView from '@/views/ProductCreateView.vue'
import ProductChangeView from '@/views/ProductChangeView.vue'
import ProductSearchView from '@/views/ProductSearchView.vue'
import ShopHomeView from '@/views/ShopHomeView.vue'
import ShopCartView from '@/views/ShopCartView.vue'
import ShopCheckoutView from '@/views/ShopCheckoutView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/about',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: LoginView,
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
    },
    {
      path: '/catalog',
      name: 'catalog',
      component: ProductHomeView,
    },
    {
      path : '/catalog/product/create',
      name : 'product-create',
      component : ProductCreateView,
    },
    {
      path : '/catalog/product/',
      name : 'product-detail',
      component : ProductChangeView,
    },
    {
      path : '/catalog/search',
      name : 'product-search',
      component : ProductSearchView,
    },
    {
      path: '/shop',
      name: 'shop',
      component: ShopHomeView,
    },
    {
      path: '/shop/cart',
      name: 'cart',
      component:ShopCartView,
    },
    {
      path: '/shop/checkout',
      name: 'checkout',
      component:ShopCheckoutView,
    }
   
  ],
})

export default router
