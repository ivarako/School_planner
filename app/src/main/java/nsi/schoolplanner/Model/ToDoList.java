package nsi.schoolplanner.Model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity
public class ToDoList {

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    private Date date;
    private String item;
    private boolean checked;

    @NotNull
    private Long subjectId;

    @ToOne(joinProperty = "subjectId")
    private Subject subject;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 48930133)
    private transient ToDoListDao myDao;

    @Generated(hash = 2119378581)
    public ToDoList(Long id, @NotNull Date date, String item, boolean checked,
                    @NotNull Long subjectId) {
        this.id = id;
        this.date = date;
        this.item = item;
        this.checked = checked;
        this.subjectId = subjectId;
    }

    @Generated(hash = 707050199)
    public ToDoList() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getItem() {
        return this.item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public boolean getChecked() {
        return this.checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Long getSubjectId() {
        return this.subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    @Generated(hash = 711858396)
    private transient Long subject__resolvedKey;

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
    @Generated(hash = 1133640380)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getToDoListDao() : null;
    }
}