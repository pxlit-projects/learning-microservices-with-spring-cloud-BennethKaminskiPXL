<template>
    <nav class="navbar">
        <ul>
            <li><a href="/catalog/product/create">Product aanmaken</a></li>
            <li><a href="/catalog/product">Product wijzigen</a></li>
            <li><a href="/catalog/search">Product zoeken</a></li>
        </ul>
    </nav>
</template>

<script>
export default {
    name: 'ProductNavBar'
}
</script>

<style scoped>
.navbar {
    background-color: #333;
    overflow: hidden;
}

.navbar ul {
    list-style-type: none;
    margin: 0;
    padding: 0;
}

.navbar li {
    float: left;
}

.navbar li a {
    display: block;
    color: white;
    text-align: center;
    padding: 14px 16px;
    text-decoration: none;
}

.navbar li a:hover {
    background-color: #111;
}
</style>