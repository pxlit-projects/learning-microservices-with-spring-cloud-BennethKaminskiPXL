<template>
    <div>
        <ProductNavBar />
        <h1>Welkom catalogusbeheerder, u kan met de topnavigatiebalk de catalogus beheren.</h1>
    </div>
</template>

<script>
import ProductNavBar from '@/components/ProductNavBar.vue';

export default {
    components: {
        ProductNavBar
    }
}
</script>

<style scoped>
h1 {
    margin-top: 20px;
    text-align: center;
}
</style>