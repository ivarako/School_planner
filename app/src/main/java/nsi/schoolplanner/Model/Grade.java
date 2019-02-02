package nsi.schoolplanner.Model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity
public class Grade {

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    private int grade;

    @NotNull
    private Long examId;

    @ToOne(joinProperty = "examId")
    private Exam exam;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 681281562)
    private transient GradeDao myDao;

    @Generated(hash = 1239735537)
    public Grade(Long id, int grade, @NotNull Long examId) {
        this.id = id;
        this.grade = grade;
        this.examId = examId;
    }

    @Generated(hash = 2042976393)
    public Grade() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getGrade() {
        return this.grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public Long getExamId() {
        return this.examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    @Generated(hash = 1667973718)
    private transient Long exam__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1741793051)
    public Exam getExam() {
        Long __key = this.examId;
        if (exam__resolvedKey == null || !exam__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ExamDao targetDao = daoSession.getExamDao();
            Exam examNew = targetDao.load(__key);
            synchronized (this) {
                exam = examNew;
                exam__resolvedKey = __key;
            }
        }
        return exam;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1996539340)
    public void setExam(@NotNull Exam exam) {
        if (exam == null) {
            throw new DaoException(
                    "To-one property 'examId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.exam = exam;
            examId = exam.getId();
            exam__resolvedKey = examId;
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
    @Generated(hash = 1187286414)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getGradeDao() : null;
    }
}
