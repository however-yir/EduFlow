package group.teachingmanagerbk.service.impl;

import group.teachingmanagerbk.dto.course.StudentSelectCourseData;
import group.teachingmanagerbk.mapper.CourseMapper;
import group.teachingmanagerbk.mapper.MemberMapper;
import group.teachingmanagerbk.vo.course.Course;
import group.teachingmanagerbk.vo.member.Student;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

    @Mock
    private CourseMapper courseMapper;

    @Mock
    private MemberMapper memberMapper;

    @InjectMocks
    private CourseServiceImpl courseService;

    @Test
    void shouldRejectSelectionWhenTimeConflicts() {
        StudentSelectCourseData data = new StudentSelectCourseData();
        data.setStudentId("s1");
        data.setCourseId("c2");

        Student student = new Student();
        student.setStudentId("s1");

        Course targetCourse = new Course();
        targetCourse.setCourseId("c2");
        targetCourse.setCourseStatusName("可选");
        targetCourse.setTime("Mon-1");

        Course selectedCourse = new Course();
        selectedCourse.setCourseId("c1");
        selectedCourse.setTime("Mon-1");

        when(memberMapper.getStudentInfoById("s1")).thenReturn(student);
        when(courseMapper.getCourseInfoByCourseId("c2")).thenReturn(targetCourse);
        ArrayList<Course> selectedCourses = new ArrayList<>();
        selectedCourses.add(selectedCourse);
        when(courseMapper.getCourseByStudentId("s1")).thenReturn(selectedCourses);

        Exception ex = Assertions.assertThrows(Exception.class, () -> courseService.studentSelectCourse(data));
        Assertions.assertEquals("该课程与已选课程上课时间冲突！", ex.getMessage());
        verify(courseMapper, never()).insertCoursesStudents(any(StudentSelectCourseData.class));
    }

    @Test
    void shouldRejectSelectionWhenCourseNotSelectable() {
        StudentSelectCourseData data = new StudentSelectCourseData();
        data.setStudentId("s1");
        data.setCourseId("c2");

        Student student = new Student();
        student.setStudentId("s1");

        Course targetCourse = new Course();
        targetCourse.setCourseId("c2");
        targetCourse.setCourseStatusName("授课中");
        targetCourse.setTime("Mon-3");

        when(memberMapper.getStudentInfoById("s1")).thenReturn(student);
        when(courseMapper.getCourseInfoByCourseId("c2")).thenReturn(targetCourse);

        Exception ex = Assertions.assertThrows(Exception.class, () -> courseService.studentSelectCourse(data));
        Assertions.assertEquals("当前课程状态不允许选课！", ex.getMessage());
        verify(courseMapper, never()).insertCoursesStudents(any(StudentSelectCourseData.class));
    }

    @Test
    void shouldRejectSelectionWhenCourseIsFull() {
        StudentSelectCourseData data = new StudentSelectCourseData();
        data.setStudentId("s1");
        data.setCourseId("c2");

        Student student = new Student();
        student.setStudentId("s1");

        Course targetCourse = new Course();
        targetCourse.setCourseId("c2");
        targetCourse.setCourseStatusName("可选");
        targetCourse.setTime("Mon-3");
        targetCourse.setMaxStudents(1);
        targetCourse.setCurrentStudents(1);

        Course lockedCourse = new Course();
        lockedCourse.setCourseId("c2");
        lockedCourse.setCourseStatusName("可选");
        lockedCourse.setMaxStudents(1);
        lockedCourse.setCurrentStudents(1);

        ReflectionTestUtils.setField(courseService, "enrollmentLockMode", "PESSIMISTIC");
        when(memberMapper.getStudentInfoById("s1")).thenReturn(student);
        when(courseMapper.getCourseInfoByCourseId("c2")).thenReturn(targetCourse);
        when(courseMapper.getCourseByStudentId("s1")).thenReturn(new ArrayList<>());
        when(courseMapper.lockCourseById("c2")).thenReturn(lockedCourse);
        when(courseMapper.selectCoursesStudents(data)).thenReturn(null);

        Exception ex = Assertions.assertThrows(Exception.class, () -> courseService.studentSelectCourse(data));
        Assertions.assertEquals("该课程已选或选课人数已满", ex.getMessage());
        verify(courseMapper, never()).insertCoursesStudents(any(StudentSelectCourseData.class));
    }

    @Test
    void shouldRejectSelectionWhenOptimisticVersionConflicts() {
        StudentSelectCourseData data = new StudentSelectCourseData();
        data.setStudentId("s1");
        data.setCourseId("c2");

        Student student = new Student();
        student.setStudentId("s1");

        Course targetCourse = new Course();
        targetCourse.setCourseId("c2");
        targetCourse.setCourseStatusName("可选");
        targetCourse.setTime("Mon-3");
        targetCourse.setMaxStudents(60);
        targetCourse.setCurrentStudents(10);
        targetCourse.setVersion(3);

        ReflectionTestUtils.setField(courseService, "enrollmentLockMode", "OPTIMISTIC");
        when(memberMapper.getStudentInfoById("s1")).thenReturn(student);
        when(courseMapper.getCourseInfoByCourseId("c2")).thenReturn(targetCourse);
        when(courseMapper.getCourseByStudentId("s1")).thenReturn(new ArrayList<>());
        when(courseMapper.selectCoursesStudents(data)).thenReturn(null);
        when(courseMapper.increaseCurrentStudentsOptimistic("c2", 3)).thenReturn(0);

        Exception ex = Assertions.assertThrows(Exception.class, () -> courseService.studentSelectCourse(data));
        Assertions.assertEquals("该课程已选或选课人数已满", ex.getMessage());
        verify(courseMapper, times(1)).insertCoursesStudents(data);
    }
}
