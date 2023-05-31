<template>
  <el-aside class="sidebar" :width="collapsed ? '72px' : '232px'">
    <div class="brand" @click="$router.push('/dashboard')">
      <span class="logo">E</span>
      <span v-if="!collapsed" class="name">EduFlow</span>
    </div>

    <el-scrollbar>
      <el-menu
        class="menu"
        :collapse="collapsed"
        :default-active="activePath"
        unique-opened
        @select="onSelect"
      >
        <el-menu-item
          v-for="item in menus"
          :key="item.path"
          :index="item.path"
        >
          <el-icon><component :is="item.icon" /></el-icon>
          <template #title>{{ item.title }}</template>
        </el-menu-item>
      </el-menu>
    </el-scrollbar>
  </el-aside>
</template>

<script setup>
const props = defineProps({
  menus: {
    type: Array,
    default: () => []
  },
  activePath: {
    type: String,
    default: ""
  },
  collapsed: {
    type: Boolean,
    default: false
  }
});

const emit = defineEmits(["menu-select"]);

function onSelect(path) {
  emit("menu-select", path);
}
</script>

<style scoped>
.sidebar {
  background: #0f172a;
  color: #d0d6e3;
  border-right: 1px solid #1e293b;
  transition: width 0.25s ease;
}

.brand {
  height: 64px;
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 0 14px;
  cursor: pointer;
  border-bottom: 1px solid #1e293b;
}

.logo {
  width: 34px;
  height: 34px;
  border-radius: 10px;
  background: linear-gradient(145deg, #3b82f6, #1d4ed8);
  color: #fff;
  font-weight: 700;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.name {
  color: #f8fafc;
  font-size: 17px;
  font-weight: 600;
}

.menu {
  border-right: none;
  background: transparent;
}

.menu :deep(.el-menu-item) {
  color: #d0d6e3;
  margin: 6px 8px;
  border-radius: 10px;
}

.menu :deep(.el-menu-item.is-active) {
  background: rgba(59, 130, 246, 0.24);
  color: #fff;
}

.menu :deep(.el-menu-item:hover) {
  background: rgba(148, 163, 184, 0.12);
}
</style>
