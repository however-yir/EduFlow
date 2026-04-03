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
        when(courseMapper.selectCoursesStudents(data)).thenReturn(null);
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
}
