import { defineStore } from 'pinia';
import axios from 'axios';
import { ref } from 'vue';

export const useLogBookStore = defineStore('logBook', () => {
  const logs = ref([]);
  const filteredLogs = ref([]);

  const fetchLogs = async () => {
    try {
      const response = await axios.get('http://localhost:8089/logbook/api/logbook');
      logs.value = response.data;
      filteredLogs.value = response.data;
      console.log("Logs fetched:", logs.value);
    } catch (error) {
      console.error("Error fetching logs:", error);
    }
  };

  const filterLogs = (action) => {
    if (action) {
      filteredLogs.value = logs.value.filter(log => log.action === action);
    } else {
      filteredLogs.value = logs.value;
    }
  };

  return {
    logs,
    filteredLogs,
    fetchLogs,
    filterLogs
  };
});