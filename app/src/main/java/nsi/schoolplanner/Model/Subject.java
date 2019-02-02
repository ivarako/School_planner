package nsi.schoolplanner.Model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Unique;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity
public class Subject {

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    @Unique
    private String name;

    private int finalGrade;

    @ToMany(referencedJoinProperty = "subjectId")
    @OrderBy("date ASC")
    private List<Exam> exams;

    @ToMany(referencedJoinProperty = "subjectId")
    @OrderBy("date ASC")
    private List<ToDoList> toDoLists;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1644932788)
    private transient SubjectDao myDao;

    @Generated(hash = 392019671)
    public Subject(Long id, @NotNull String name, int finalGrade) {
        this.id = id;
        this.name = name;
        this.finalGrade = finalGrade;
    }

    @Generated(hash = 1617906264)
    public Subject() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFinalGrade() {
        return this.finalGrade;
    }

    public void setFinalGrade(int finalGrade) {
        this.finalGrade = finalGrade;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1112203453)
    public List<Exam> getExams() {
        if (exams == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ExamDao targetDao = daoSession.getExamDao();
            List<Exam> examsNew = targetDao._querySubject_Exams(id);
            synchronized (this) {
                if (exams == null) {
                    exams = examsNew;
                }
            }
        }
        return exams;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 841969952)
    public synchronized void resetExams() {
        exams = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 13376011)
    public List<ToDoList> getToDoLists() {
        if (toDoLists == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ToDoListDao targetDao = daoSession.getToDoListDao();
            List<ToDoList> toDoListsNew = targetDao._querySubject_ToDoLists(id);
            synchronized (this) {
                if (toDoLists == null) {
                    toDoLists = toDoListsNew;
                }
            }
        }
        return toDoLists;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 156055337)
    public synchronized void resetToDoLists() {
        toDoLists = null;
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
    @Generated(hash = 937984622)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSubjectDao() : null;
    }
}
