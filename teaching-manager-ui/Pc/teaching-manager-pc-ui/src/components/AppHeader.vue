<template>
  <header class="header">
    <div class="left">
      <el-button text @click="$emit('toggle-collapse')">
        <el-icon><Fold v-if="!collapsed" /><Expand v-else /></el-icon>
      </el-button>
      <div class="title-wrap">
        <p class="title">{{ title }}</p>
        <p class="subtitle">{{ subtitle }}</p>
      </div>
    </div>

    <div class="right">
      <el-tag effect="plain" type="primary">{{ roleLabel }}</el-tag>
      <el-dropdown>
        <span class="dropdown-trigger">
          {{ userName }}
          <el-icon><ArrowDown /></el-icon>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="$router.push('/profile/password')">密码修改</el-dropdown-item>
            <el-dropdown-item divided @click="$emit('logout')">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </header>
</template>

<script setup>
import { ArrowDown, Expand, Fold } from "@element-plus/icons-vue";

defineProps({
  title: {
    type: String,
    default: "教务管理"
  },
  subtitle: {
    type: String,
    default: ""
  },
  userName: {
    type: String,
    default: ""
  },
  roleLabel: {
    type: String,
    default: ""
  },
  collapsed: {
    type: Boolean,
    default: false
  }
});

defineEmits(["logout", "toggle-collapse"]);
</script>

<style scoped>
.header {
  height: 64px;
  border-bottom: 1px solid #e5e7eb;
  background: #fff;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 18px;
}

.left,
.right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.title-wrap .title {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
}

.title-wrap .subtitle {
  margin: 2px 0 0;
  font-size: 12px;
  color: #6b7280;
}

.dropdown-trigger {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  cursor: pointer;
  color: #334155;
}
</style>
