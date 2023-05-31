<template>
  <section class="page-section">
    <h2 class="page-title">系统总览</h2>
    <p class="page-subtitle">当前角色：{{ roleLabel }} · 账号：{{ authStore.name }}（{{ authStore.userId }}）</p>

    <div class="stat-grid" style="margin-top: 18px">
      <article v-for="item in stats" :key="item.label" class="stat-card">
        <div class="label">{{ item.label }}</div>
        <div class="value">{{ item.value }}</div>
        <div class="page-subtitle">{{ item.tip }}</div>
      </article>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted, ref } from "vue";
import { ElMessage } from "element-plus";
import { fetchAllApplications, fetchWaitExaminations } from "../api/application";
import { fetchCourses, fetchStudentSelectedCourses, fetchTeacherCourses } from "../api/course";
import { fetchStudents, fetchTeachers } from "../api/member";
import { ROLES, getRoleLabel } from "../constants/roles";
import { useAuthStore } from "../stores/auth";

const authStore = useAuthStore();
const roleLabel = computed(() => getRoleLabel(authStore.role));

const stats = ref([]);

const emptyCourseQuery = {
  pageSize: 1,
  currentPage: 1,
  param: {
    name: "",
    teacherName: "",
    courseStatusId: null
  }
};

const emptyTeacherQuery = {
  pageSize: 1,
  currentPage: 1,
  param: {
    teacherNumber: "",
    name: "",
    departmentName: ""
  }
};

const emptyStudentQuery = {
  pageSize: 1,
  currentPage: 1,
  param: {
    studentNumber: "",
    name: "",
    studentClass: ""
  }
};

async function loadAdminStats() {
  const [teacherRes, studentRes, courseRes, examinationRes] = await Promise.all([
    fetchTeachers(emptyTeacherQuery),
    fetchStudents(emptyStudentQuery),
    fetchCourses(emptyCourseQuery),
    fetchWaitExaminations("待审批")
  ]);
  stats.value = [
    { label: "教师总数", value: teacherRes.total || 0, tip: "支持分页、筛选与增删改" },
    { label: "学生总数", value: studentRes.total || 0, tip: "班级与学号信息可维护" },
    { label: "课程总数", value: courseRes.total || 0, tip: "课程状态可批量流转" },
    { label: "待审批申请", value: examinationRes.data?.length || 0, tip: "课程新增申请待管理员处理" }
  ];
}

async function loadTeacherStats() {
  const [courseRes, applicationRes] = await Promise.all([
    fetchTeacherCourses(authStore.userId),
    fetchAllApplications(authStore.userId)
  ]);
  stats.value = [
    { label: "我的课程", value: courseRes.data?.length || 0, tip: "查看课程状态与授课安排" },
    { label: "我的申请", value: applicationRes.data?.length || 0, tip: "新增/修改/删除申请追踪" }
  ];
}

async function loadStudentStats() {
  const [selectedRes, allCourseRes] = await Promise.all([
    fetchStudentSelectedCourses(authStore.userId),
    fetchCourses(emptyCourseQuery)
  ]);
  stats.value = [
    { label: "已选课程", value: selectedRes.data?.length || 0, tip: "支持退课与成绩展示" },
    { label: "可检索课程", value: allCourseRes.total || 0, tip: "可按名称与教师筛选课程" }
  ];
}

async function loadStats() {
  try {
    if (authStore.role === ROLES.ADMIN) {
      await loadAdminStats();
    } else if (authStore.role === ROLES.TEACHER) {
      await loadTeacherStats();
    } else {
      await loadStudentStats();
    }
  } catch (error) {
    ElMessage.error(error.message || "加载概览数据失败");
  }
}

onMounted(loadStats);
</script>
