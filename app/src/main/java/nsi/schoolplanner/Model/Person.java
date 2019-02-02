package nsi.schoolplanner.Model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Person {

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    private String name;

    private String pathToImg;

    @Generated(hash = 204571960)
    public Person(Long id, @NotNull String name, String pathToImg) {
        this.id = id;
        this.name = name;
        this.pathToImg = pathToImg;
    }

    @Generated(hash = 1024547259)
    public Person() {
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

    public String getPathToImg() {
        return this.pathToImg;
    }

    public void setPathToImg(String pathToImg) {
        this.pathToImg = pathToImg;
    }
}
