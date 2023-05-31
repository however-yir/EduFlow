<template>
  <div class="login-page">
    <div class="overlay"></div>
    <div class="panel">
      <section class="left">
        <p class="hint">EduFlow</p>
        <h1>教务管理全流程工作台</h1>
        <p class="desc">
          覆盖课程管理、选课流程、申请审核与成绩录入，统一接入后端 API，提升教务协同效率。
        </p>
        <ul>
          <li>管理员：成员管理、课程维护、审批处理</li>
          <li>教师：课程申请、课程查看、申请追踪</li>
          <li>学生：选课、退课、课程成绩查询</li>
        </ul>
      </section>

      <section class="right">
        <el-card shadow="never" class="login-card">
          <template #header>
            <div class="card-title">登录平台</div>
          </template>
          <el-form ref="formRef" :model="form" :rules="rules" label-position="top">
            <el-form-item label="身份" prop="authority">
              <el-radio-group v-model="form.authority">
                <el-radio-button label="1">管理员</el-radio-button>
                <el-radio-button label="3">教师</el-radio-button>
                <el-radio-button label="2">学生</el-radio-button>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="账号" prop="account">
              <el-input v-model="form.account" placeholder="请输入账号" clearable />
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input
                v-model="form.password"
                type="password"
                placeholder="请输入密码"
                show-password
                clearable
                @keyup.enter="submit"
              />
            </el-form-item>
            <el-button type="primary" :loading="loading" style="width: 100%" @click="submit">
              登录
            </el-button>
          </el-form>
        </el-card>
      </section>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import { login } from "../api/auth";
import { ROLES } from "../constants/roles";
import { useAuthStore } from "../stores/auth";

const router = useRouter();
const authStore = useAuthStore();
const formRef = ref();
const loading = ref(false);

const form = reactive({
  authority: ROLES.ADMIN,
  account: "",
  password: ""
});

const rules = {
  authority: [{ required: true, message: "请选择身份", trigger: "change" }],
  account: [{ required: true, message: "请输入账号", trigger: "blur" }],
  password: [{ required: true, message: "请输入密码", trigger: "blur" }]
};

function getHome(role) {
  if (role === ROLES.ADMIN) return "/admin/teachers";
  if (role === ROLES.TEACHER) return "/teacher/my-courses";
  if (role === ROLES.STUDENT) return "/student/course-select";
  return "/dashboard";
}

async function submit() {
  await formRef.value?.validate();
  loading.value = true;
  try {
    const res = await login(form);
    authStore.setSession(res.data);
    ElMessage.success("登录成功");
    router.replace(getHome(res.data.role));
  } catch (error) {
    ElMessage.error(error.message || "登录失败");
  } finally {
    loading.value = false;
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: radial-gradient(circle at 16% 16%, #dbeafe, transparent 40%),
    radial-gradient(circle at 84% 12%, #bfdbfe, transparent 35%),
    linear-gradient(135deg, #eff6ff, #eef2ff 48%, #f8fafc);
  position: relative;
  overflow: hidden;
}

.overlay {
  position: absolute;
  inset: 0;
  background-image: linear-gradient(rgba(148, 163, 184, 0.12) 1px, transparent 1px),
    linear-gradient(90deg, rgba(148, 163, 184, 0.12) 1px, transparent 1px);
  background-size: 28px 28px;
  opacity: 0.4;
}

.panel {
  position: relative;
  z-index: 1;
  width: min(1120px, 94vw);
  background: rgba(255, 255, 255, 0.92);
  border: 1px solid rgba(148, 163, 184, 0.25);
  border-radius: 20px;
  box-shadow: 0 24px 60px rgba(15, 23, 42, 0.16);
  display: grid;
  grid-template-columns: 1.1fr 0.9fr;
  overflow: hidden;
}

.left {
  padding: 48px;
  background: linear-gradient(160deg, #0f172a, #1d4ed8 75%);
  color: #fff;
}

.left .hint {
  margin: 0;
  font-size: 13px;
  letter-spacing: 0.12em;
  text-transform: uppercase;
  opacity: 0.86;
}

.left h1 {
  margin: 14px 0 12px;
  font-size: 34px;
  line-height: 1.24;
}

.left .desc {
  margin: 0;
  opacity: 0.94;
}

.left ul {
  margin: 18px 0 0;
  padding-left: 20px;
  display: flex;
  flex-direction: column;
  gap: 8px;
  opacity: 0.94;
}

.right {
  padding: 38px;
  display: flex;
  align-items: center;
}

.login-card {
  width: 100%;
  border-radius: 14px;
}

.card-title {
  font-size: 18px;
  font-weight: 600;
}

@media (max-width: 960px) {
  .panel {
    grid-template-columns: 1fr;
  }

  .left,
  .right {
    padding: 24px;
  }
}
</style>
