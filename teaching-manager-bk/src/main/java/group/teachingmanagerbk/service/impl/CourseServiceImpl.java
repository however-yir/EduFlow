package group.teachingmanagerbk.service.impl;

import group.teachingmanagerbk.dto.course.QueryCourseParam;
import group.teachingmanagerbk.dto.course.StudentSelectCourseData;
import group.teachingmanagerbk.mapper.CourseMapper;
import group.teachingmanagerbk.mapper.MemberMapper;
import group.teachingmanagerbk.service.CourseService;
import group.teachingmanagerbk.utils.ReturnResult.Result;
import group.teachingmanagerbk.utils.ReturnResult.ResultWithTotal;
import group.teachingmanagerbk.vo.course.Course;
import group.teachingmanagerbk.vo.course.Place;
import group.teachingmanagerbk.vo.member.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    MemberMapper memberMapper;

    @Override
    public Course getCourseById(String courseId) {
        return courseMapper.getCourseInfoByCourseId(courseId);
    }

    @Override
    public Result getCourseByCondition(QueryCourseParam json) {
        Integer currentPage = json.getCurrentPage();
        Integer pageSize = json.getPageSize();
        Integer index = pageSize * (currentPage - 1);
        Course param = json.getParam();
        int total = courseMapper.getCourseCountByCondition(param);
        ArrayList<Course> courses = courseMapper.getCourseByCondition(param, index, pageSize);
        ResultWithTotal result = new ResultWithTotal();
        result.setTotal(total);
        result.setData(courses);
        return result.success();
    }

    @Override
    public ArrayList<Place> getAllPlace() {
        return courseMapper.getAllPlace();
    }

    @Override
    public void insertANewCourse(Course json) throws Exception {
        courseMapper.insertCourse(json);
    }

    @Override
    public void updateCourse(Course json) {
        courseMapper.modifyCourse(json);
    }

    @Override
    public void deleteCourse(Course json) {
        String courseId = json.getCourseId();
        courseMapper.deleteCourseByIds(new String[] {courseId});
    }

    @Override
    public String getCourseSwitchStatus() {
        return courseMapper.getCourseSwitchStatus();
    }

    @Override
    public void updateCourseStatus(String courseSwitchStatus) throws Exception {
        if ("1".equals(courseSwitchStatus)) {
            String oleCourseStatusId = courseMapper.getCourseStatusIdByName("待选");
            String newCourseStatusId = courseMapper.getCourseStatusIdByName("可选");
            courseMapper.updateCourseStatus(newCourseStatusId,oleCourseStatusId);
            courseMapper.updateCourseSwitchStatus(courseSwitchStatus);
        } else if ("0".equals(courseSwitchStatus)) {
            String oleCourseStatusId = courseMapper.getCourseStatusIdByName("可选");
            String newCourseStatusId = courseMapper.getCourseStatusIdByName("授课中");
            courseMapper.updateCourseStatus(newCourseStatusId,oleCourseStatusId);
            courseMapper.updateCourseSwitchStatus(courseSwitchStatus);
        } else {
            throw new Exception("传入的切换状态码有误！");
        }
    }

    @Override
    public void studentSelectCourse(StudentSelectCourseData json) throws Exception {
        String studentId = json.getStudentId();
        Student student = memberMapper.getStudentInfoById(studentId);
        if (student == null) {
            throw new Exception("学生不存在！");
        }
        String courseId = json.getCourseId();
        Course course = courseMapper.getCourseInfoByCourseId(courseId);
        if (course == null) {
            throw new Exception("课程不存在！");
        }
        if (this.judgeCourseSelectedStatus(json)) {
            throw new Exception("课程已经被该学生选择！");
        }
        courseMapper.insertCoursesStudents(json);
    }

    @Override
    public boolean judgeCourseSelectedStatus(StudentSelectCourseData json) {
        StudentSelectCourseData data = courseMapper.selectCoursesStudents(json);
        return data != null;
    }

    @Override
    public void exitCourse(StudentSelectCourseData json) throws Exception {
        StudentSelectCourseData data = courseMapper.selectCoursesStudents(json);
        if (data == null) {
            throw new Exception("学生退选的课程不存在！");
        }
        courseMapper.deleteCoursesStudents(json);
    }

    @Override
    public ArrayList<Course> getAllSelectedCourse(String studentId) {
        return courseMapper.getCourseByStudentId(studentId);
    }

    @Override
    public ArrayList<Student> getSelectTheCourseStudents(String courseId) {
        return courseMapper.getSelectTheCourseStudents(courseId);
    }

    @Override
    public void updateStudentScore(StudentSelectCourseData data) {
        courseMapper.updateStudentScore(data);
    }

    @Override
    public ArrayList<Course> getTeacherCourse(String teacherId) {
        return courseMapper.getTeacherCourse(teacherId);
    }

}
