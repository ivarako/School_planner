package nsi.schoolplanner.Model;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity
public class Exam {

    @Id(autoincrement = true)
    private Long id;

    private String title;
    private Date date;
    private String note;

    @NotNull
    private Long subjectId;

    private Long gradeId;

    @ToOne(joinProperty = "subjectId")
    private Subject subject;

    @ToOne(joinProperty = "gradeId")
    private Grade grade;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 973692038)
    private transient ExamDao myDao;

    @Generated(hash = 711858396)
    private transient Long subject__resolvedKey;

    @Generated(hash = 74796936)
    private transient Long grade__resolvedKey;

    @Generated(hash = 1863795948)
    public Exam(Long id, String title, Date date, String note,
            @NotNull Long subjectId, Long gradeId) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.note = note;
        this.subjectId = subjectId;
        this.gradeId = gradeId;
    }
    @Generated(hash = 945526930)
    public Exam() {
    }

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Date getDate() {
        return this.date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public String getNote() {
        return this.note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public Long getSubjectId() {
        return this.subjectId;
    }
    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }
    public Long getGradeId() {
        return this.gradeId;
    }
    public void setGradeId(Long gradeId) {
        this.gradeId = gradeId;
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 86684749)
    public Subject getSubject() {
        Long __key = this.subjectId;
        if (subject__resolvedKey == null || !subject__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SubjectDao targetDao = daoSession.getSubjectDao();
            Subject subjectNew = targetDao.load(__key);
            synchronized (this) {
                subject = subjectNew;
                subject__resolvedKey = __key;
            }
        }
        return subject;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 476367223)
    public void setSubject(@NotNull Subject subject) {
        if (subject == null) {
            throw new DaoException(
                    "To-one property 'subjectId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.subject = subject;
            subjectId = subject.getId();
            subject__resolvedKey = subjectId;
        }
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 2142581284)
    public Grade getGrade() {
        Long __key = this.gradeId;
        if (grade__resolvedKey == null || !grade__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            GradeDao targetDao = daoSession.getGradeDao();
            Grade gradeNew = targetDao.load(__key);
            synchronized (this) {
                grade = gradeNew;
                grade__resolvedKey = __key;
            }
        }
        return grade;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2134456703)
    public void setGrade(Grade grade) {
        synchronized (this) {
            this.grade = grade;
            gradeId = grade == null ? null : grade.getId();
            grade__resolvedKey = gradeId;
        }
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1730563422)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getExamDao() : null;
    }




}