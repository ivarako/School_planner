package nsi.schoolplanner.Model;

import android.content.Context;
import android.content.SharedPreferences;


import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseOpenHelper;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class DBManagerSingletone {
    private static DBManager instance = null;

    public DBManagerSingletone(){ }

    public static DBManager getInstance(Context context){
        if(instance == null)
            instance = new DBManager(context);

        return instance;
    }

    public static class DBManager{

        private DaoMaster.DevOpenHelper helper;
        private DatabaseOpenHelper database;
        private Database dbhelper;
        private DaoMaster daoMaster;
        public DaoSession daoSession;
        private Context context;

        public DBManager(Context context){

            this.context=context;
            helper = new DaoMaster.DevOpenHelper(this.context, "notes-db");
            dbhelper = helper.getWritableDb();

            daoSession = new DaoMaster(dbhelper).newSession();


        }


        // -------------------------  IVINE FUNKCIJE  ----------------------------------------------------------------------------

        public void createPerson(Person person){
            daoSession.insert(person);
        }

        public void updatePerson(Person person){
            daoSession.getPersonDao().update(person);
        }

        public Person getPerson(String name){
            PersonDao personDao=daoSession.getPersonDao();
            Person person = personDao.queryBuilder()
                    .where(PersonDao.Properties.Name.eq(name))
                    .list().get(0);
            return person;
        }

        public Person getPerson(){
            List<Person> personList = daoSession.getPersonDao().loadAll();
            if(personList.size() > 0)
                return personList.get(0);

            return  null;
        }

        public Long createExam(Exam exam){
            return daoSession.insert(exam); //returns id
        }

        public Exam getExam(Long id){
            return daoSession.getExamDao().load(id);
        }

        public Exam getExam(String title){
            ExamDao examDao=daoSession.getExamDao();
            Exam exam = examDao.queryBuilder()
                    .where(ExamDao.Properties.Title.eq(title))
                    .list().get(0);
            return exam;
        }

        public List<Exam> getFutureExams(){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());

            ExamDao examDao = daoSession.getExamDao();
            List<Exam> exams = examDao.queryBuilder()
                    .where(ExamDao.Properties.Date.gt(calendar.getTime()))
                    .orderAsc(ExamDao.Properties.Date)
                    .list();

            return exams;
        }

        public void updateExam(Exam exam){
            daoSession.getExamDao().update(exam);
        }

        public void deleteExam(Exam exam){
            if(exam.getGrade() != null)
                daoSession.getGradeDao().delete(exam.getGrade());

            daoSession.getExamDao().delete(exam);
        }

        public List<Exam> getExamsForDate(Date date){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            Date startDate = calendar.getTime();

            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            Date endDate = calendar.getTime();

            ExamDao examDao = daoSession.getExamDao();
            List<Exam> exams = examDao.queryBuilder()
                    .where(ExamDao.Properties.Date.between(startDate, endDate))
                    .list();

            return exams;
        }

        public void deleteGradesAndTimeTable(){
            daoSession.getGradeDao().deleteAll();
            daoSession.getExamDao().deleteAll();
            daoSession.getTimetableDao().deleteAll();

            List<Subject> allSubjects = getAllSubjects();

            for(int i=0; i<allSubjects.size(); i++){
                if(allSubjects.get(i).getFinalGrade() != 0)
                    allSubjects.get(i).setFinalGrade(0);
                daoSession.update(allSubjects.get(i));
            }
        }

        public Long createToDoList(ToDoList toDoList){
            return daoSession.insert(toDoList);
        }

        public void updateToDoList(ToDoList toDoList){
            daoSession.getToDoListDao().update(toDoList);
        }

        public List<Date> getDatesForToDoList(){
            ArrayList<Date> dates = new ArrayList<>();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            calendar.add(Calendar.DAY_OF_YEAR, -dayOfWeek);
            calendar.add(Calendar.DAY_OF_YEAR, 1);

            ToDoListDao toDoListDao = daoSession.getToDoListDao();
            List<ToDoList> toDoLists = toDoListDao.queryBuilder()
                    .where(ToDoListDao.Properties.Date.gt(calendar.getTime()))
                    .orderAsc(ToDoListDao.Properties.Date)
                    .list();

            for(ToDoList toDoList : toDoLists){
                if(!dates.contains(toDoList.getDate()))
                    dates.add(toDoList.getDate());
            }

            return dates;
        }

        public List<ToDoList> getToDoListForDate(Date date){
            ToDoListDao toDoListDao = daoSession.getToDoListDao();
            return toDoListDao.queryBuilder()
                    .where(ToDoListDao.Properties.Date.eq(date))
                    .list();
        }


        //---------------------------------------------------------------------------------------------------------------------------------------



        public Long createSubject(Subject subject){

            return DBManagerSingletone.getInstance(this.context).daoSession.insert(subject);
        }

        public Subject getSubjectByName(String name){

            SubjectDao subjectDao=daoSession.getSubjectDao();
            List<Subject> subjects = subjectDao.queryBuilder()
                    .where(SubjectDao.Properties.Name.eq(name))
                    .list();

            if(subjects.size()>0)
                return subjects.get(0);
            else
                return null;

        }

        public Subject getSubjectById(Long id){
            SubjectDao subjectDao = daoSession.getSubjectDao();
            Subject subject = subjectDao.queryBuilder().where(SubjectDao.Properties.Id.eq(id)).list().get(0);
            return subject;
        }

        public List<Subject> getAllSubjects(){

            SubjectDao subjectDao=daoSession.getSubjectDao();
            List<Subject>subjects=subjectDao.queryBuilder().list();
            return  subjects;
        }


        public void deleteSubject(String name){

            Subject subject=getSubjectByName(name);
            SubjectDao subjectDao=daoSession.getSubjectDao();
            subjectDao.delete(subject);
        }

        public List<Exam> getAllSubjectExams(Subject subject){

            ExamDao examDao=daoSession.getExamDao();
            List<Exam> exams=examDao.queryBuilder().where(ExamDao.Properties.SubjectId.eq(subject.getId())).list();

            if(exams.size()>0){
                return exams;
            }
            else
                return null;
        }
        public void addGrade(Exam exam, int grade){

            Grade newGrade=new Grade();
            newGrade.setGrade(grade);
            newGrade.setExam(exam);
            newGrade.setExamId(exam.getId());

            daoSession.insert(newGrade);

            GradeDao gradeDao=daoSession.getGradeDao();

            Grade gradedb=gradeDao.queryBuilder().where(GradeDao.Properties.ExamId.eq(exam.getId())).list().get(0);
            exam.setGrade(gradedb);
            exam.setGradeId(gradedb.getId());
            daoSession.getExamDao().update(exam);
        }

        public  Grade getGrade(Exam exam){

            ExamDao examDao=daoSession.getExamDao();
            Exam examdb=examDao.queryBuilder().where(ExamDao.Properties.Id.eq(exam.getId())).list().get(0);
            return examdb.getGrade();
        }

        public void editGrade(Exam exam, int grade){

            Grade gradedb=exam.getGrade();
            gradedb.setGrade(grade);
            daoSession.getGradeDao().update(gradedb);
        }

        public void deleteGrade(Exam exam){

            Grade gradedb=exam.getGrade();
            daoSession.delete(gradedb);

            exam.setGradeId(null);
            exam.setGrade(null);
            daoSession.update(exam);
        }

        public void deleteSubject(Subject subject){
            ExamDao examDao=daoSession.getExamDao();
            List<Exam>exams=examDao.queryBuilder().where(ExamDao.Properties.SubjectId.eq(subject.getId())).list();
            for(int i=0;i<exams.size();i++){
                if(exams.get(i).getGrade()!= null) {
                    Grade grade = exams.get(i).getGrade();
                    daoSession.delete(grade);
                }
                daoSession.delete(exams.get(i));
            }
            daoSession.delete(subject);
        }
        public void editSubject(Subject subject,String name){

            subject.setName(name);
            daoSession.update(subject);
        }

        public void addFinalGrade(Subject subject,int grade){

            subject.setFinalGrade(grade);
            daoSession.update(subject);
        }

        public void createTimetable(int row,int column,String subject){

            Timetable timetable=new Timetable();
            timetable.setColumn(column);
            timetable.setRow(row);
            timetable.setSubject(subject);
            daoSession.insert(timetable);
        }

        public String getTimetable(int row,int column){
            TimetableDao timetableDao=daoSession.getTimetableDao();
            List<Timetable> timetables=timetableDao.queryBuilder().where(TimetableDao.Properties.Row.eq(row),TimetableDao.Properties.Column.eq(column)).list();
            if(timetables.size()>0){
                if(timetables.get(0).getSubject()!=null){
                    return timetables.get(0).getSubject();
                }
            }
            return null;
        }

        public void editTimetable(int row,int column,String subject){
            TimetableDao timetableDao=daoSession.getTimetableDao();
            List<Timetable> timetables=timetableDao.queryBuilder().where(TimetableDao.Properties.Row.eq(row),TimetableDao.Properties.Column.eq(column)).list();
            if(timetables.size()>0){
                if(timetables.get(0).getSubject()!=null){
                    timetables.get(0).setSubject(subject);
                    daoSession.update(timetables.get(0));
                }
            }
        }
    }
}