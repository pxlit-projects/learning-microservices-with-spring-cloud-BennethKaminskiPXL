<script setup>
import WelcomeItem from './WelcomeItem.vue'
import DocumentationIcon from './icons/IconDocumentation.vue'
import ToolingIcon from './icons/IconTooling.vue'
import EcosystemIcon from './icons/IconEcosystem.vue'
import CommunityIcon from './icons/IconCommunity.vue'
import SupportIcon from './icons/IconSupport.vue'
</script>

<template>
  <WelcomeItem>
    <template #icon>
      <DocumentationIcon />
    </template>
    <template #heading>Werknemer</template>

    
    <router-link to="/login">Login</router-link>
    Om de productcatalogus te beheren.
  </WelcomeItem>

  <WelcomeItem>
    <template #icon>
      <ToolingIcon />
    </template>
    <template #heading>Shop till you drop!</template>

    Winkel tot je er bij neervalt! Klik hier : 
    <router-link to="/shop">Shop. En laad je winkelwagen vol!</router-link>
    alles is toch gratis...
    <router-link to="/shop">
    <img src="@/assets/smiley.png" height="175" width="175" alt="shop till you drop" /></router-link>
    <br />
  </WelcomeItem>

  <WelcomeItem>
    <template #icon>
      <SupportIcon />
    </template>
    <template #heading>Logbook</template>

    Voor als je ons filantropisch project wilt steunen... of niet echt vertrouwt
   <router-link to = "/logbook">Klik dan hier om te zien hoe georganiseerd we zijn! We houden onze administratie nauwkeurig bij!</router-link>
  </WelcomeItem>
</template>
