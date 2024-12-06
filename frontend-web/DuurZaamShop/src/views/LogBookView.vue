<template>
  <div class="log-book">
    <h1>Log Book</h1>
    <div class="filter">
      <label for="action">Filter by Action:</label>
      <select v-model="selectedAction" @change="filterLogs">
        <option value="">All</option>
        <option v-for="action in uniqueActions" :key="action" :value="action">{{ action }}</option>
      </select>
    </div>
    <ul>
      <li v-for="log in filteredLogs" :key="log.id">
        <span>{{ log.changed }} - {{ log.userName }} - {{ log.action }} - {{ log.name }} - {{ log.label }} - {{ log.description }} - {{ log.category }} - {{ log.price }}€</span>
      </li>
    </ul>
  </div>
</template>

<script>
import { useLogBookStore } from '@/stores/logBookStore';
import { computed, ref, onMounted } from 'vue';

export default {
  setup() {
    const logBookStore = useLogBookStore();
    const selectedAction = ref('');

    const uniqueActions = computed(() => {
      const actions = logBookStore.logs.map(log => log.action);
      return [...new Set(actions)];
    });

    const filteredLogs = computed(() => logBookStore.filteredLogs);

    const filterLogs = () => {
      logBookStore.filterLogs(selectedAction.value);
    };

    onMounted(async () => {
      await logBookStore.fetchLogs();
    });

    return {
      selectedAction,
      uniqueActions,
      filteredLogs,
      filterLogs
    };
  }
};
</script>

<style scoped>
.log-book {
  padding: 20px;
}

.filter {
  margin-bottom: 20px;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  padding: 10px;
  border-bottom: 1px solid #ccc;
}
</style>