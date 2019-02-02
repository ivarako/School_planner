package nsi.schoolplanner.Model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Timetable {

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    private int column;

    @NotNull
    private int row;

    @NotNull
    private String subject;

    public Timetable(){
        
    }

    @Generated(hash = 873639454)
    public Timetable(Long id, int column, int row, @NotNull String subject) {
        this.id = id;
        this.column = column;
        this.row = row;
        this.subject = subject;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getColumn() {
        return this.column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return this.row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

}